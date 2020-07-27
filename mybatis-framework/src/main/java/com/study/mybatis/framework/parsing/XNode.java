package com.study.mybatis.framework.parsing;

import com.study.mybatis.framework.enums.CollectionType;
import com.study.mybatis.framework.enums.SQLType;
import com.study.mybatis.framework.mapping.ResultMap;

public class XNode {

    private final String name;
    private final ResultMap resultMap;
    private final String parameterType;
    private final String sql;
    private final SQLType sqlType;
    private final CollectionType collectionType;

    public XNode(String name, ResultMap resultMap, String parameterType, String sql, SQLType sqlType,
                 CollectionType collectionType) {
        this.name = name;
        this.resultMap = resultMap;
        this.parameterType = parameterType;
        this.sql = sql;
        this.sqlType = sqlType;
        this.collectionType = collectionType;
    }

    public String getName() {
        return name;
    }

    public ResultMap getResultMap() {
        return resultMap;
    }

    public String getParameterType() {
        return parameterType;
    }

    public String getSql() {
        return sql;
    }

    public SQLType getSqlType() {
        return sqlType;
    }
}
