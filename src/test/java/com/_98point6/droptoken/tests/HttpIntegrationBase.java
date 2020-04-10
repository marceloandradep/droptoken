package com._98point6.droptoken.tests;

import com._98point6.droptoken.Application;
import com._98point6.droptoken.vertx.http.HttpVerticle;
import io.vertx.reactivex.core.Vertx;
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
        HttpIntegrationBase.class, Application.class
})
@ActiveProfiles("test")
public class HttpIntegrationBase {
    
    @Value("http://localhost:${http.port}")
    public String baseUrl;
    
    @Autowired
    private Vertx vertx;
    
    @PostConstruct
    public void init() {
        vertx
                .rxDeployVerticle(springify(HttpVerticle.class.getName()))
                .blockingGet();
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
    
}
