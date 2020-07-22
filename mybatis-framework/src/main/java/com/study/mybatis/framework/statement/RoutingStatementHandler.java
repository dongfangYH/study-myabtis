package com.study.mybatis.framework.statement;

import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RoutingStatementHandler implements StatementHandler{

    private final StatementHandler delegate;

    public RoutingStatementHandler(MappedStatement ms, Object parameter, Executor executor) {
        delegate = new PreparedStatementHandler(executor, ms,null, parameter);
    }

    @Override
    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        return delegate.prepare(connection, transactionTimeout);
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        return delegate.query(statement, resultHandler);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        delegate.parameterize(statement);
    }
}
