/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-11-13 16:07
 */
package me.zhengjie.utils;

import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.service.dto.HisCkItemDto;
import org.apache.http.util.TextUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * HIS 工具类
 *
 * @author yanjun
 * @date 2020-11-13 16:07
 */
public class HisUtil {

    public static String getQueryString(HisCkItemVo vo) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("string")
                .addNamespace("xmlns", "http://tempuri.org/");
        root = root.addElement("Request");
        root.addElement("PatientInfo")
                .addText(vo.getPatientInfo());
        root.addElement("InfoType")
                .addText(vo.getInfoType().getValue());
        return root.asXML();
    }

    public static List<HisCkItemDto> parseCkItemList(InputStream inputStream) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        return parseCkItemList(document);
    }

    public static List<HisCkItemDto> parseCkItemList(String content) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader(content));
        return parseCkItemList(document);
    }

    private static List<HisCkItemDto> parseCkItemList(Document document) {
        Element root = document.getRootElement();
        Element response = root.element("Response");
        String resultCode = response.element("ResultCode").getTextTrim();

        // 判断查询结果
        if (!"0".equals(resultCode)) {
            String errorMsg = response.element("ErrorMsg").getTextTrim();
            if (StringUtils.isEmpty(errorMsg)) {
                errorMsg = "his 查询失败";
            }
            throw new IllegalStateException(errorMsg);
        }

        Element infoList = response.element("InfoList");
        List<HisCkItemDto> list = new ArrayList<>();
        for (Iterator<Element> it = infoList.elementIterator("Info"); it.hasNext(); ) {
            Element item = it.next();
            HisCkItemDto info = new HisCkItemDto();
            info.setPatientId(getItemLong(item, "PatientId"));
            info.setName(getItemText(item, "Name"));
            info.setMobilePhone(getItemText(item, "MobliePhone"));
            info.setMrn(getItemText(item, "MRN"));
            info.setVisitDept(getItemText(item, "VisitDept"));
            info.setVisitDate(getItemDate(item, "VisitDate"));
            info.setItemCode(getItemText(item, "ItemCode"));
            info.setItemName(getItemText(item, "ItemName"));
            info.setPrice(getItemMoney(item, "Price"));
            info.setAmount(getItemLong(item, "Amount"));
            info.setUnit(getItemText(item, "Unit"));
            info.setCosts(getItemMoney(item, "Costs"));
            info.setActualCosts(getItemMoney(item, "ActualCosts"));
            info.setPatItemId(getItemLong(item, "PatItemId"));
            info.setVisitId(getItemLong(item, "VisitId"));
            list.add(info);
        }

        return list;
    }

    private static String getItemText(Element parent, String child) {
        return getItemText(parent.element(child));
    }

    private static String getItemText(Element element) {
        if (null == element) {
            return null;
        }
        return element.getTextTrim();
    }

    private static Long getItemMoney(Element parent, String child) {
        return getItemMoney(parent.element(child));
    }

    private static Long getItemMoney(Element element) {
        String text = getItemText(element);
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        return new BigDecimal(text).multiply(BigDecimal.valueOf(100)).longValue();
    }

    private static Double getItemDouble(Element parent, String child) {
        return getItemDouble(parent.element(child));
    }

    private static Double getItemDouble(Element element) {
        String text = getItemText(element);
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        return Double.parseDouble(text);
    }

    private static Long getItemLong(Element parent, String child) {
        return getItemLong(parent.element(child));
    }

    private static Long getItemLong(Element element) {
        String text = getItemText(element);
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        return Long.parseLong(text);
    }

    private static final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat format4 = new SimpleDateFormat("yyyy/MM/dd");

    private static Date getItemDate(Element parent, String child) {
        return getItemDate(parent.element(child));
    }

    private static Date getItemDate(Element element) {
        String text = getItemText(element);
        if (TextUtils.isEmpty(text)) {
            return null;
        }

        try {
            try {
                try {
                    return format1.parse(text);
                } catch (ParseException e) {
                    return format2.parse(text);
                }
            } catch (ParseException e1) {
                try {
                    return format3.parse(text);
                } catch (ParseException e2) {
                    return format4.parse(text);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
