package com.study.mybatis.framework.parameter;

import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultParameterHandler implements ParameterHandler{

    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private final Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, Configuration configuration) {
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.configuration = configuration;
    }

    @Override
    public Object getParameterObject() {
        return parameterObject;
    }

    /**
     * 给preparedStatement 绑定参数
     * @param ps
     * @throws SQLException
     */
    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
       String[] parameterNames = mappedStatement.getParams();
    }
}
