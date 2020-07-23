package com.study.mybatis.framework.executor;

import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.transaction.Transaction;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseExecutor implements Executor{

    private Configuration configuration;
    protected Transaction transaction;
    protected Executor wrapper;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        wrapper = this;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object[] parameters) throws SQLException {
        return doQuery(ms, parameters);
    }

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object[] parameters)
            throws SQLException;


    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}
