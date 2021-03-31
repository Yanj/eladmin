package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.modules.yy.domain.PatientSourceEnum;
import me.zhengjie.utils.HisCkInfoTypeEnum;
import me.zhengjie.utils.StringUtils;
import me.zhengjie.utils.enums.YesNoEnum;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 14:36
 */
@Data
public class PatientCriteria extends BaseCriteria implements Serializable {

    @Query
    private Long id;

    @Query
    private String code;

    @Query
    private PatientSourceEnum source;

    @Query(blurry = "mrn,name,phone,code")
    private String blurry;

    @Query
    private String mrn;

    @Query
    private String name;

    @Query
    private String phone;

    @Query
    private YesNoEnum status;

    /**
     * 设置<患者同步>查询信息
     *
     * @param patient .
     * @return true=设置成功
     */
    public boolean setPatientSync(PatientSync patient) {
        if (null == patient.getInfoType() || StringUtils.isEmpty(patient.getInfoType())) {
            return false;
        }

        HisCkInfoTypeEnum infoType = HisCkInfoTypeEnum.valueOf(patient.getInfoType());
        if (infoType == HisCkInfoTypeEnum.PHONE) {
            this.phone = patient.getPatientInfo();
        } else if (infoType == HisCkInfoTypeEnum.MRN) {
            this.mrn = patient.getPatientInfo();
        } else if (infoType == HisCkInfoTypeEnum.NAME) {
            this.name = patient.getPatientInfo();
        } else {
            return false;
        }
        return true;
    }

}
