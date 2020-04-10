package com._98point6.droptoken.vertx.configuration;

import com._98point6.droptoken.vertx.factories.SpringVerticleFactory;
import io.vertx.reactivex.core.Vertx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class VertxConfiguration {
    
    @Bean
    public Vertx vertx(SpringVerticleFactory factory) {
        Vertx vertx = Vertx.vertx();
        vertx.getDelegate().registerVerticleFactory(factory);
        return vertx;
    }
    
}
