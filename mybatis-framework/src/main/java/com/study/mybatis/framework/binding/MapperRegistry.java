package com.study.mybatis.framework.binding;

import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {

    private final Configuration configuration;

    private final Map<Class<?>, MapperProxyFactory<?>> storeMappers = new HashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 把mapper interface 注册到storeMapper 里
     * @param type
     * @param <T>
     */
    public <T> void addMapper(Class<T> type){
        if (type.isInterface()){
            storeMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) storeMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }
}
