package com._98point6.droptoken.vertx.factories;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringVerticleFactory implements VerticleFactory {
    
    private static final String PREFIX = "spring";
    
    @Autowired
    private ApplicationContext ctx;
    
    @Override
    public String prefix() {
        return PREFIX;
    }

    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        Class clazz = Class.forName(verticleName.substring(PREFIX.length() + 1));
        return (Verticle) ctx.getBean(clazz);
    }
    
    static public String springify(String verticleName) {
        return PREFIX + ":" + verticleName;
    }
    
}
