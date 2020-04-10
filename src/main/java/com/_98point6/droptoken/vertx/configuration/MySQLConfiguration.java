package com._98point6.droptoken.vertx.configuration;

import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class MySQLConfiguration {
    
    @Bean
    public MySQLPool mySQLPool(
            @Value("${datasource.port}") int port,
            @Value("${datasource.host}") String host,
            @Value("${datasource.database}") String database,
            @Value("${datasource.user}") String user,
            @Value("${datasource.password}") String password) {
        
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(port)
                .setHost(host)
                .setDatabase(database)
                .setUser(user)
                .setPassword(password);

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        return MySQLPool.pool(connectOptions, poolOptions);
    }
    
}
