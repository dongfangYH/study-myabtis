package com.study.mybatis.framework.binding;

import com.study.mybatis.framework.enums.SQLType;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.session.Configuration;
import com.study.mybatis.framework.session.SqlSession;

import java.lang.reflect.Method;
import java.util.Collection;

public class MapperMethod {

    private final SqlCommand sqlCommand;
    private final MethodSignature methodSignature;

    public MapperMethod(Configuration configuration, Method invokeMethod, Class<?> mapperInterface) {
        sqlCommand = new SqlCommand(configuration, invokeMethod, mapperInterface);
        methodSignature = new MethodSignature(configuration, invokeMethod, mapperInterface);
    }

    public Object execute(SqlSession sqlSession, Object[] args){
        Object result = null;

        switch (sqlCommand.sqlType){
            case SELECT: {
                result = execute(sqlSession, args, methodSignature);
                break;
            }
            default:
                throw new RuntimeException("unknown sql type.");
        }

        return result;
    }

    private Object execute(SqlSession sqlSession, Object[] args, MethodSignature method){

        Class<?> returnType = method.getReturnType();

        return sqlSession.selectList(sqlCommand.getName(), args);

    }

    public static class MethodSignature{

        private Class<?> returnType;

        public Class<?> getReturnType() {
            return returnType;
        }

        public MethodSignature(Configuration configuration, Method method, Class<?> mapperInterface){
            returnType = method.getReturnType();
        }
    }

    public static class SqlCommand {
        private final String name;
        private final SQLType sqlType;

        public SqlCommand(Configuration configuration, Method method, Class<?> mapperInterface) {

            MappedStatement ms = resolveMappedStatement(configuration, method.getName(),
                    method.getDeclaringClass(), mapperInterface);

            name = ms.getSql();
            sqlType = ms.getSqlType();
        }

        private MappedStatement resolveMappedStatement(Configuration configuration, String methodName,
                                                       Class<?> declaringClass, Class<?> mapperInterface) {
            String statementId = mapperInterface.getName() + "."+ methodName;
            if (configuration.hasStatement(statementId)){
                return configuration.getMappedStatement(statementId);
            }
            return null;
        }

        public String getName() {
            return name;
        }
    }
}
