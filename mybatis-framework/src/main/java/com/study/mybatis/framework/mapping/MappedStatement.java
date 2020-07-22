package com.study.mybatis.framework.mapping;

import com.study.mybatis.framework.enums.SQLType;
import com.study.mybatis.framework.session.Configuration;

public class MappedStatement implements Cloneable{

    private Configuration configuration;

    // mapper xml 文件对应接口方法id, 与mapper 接口方法一致
    private String id;

    private SQLType sqlType;

    private String sql;

    MappedStatement() {
    }

    public String getId() {
        return id;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    @Override
    protected MappedStatement clone(){
        try {
            return (MappedStatement)super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }

    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SQLType sqlType, String sql){
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlType = sqlType;
            mappedStatement.sql = sql;
        }

        public MappedStatement build(){
            return mappedStatement.clone();
        }
    }
}
