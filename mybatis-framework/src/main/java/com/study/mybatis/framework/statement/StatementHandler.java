package com.study.mybatis.framework.statement;

import com.study.mybatis.framework.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {

    Statement prepare(Connection connection, Integer transactionTimeout)
            throws SQLException;

    <E> List<E> query(Statement statement, ResultHandler resultHandler)
            throws SQLException;

    void parameterize(Statement statement)
            throws SQLException;
}
