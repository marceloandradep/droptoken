package com._98point6.droptoken.vertx;

import com._98point6.droptoken.vertx.http.HttpVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com._98point6.droptoken.vertx.factories.SpringVerticleFactory.springify;

@Component
@Profile("!test")
public class VertxStartup {
    
    private Logger logger = LoggerFactory.getLogger(VertxStartup.class);
    
    @Value("${http.instances}")
    private int httpInstances;
    
    @Autowired
    private Vertx vertx;

    @PostConstruct
    public void deployVerticles() {
        logger.info("Deploying verticles...");
        
        vertx
                .rxDeployVerticle(springify(HttpVerticle.class.getName()), new DeploymentOptions().setInstances(httpInstances))
                .subscribe(this::handleSuccess, this::handleFailure);
    }
    
    private void handleSuccess(String message) {
        logger.info("All verticles deployed.");
    }
    
    private void handleFailure(Throwable t) {
        logger.error("Error while deploying verticles.", t);
        System.exit(-1);
    }
}
