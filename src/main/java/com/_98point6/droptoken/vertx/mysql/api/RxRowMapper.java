package com._98point6.droptoken.vertx.mysql.api;

import io.vertx.reactivex.sqlclient.Row;

public interface RxRowMapper<T> {
    
    T mapRow(Row row);
    
}
