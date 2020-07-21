package com.study.mybatis.framework.session.defaults;

import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.session.SqlSession;
import com.study.mybatis.framework.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        final Executor executor = configuration.newExecutor();
        return new DefaultSqlSession(configuration, executor);
    }

}
