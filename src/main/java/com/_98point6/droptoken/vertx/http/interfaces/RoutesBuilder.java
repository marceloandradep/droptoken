package com._98point6.droptoken.vertx.http.interfaces;

import io.vertx.reactivex.ext.web.Router;

public interface RoutesBuilder {

    String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    
    void build(Router router);
    
}
