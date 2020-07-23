package com.study.mybatis.framework.statement;

import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.ResultHandler;

import java.sql.*;
import java.util.List;

public class PreparedStatementHandler extends BaseStatementHandler{

    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, ResultHandler resultHandler, Object[] parameters) {
        super(executor, mappedStatement, resultHandler, parameters);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = mappedStatement.getSql();
        return connection.prepareStatement(sql);
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.handleResultSets(ps);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        parameterHandler.setParameters((PreparedStatement) statement);
    }
}
