package com.example.testdataservice.config;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
public class EnvironmentRoutingDataSource extends AbstractRoutingDataSource {
    @Override protected Object determineCurrentLookupKey() { return EnvironmentContext.get(); }
}
