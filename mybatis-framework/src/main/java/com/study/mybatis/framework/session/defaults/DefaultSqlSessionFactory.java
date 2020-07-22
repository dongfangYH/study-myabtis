package com.study.mybatis.framework.session.defaults;

import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.session.SqlSession;
import com.study.mybatis.framework.session.SqlSessionFactory;
import com.study.mybatis.framework.transaction.Transaction;
import com.study.mybatis.framework.transaction.jdbc.JdbcTransaction;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        Transaction tx = new JdbcTransaction(configuration.getConnection());
        final Executor executor = configuration.newExecutor(tx);
        return new DefaultSqlSession(configuration, executor);
    }

}
