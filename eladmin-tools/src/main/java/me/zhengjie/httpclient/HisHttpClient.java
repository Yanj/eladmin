package me.zhengjie.httpclient;

import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.HisUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-11-15 19:37
 */
@Component
public class HisHttpClient extends AbstractHttpClient {

    private ResponseHandler<List<HisCkItemDto>> hisCkItemListResponseHandler;

    public HisHttpClient(HttpClientConfig config, ConnectionManager connectionManager) {
        super(config, connectionManager);
    }

    public List<HisCkItemDto> query(HisCkItemVo vo) throws Exception {
        String queryString = HisUtil.getQueryString(vo);
        String url = getConfig().getOpUrl() + "?xmlpara=" + URLEncoder.encode(queryString, "utf-8");
        System.out.println("url:" + url);
        return doGet(url, getHisCkItemListResponseHandler());
    }

    public ResponseHandler<List<HisCkItemDto>> getHisCkItemListResponseHandler() {
        if (null == hisCkItemListResponseHandler) {
            hisCkItemListResponseHandler = new HisCkItemListResponseHandler();
        }
        return hisCkItemListResponseHandler;
    }

    public void setHisCkItemListResponseHandler(ResponseHandler<List<HisCkItemDto>> hisCkItemListResponseHandler) {
        this.hisCkItemListResponseHandler = hisCkItemListResponseHandler;
    }

    private static class HisCkItemListResponseHandler extends AbstractResponseHandler<List<HisCkItemDto>> {

        @Override
        public List<HisCkItemDto> handleEntity(HttpEntity httpEntity) throws IOException {
            try {
                String content = EntityUtils.toString(httpEntity);
                System.out.println("content:" + content);
                return HisUtil.parseCkItemList(content.replace("&lt;", "<").replace("&gt;", ">"));
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof IOException) {
                    throw (IOException) e;
                }
                throw new IOException(e.getMessage(), e);
            }
        }
    }

}
