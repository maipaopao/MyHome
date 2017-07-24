package com.happiness.core.config;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableSpringDataWebSupport
@EnableMongoRepositories(basePackages = "com.happiness.db.mapper.repository", repositoryImplementationPostfix = "Impl",
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongo.host}")
    private String mongoHost;
    @Value("${mongo.port}")
    private int mongoPort;
    @Value("${mongo.database}")
    private String mongoDataBase;

    @Bean
    public MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient(mongoHost, mongoPort);
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(mongoClient(), mongoDataBase);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        return new MongoTemplate(mongoDbFactory());
    }

    @Override
    protected String getDatabaseName() {

        return mongoDataBase;
    }

    @Override
    protected String getMappingBasePackage() {

        return "com.tsingda.am.model.domain";
    }

    @Override
    public Mongo mongo() throws Exception {

        return new MongoClient(mongoHost, mongoPort);
    }

    // the following are optional

    @Bean
    @Override
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        return new CustomConversions(converterList);
    }

    @Bean
    public LoggingEventListener mappingEventsListener() {
        return new LoggingEventListener();
    }
    
    @Bean
    public MongoTypeMapper customTypeMapper() {
        return new DefaultMongoTypeMapper();
    }
    

}
