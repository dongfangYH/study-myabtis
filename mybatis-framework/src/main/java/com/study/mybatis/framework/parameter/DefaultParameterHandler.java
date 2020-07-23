package com.study.mybatis.framework.parameter;

import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultParameterHandler implements ParameterHandler{

    private final MappedStatement mappedStatement;
    private final Object[] parameterObjects;
    private final Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object[] parameterObjects, Configuration configuration) {
        this.mappedStatement = mappedStatement;
        this.parameterObjects = parameterObjects;
        this.configuration = configuration;
    }

    @Override
    public Object[] getParameterObjects() {
        return parameterObjects;
    }

    /**
     * 给preparedStatement 绑定参数
     * @param ps
     * @throws SQLException
     */
    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
       String[] parameterNames = mappedStatement.getParams();

       for (int i = 0; i < parameterNames.length; i++){
          Object arg = parameterObjects[i];
          if (arg instanceof String){
              ps.setString(i+1, arg.toString());
          } else if (arg instanceof Integer){
              ps.setInt(i+1, (Integer)arg);
          } else if (arg instanceof Long){
              ps.setLong(i+1, (Long)arg);
          } else {
              throw new RuntimeException("can not determine parameter type -> " + arg);
          }
       }
    }
}
