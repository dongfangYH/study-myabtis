package com.study.mybatis.framework.session.defaults;

import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    public <T> T getMapper(Class<T> type) {
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
