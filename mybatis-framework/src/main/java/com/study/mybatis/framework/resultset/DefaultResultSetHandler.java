package com.study.mybatis.framework.resultset;

import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.mapping.ResultMapping;
import com.study.mybatis.framework.parameter.ParameterHandler;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.session.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler{

    private final Executor executor;
    private final Configuration configuration;
    private final MappedStatement mappedStatement;
    private final ParameterHandler parameterHandler;
    private final ResultHandler<?> resultHandler;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler<?> resultHandler) {
        this.executor = executor;
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.parameterHandler = parameterHandler;
        this.resultHandler = resultHandler;
    }

    @Override
    public List<Object> handleResultSets(Statement stmt) throws SQLException {
        ResultSet rs = stmt.getResultSet();

        Class<?> returnType = mappedStatement.getResultMap().getType();
        ArrayList<ResultMapping> resultMappings = mappedStatement.getResultMap().getResultMappings();

        final List<Object> result = new ArrayList<>();

        while (rs.next()){
            Object instance = returnType.getConstructor(null).newInstance();

            for (ResultMapping rtm : resultMappings){
                rs.getString(rtm.getColumn());
            }

            result.add(instance);
        }

        return result;
    }
}
