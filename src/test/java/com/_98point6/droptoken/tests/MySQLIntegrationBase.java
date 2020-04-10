package com._98point6.droptoken.tests;

import com._98point6.droptoken.Application;
import com._98point6.droptoken.vertx.mysql.RxMySQLImpl;
import com._98point6.droptoken.vertx.mysql.api.RxMySQL;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        MySQLIntegrationBase.class, Application.class
})
@ActiveProfiles("test")
public class MySQLIntegrationBase {
    
    private static final int PORT = 3306;
    private static final String HOST = "localhost";
    private static final String DATABASE = "droptoken";
    private static final String USER = "appuser";
    private static final String PASSWORD = "appuser";
    
    @Autowired
    protected DataSource dataSource;
    
    @Value("classpath:schema.sql")
    private Resource schema;
    
    @PostConstruct
    public void init() throws SQLException {
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, schema);
    }

    @Bean
    public DataSource dataSource() {
        String url = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        return new DriverManagerDataSource(url, USER, PASSWORD);
    }

    @Bean
    public MySQLPool mySQLPool() {

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(3306)
                .setHost(HOST)
                .setDatabase(DATABASE)
                .setUser(USER)
                .setPassword(PASSWORD);

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        return MySQLPool.pool(connectOptions, poolOptions);
    }
    
    @Bean
    public RxMySQL rxMySQL(MySQLPool mySQLPool) {
        return new RxMySQLImpl(mySQLPool);
    }

    @Bean
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(@Value("classpath:application.yml") Resource configFile) throws IOException {
        YamlPropertySourceLoader propertySourceLoader = new YamlPropertySourceLoader();

        List<PropertySource<?>> propertySources = propertySourceLoader.load("application", configFile);

        MutablePropertySources mps = new MutablePropertySources();
        propertySources.forEach(mps::addFirst);

        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setPropertySources(mps);

        return c;
    }
    
}
