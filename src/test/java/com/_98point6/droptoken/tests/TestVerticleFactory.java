package com._98point6.droptoken.tests;

import com._98point6.droptoken.vertx.factories.SpringVerticleFactory;
import com._98point6.droptoken.vertx.http.HttpVerticle;
import io.vertx.core.Verticle;
import org.springframework.stereotype.Component;

@Component
public class TestVerticleFactory extends SpringVerticleFactory {

    static int lastPortAssigned = 8080;
    
    private int port;
    {
        port = lastPortAssigned + 1;
        lastPortAssigned = port;
    }
    
    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        Verticle verticle = super.createVerticle(verticleName, classLoader);
        
        if (verticle instanceof HttpVerticle) {
            HttpVerticle httpVerticle = (HttpVerticle) verticle;
            httpVerticle.setHttpPort(port);
        }
        
        return verticle;
    }

    public int getPort() {
        return port;
    }
}
