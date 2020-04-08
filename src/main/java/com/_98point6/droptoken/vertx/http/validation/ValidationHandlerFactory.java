package com._98point6.droptoken.vertx.http.validation;

import io.vertx.reactivex.ext.web.api.validation.HTTPRequestValidationHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ValidationHandlerFactory {
    
    private Logger logger = LoggerFactory.getLogger(ValidationHandlerFactory.class);
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    public HTTPRequestValidationHandler getSchemaValidationHandler(String schema) {
        Resource resource = resourceLoader.getResource("classpath:json-schemas/" + schema + ".json");
        
        try {
            return HTTPRequestValidationHandler.create().addJsonBodySchema(IOUtils.toString(resource.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException("Could not load schema " + schema, e);
        }
    }
    
}
