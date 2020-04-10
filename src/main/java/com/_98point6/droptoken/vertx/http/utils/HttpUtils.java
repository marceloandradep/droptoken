package com._98point6.droptoken.vertx.http.utils;

import com._98point6.droptoken.exceptions.DropTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeTypeUtils;

public class HttpUtils {
    
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    
    private static ObjectMapper mapper = new ObjectMapper();
    
    static {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        FilterProvider provider = new SimpleFilterProvider()
                .addFilter("customPropertyFilter", new CustomPropertyFilter());
        
        mapper.setFilterProvider(provider);
    }
    
    public static <T> T getParam(RoutingContext ctx, String name, Class<T> clazz) {
        String param = ctx.request().getParam(name);
        
        if (param == null) {
            return null;
        }
        
        Object value = null;        
        
        if (Long.class.isAssignableFrom(clazz)) {
            value = Long.parseLong(param);
        } else if (Integer.class.isAssignableFrom(clazz)) {
            value = Integer.parseInt(param);
        } else if (String.class.isAssignableFrom(clazz)) {
            value = param;
        } else {
            throw new RuntimeException("Unsupported conversion (" + param + ") to (" + clazz);
        }
        
        return clazz.cast(value);
    }
    
    public static <T> T parseBody(RoutingContext ctx, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(ctx.getBodyAsString(), clazz);
    }
    
    public static void ok(RoutingContext ctx, Object response) {
        jsonResponse(ctx, response, HttpResponseStatus.OK.code());
    }

    public static void accepted(RoutingContext ctx) {
        ctx
                .response()
                .setStatusCode(HttpResponseStatus.ACCEPTED.code())
                .end();
    }
    
    private static void jsonResponse(RoutingContext ctx, Object response, int statusCode) {
        String body = "";

        try {
            body = mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting (" + response + ") to string.", e);
        }

        ctx
                .response()
                .setStatusCode(statusCode)
                .putHeader(HttpHeaderNames.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .end(body);
    }
    
    public static void handleFailure(RoutingContext ctx, Throwable error) {
        logger.error("Unexpected error.", error);
        
        if (error instanceof DropTokenException) {
            ctx
                    .response()
                    .setStatusCode(((DropTokenException)error).getStatusCode())
                    .end();
        } else {
            ctx
                    .response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end();
        }
    }
    
}
