package com.jark.template.common.web.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;

import com.jark.template.common.utils.constant.HeaderConst;
import com.jark.template.common.utils.json.JsonUtil;
import com.jark.template.common.utils.resp.R;

import cn.hutool.core.collection.CollUtil;
import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 拦截所有 feign 请求, 所有feign请求都带有from
 *
 * @author ponder
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeignResponseDecoder implements Decoder {

    private final ObjectFactory<HttpMessageConverters> messageConverters;


    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        final Collection<String> headers = response.headers().get(HeaderConst.EXCEPTION);
        FeignResponseAdapter adapter = new FeignResponseAdapter(response);
        if (CollUtil.isNotEmpty(headers)) {
            List<HttpMessageConverter<?>> converters = messageConverters.getObject().getConverters();
            @SuppressWarnings({"unchecked", "rawtypes"})
            HttpMessageConverterExtractor<?> extractor = new HttpMessageConverterExtractor(R.class, converters);
            final R result = (R) extractor.extractData(adapter);
            log.warn("用户请求路径异常:{},result:{}", response.request(), result);
            throw new DecodeException(response.status(), result.getMsg(), response.request());
        } else {
            if (type instanceof Class || type instanceof ParameterizedType || type instanceof WildcardType) {
                Object o = JsonUtil.toBean(adapter.getBody(), type);
                log.debug("Feign调用返回数据为:{}", o);
                return o;
            }
        }
        log.error("无法解析的数据类型,type:{},request:{},response:{}", type, response.request(), response);
        throw new DecodeException(response.status(), "无法解析类型",
            response.request());
    }


    /**
     * Feign响应处理
     * @author ponder
     */
    private static final class FeignResponseAdapter implements ClientHttpResponse {

        private final Response response;

        private FeignResponseAdapter(Response response) {
            this.response = response;
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.valueOf(response.status());
        }

        @Override
        public int getRawStatusCode() {
            return response.status();
        }

        @Override
        public String getStatusText() {
            return response.reason();
        }

        @Override
        public void close() {
            try {
                response.body().close();
            } catch (IOException expected) {
                // Ignore exception on close...
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            return response.body().asInputStream();
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (Map.Entry<String, Collection<String>> entry : response.headers().entrySet()) {
                httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return httpHeaders;
        }

    }
}
