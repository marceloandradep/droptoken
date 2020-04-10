package com._98point6.droptoken.tests;

import com._98point6.droptoken.Application;
import com._98point6.droptoken.vertx.http.HttpVerticle;
import com._98point6.droptoken.vertx.mysql.RxMySQLImpl;
import com._98point6.droptoken.vertx.mysql.api.RxMySQL;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.reactivex.core.Vertx;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

import static com._98point6.droptoken.vertx.factories.SpringVerticleFactory.springify;

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        TestVerticleFactory.class, HttpIntegrationBase.class, Application.class
})
@ActiveProfiles("test")
public abstract class HttpIntegrationBase {
    private static final String HOST = "localhost";
    private static final String DATABASE = "droptoken";
    private static final String USER = "appuser";
    private static final String PASSWORD = "appuser";
    
    public String baseUrl;
    
    @Autowired
    private Vertx vertx;

    @Autowired
    private TestVerticleFactory factory;
    
    @PostConstruct
    public void init() {
        vertx
                .rxDeployVerticle(springify(HttpVerticle.class.getName()))
                .blockingGet();
        
        int port = factory.getPort();
        baseUrl = "http://localhost:" + port;
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
    RestTemplate restTemplate() {
        return new RestTemplate();
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

    @Bean
    public Vertx vertx(TestVerticleFactory factory) {
        Vertx vertx = Vertx.vertx();
        vertx.getDelegate().registerVerticleFactory(factory);
        return vertx;
    }
    
}
