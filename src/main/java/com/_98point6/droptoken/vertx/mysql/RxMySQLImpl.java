package com._98point6.droptoken.vertx.mysql;

import com._98point6.droptoken.vertx.mysql.api.RxMySQL;
import com._98point6.droptoken.vertx.mysql.api.RxRowMapper;
import com._98point6.droptoken.vertx.mysql.exceptions.EmptyResultException;
import com._98point6.droptoken.vertx.mysql.exceptions.TooManyResultsException;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.reactivex.mysqlclient.MySQLClient;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlResult;
import io.vertx.reactivex.sqlclient.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//@Component
@Profile("!test")
public class RxMySQLImpl implements RxMySQL {
    
    @Autowired
    private MySQLPool mySQLPool;

    @Override
    public <T> Single<T> selectOne(String query, RxRowMapper<T> rowMapper, Object...params) {
        return mySQLPool
                .rxPreparedQuery(query, Tuple.wrap(params))
                .map(rows -> handleSingle(rows, rowMapper));
    }

    @Override
    public <T> Observable<T> selectMany(String query, RxRowMapper<T> rowMapper, Object...params) {
        return mySQLPool
                .rxPreparedQuery(query, Tuple.wrap(params))
                .flatMapObservable(Observable::fromIterable)
                .map(rowMapper::mapRow);
    }

    @Override
    public Single<Long> insert(String query, Object...params) {
        return mySQLPool
                .rxPreparedQuery(query, Tuple.wrap(params))
                .map(rows -> rows.property(MySQLClient.LAST_INSERTED_ID));
    }

    @Override
    public Single<Integer> update(String query, Object...params) {
        return mySQLPool
                .rxPreparedQuery(query, Tuple.wrap(params))
                .map(SqlResult::rowCount);
    }
    
    private <T> T handleSingle(RowSet<Row> rows, RxRowMapper<T> rowMapper) {
        if (rows.size() == 0) {
            throw new EmptyResultException();
        }
        
        if (rows.size() > 1) {
            throw new TooManyResultsException();
        }
        
        return rowMapper.mapRow(rows.iterator().next());
    }
    
}
