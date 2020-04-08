package com._98point6.droptoken.vertx.mysql.api;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface RxMySQL {
    
    <T> Single<T> selectOne(String query, RxRowMapper<T> rowMapper, Object... params);
    <T> Observable<T> selectMany(String query, RxRowMapper<T> rowMapper, Object... params);
    
    Single<Long> insert(String query, Object... params);
    Single<Integer> update(String query, Object... params);
    
}
