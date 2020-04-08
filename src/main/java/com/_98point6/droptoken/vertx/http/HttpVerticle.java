package com._98point6.droptoken.vertx.http;

import com._98point6.droptoken.vertx.annotations.Routes;
import com._98point6.droptoken.vertx.http.interfaces.RoutesBuilder;
import io.reactivex.Completable;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.CorsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class HttpVerticle extends AbstractVerticle {
    
    private Logger logger = LoggerFactory.getLogger(HttpVerticle.class);
    
    @Value("${http.port}")
    private int httpPort;
    
    @Autowired
    private ApplicationContext ctx;

    @Override
    public Completable rxStart() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        CorsHandler corsHandler = CorsHandler.create("*")
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Content-Type")
                .allowedHeader("Accept")
                .allowedHeader("Authorization");
        
        router.route()
                .handler(corsHandler)
                .handler(BodyHandler.create());
        
        ctx.getBeansWithAnnotation(Routes.class)
                .forEach((name, bean) -> ((RoutesBuilder)bean).build(router));
        
        return server
                .requestHandler(router)
                .rxListen(httpPort)
                .doOnSuccess(this::handleSuccess)
                .doOnError(this::handleFailure)
                .ignoreElement();
    }

    private void handleSuccess(HttpServer httpServer) {
        logger.info("HTTP server running on port " + httpPort);
    }
    
    private void handleFailure(Throwable error) {
        logger.error("Error while starting http server", error);
    }
}
