package com.study.mybatis.framework.builder;

import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.Configuration;

public class MapperBuilderAssistant extends BaseBuilder{

    private final String namespace;

    public MapperBuilderAssistant(Configuration configuration, String namespace) {
        super(configuration);
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public void addMapper(Class<?> superType) {
        getConfiguration().addMapper(superType);
    }

    public void addMappedStatement(MappedStatement ms) {
        getConfiguration().addMappedStatement(ms);
    }
}
