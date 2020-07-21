package com.study.mybatis.framework.session;

import com.study.mybatis.framework.binding.MapperRegistry;
import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.executor.SimpleExecutor;
import com.study.mybatis.framework.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private String mapperLocation;
    private String driver;
    private String url;
    private String username;
    private String password;

    private boolean parse = false;

    //保存注册的mapper
    private final MapperRegistry mapperRegistry = new MapperRegistry(this);
    private final Map<String, MappedStatement> mappedStatements = new HashMap<>();


    public boolean hasStatement(String statementId){
        return mappedStatements.containsKey(statementId);
    }

    public MappedStatement getMappedStatement(String statementId){
        return mappedStatements.get(statementId);
    }

    public Executor newExecutor(){
        Executor executor = new SimpleExecutor(this);
        return executor;
    }

    public void setMapperLocation(String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    public Configuration parse(){
        if (parse){
            throw new RuntimeException("configuration has been parse.");
        }
        parse = true;

        return this;
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
