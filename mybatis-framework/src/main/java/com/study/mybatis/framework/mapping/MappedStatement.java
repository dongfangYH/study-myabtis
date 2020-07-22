package com.study.mybatis.framework.mapping;

import com.study.mybatis.framework.enums.SQLType;
import com.study.mybatis.framework.session.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappedStatement implements Cloneable{

    private Configuration configuration;

    // mapper xml 文件对应接口方法id, 与mapper 接口方法一致
    private String id;
    private SQLType sqlType;
    private String sql;
    private String[] params;

    MappedStatement() {
    }

    public String[] getParams() {
        return params;
    }

    public String getId() {
        return id;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getSql() {
        return sql;
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
            List<String> paramList = resolveParams(sql);
            mappedStatement.params = new String[paramList.size()];
            String parseSql = sql;

            for (int i = 0; i < paramList.size(); i++){
                String param = paramList.get(i);
                parseSql = parseSql.replaceAll(param, "?");
                mappedStatement.params[i] = param.substring(0, param.length() - 1).substring(2);
            }

            mappedStatement.sql = parseSql;
        }

        public MappedStatement build(){
            return mappedStatement.clone();
        }

        private List<String> resolveParams(String sql) {
            String regex = "#\\{[a-zA-Z]+\\}";
            // 简单将 #{parameter} 替换为 ？
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(sql);
            List<String> paramList = new ArrayList<>();
            while (matcher.find()){
                int count = matcher.groupCount();
                for (int i = 0; i <= count; i++){
                    paramList.add(matcher.group(i));
                }
            }

            return paramList;
        }
    }

    public Integer getTimeout(){
        return 10;
    }
}
