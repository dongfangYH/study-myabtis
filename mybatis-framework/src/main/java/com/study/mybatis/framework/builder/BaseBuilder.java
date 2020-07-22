package com.study.mybatis.framework.builder;

import com.study.mybatis.framework.session.Configuration;

public abstract class BaseBuilder {

    private final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }


    public Configuration getConfiguration() {
        return configuration;
    }
}
