package com.example.testdata.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.*;

@Configuration
public class DataSourceConfig {

    private final Environment springEnv;
    public DataSourceConfig(Environment springEnv) { this.springEnv = springEnv; }

    @Bean
    public DataSource routingDataSource() {
        Binder binder = Binder.get(springEnv);
        List<Map<String, String>> envs = binder.bind("app.environments", List.class).orElse(Collections.emptyList());

        EnvironmentRoutingDataSource routing = new EnvironmentRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();

        for (Map<String, String> cfg : envs) {
            String key = cfg.get("key").trim();
            String url = cfg.get("url");
            String user = cfg.get("username");
            String pass = cfg.get("password");

            HikariDataSource ds = new HikariDataSource();
            ds.setJdbcUrl(url);
            ds.setUsername(user);
            ds.setPassword(pass);
            ds.setMaximumPoolSize(Integer.parseInt(springEnv.getProperty("app.hikari.max-pool-size","3")));
            ds.setMinimumIdle(Integer.parseInt(springEnv.getProperty("app.hikari.min-idle","1")));
            ds.setConnectionTimeout(Long.parseLong(springEnv.getProperty("app.hikari.connection-timeout-ms","10000")));
            ds.setMaxLifetime(Long.parseLong(springEnv.getProperty("app.hikari.max-lifetime-ms","600000")));

            targetDataSources.put(key, ds);
        }

        routing.setTargetDataSources(targetDataSources);
        if (!targetDataSources.isEmpty()) routing.setDefaultTargetDataSource(targetDataSources.values().iterator().next());
        routing.afterPropertiesSet();
        return routing;
    }
}
