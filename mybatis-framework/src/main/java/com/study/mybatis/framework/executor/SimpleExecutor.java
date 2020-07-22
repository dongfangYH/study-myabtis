package com.study.mybatis.framework.executor;

import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.statement.StatementHandler;
import com.study.mybatis.framework.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleExecutor extends BaseExecutor{

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    /**
     * 真正执行sql的地方
     * @param ms
     * @param parameter
     * @param <E>
     * @return
     * @throws SQLException
     */
    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();

            //PreparedStatementHandler
            StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter);
            stmt = prepareStatement(handler);
            return handler.query(stmt, null);
        } finally {
            closeStatement(stmt);
        }
    }

    private Statement prepareStatement(StatementHandler handler) throws SQLException {
        Statement stmt;
        Connection connection = getConnection();
        stmt = handler.prepare(connection, transaction.getTimeout());
        handler.parameterize(stmt);
        return stmt;
    }

    protected Connection getConnection() throws SQLException {
        Connection connection = transaction.getConnection();
        return connection;
    }
}
