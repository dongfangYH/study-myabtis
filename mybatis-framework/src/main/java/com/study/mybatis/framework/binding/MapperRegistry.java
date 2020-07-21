package com.study.mybatis.framework.binding;

import com.study.mybatis.framework.session.Configuration;

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
}
