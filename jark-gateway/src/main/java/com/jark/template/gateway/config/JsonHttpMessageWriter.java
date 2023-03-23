package com.jark.template.gateway.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.jark.template.common.utils.json.JsonUtil;

import reactor.core.publisher.Mono;

/**
 * @author ponder
 */
@Component
public class JsonHttpMessageWriter implements HttpMessageWriter<Map<String, Object>> {
    @NonNull
    @Override
    public List<MediaType> getWritableMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_JSON);
    }

    @Override
    public boolean canWrite(final @NonNull ResolvableType elementType, final MediaType mediaType) {
        return MediaType.APPLICATION_JSON.includes(mediaType);
    }

    @NonNull
    @Override
    public Mono<Void> write(final @NonNull Publisher<? extends Map<String, Object>> inputStream,
                            final @NonNull ResolvableType elementType,
                            final MediaType mediaType,
                            final @NonNull ReactiveHttpOutputMessage message,
                            final @NonNull Map<String, Object> hints) {
        return Mono.from(inputStream).flatMap(m -> message.writeWith(Mono.just(message.bufferFactory().wrap(transform2Json(m).getBytes()))));
    }

    private String transform2Json(final Map<String, Object> sourceMap) {
        return JsonUtil.toJSON(sourceMap);
    }
}
