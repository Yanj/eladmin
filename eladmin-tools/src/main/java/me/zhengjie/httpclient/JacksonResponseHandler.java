package me.zhengjie.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author yanjun
 * @date 2020-11-15 19:57
 */
public class JacksonResponseHandler<T> implements ResponseHandler<T> {

    private final ObjectMapper objectMapper;

    private final Class<? extends T> clazz;

    public JacksonResponseHandler(ObjectMapper objectMapper, Class<? extends T> clazz) {
        this.objectMapper = objectMapper;
        this.clazz = clazz;
    }

    @Override
    public T handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        String content = EntityUtils.toString(httpResponse.getEntity());
        return objectMapper.readValue(content, clazz);
    }

}
