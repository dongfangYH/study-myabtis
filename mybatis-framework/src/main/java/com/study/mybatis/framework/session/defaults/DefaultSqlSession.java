package com.study.mybatis.framework.session.defaults;

import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.session.SqlSession;

import java.sql.SQLException;
import java.util.List;

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

    @Override
    public <E> List<E> selectList(String statement) {
        return this.selectList(statement, null);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        MappedStatement ms = configuration.getMappedStatement(statement);
        try {
            return executor.query(ms, parameter);
        } catch (SQLException se) {
            throw new RuntimeException(se);
        }
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {

        List<T> list = this.selectList(statement, parameter);
        if (list.size() == 1){
            return list.get(0);
        }else if (list.size() > 1){
            throw new RuntimeException("too many result.");
        }
        return null;
    }
}
