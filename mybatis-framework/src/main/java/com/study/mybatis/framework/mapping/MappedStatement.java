package com.study.mybatis.framework.mapping;

import com.study.mybatis.framework.enums.SQLType;
import com.study.mybatis.framework.session.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述一个mapper方法的类
 */
public class MappedStatement implements Cloneable{

    private Configuration configuration;

    // mapper xml 文件对应接口方法id, 与mapper 接口方法一致
    private String id;

    // sql 类型， SELECT 、UPDATE、DELETE等等
    private SQLType sqlType;

    //一条查询语句
    private String sql;
    private String[] params;

    // mapper 接口类
    private Class<?> mapperInterface;

    private ResultMap resultMap;

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

    public ResultMap getResultMap() {
        return resultMap;
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

        public Builder(Configuration configuration, String id, SQLType sqlType, String sql, ResultMap resultMap){
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlType = sqlType;
            List<String> paramList = resolveParams(sql);
            mappedStatement.params = new String[paramList.size()];
            String parseSql = sql;

            for (int i = 0; i < paramList.size(); i++){
                String param = paramList.get(i);
                parseSql = parseSql.replaceAll("#\\{"+param+"\\}", "?");
                mappedStatement.params[i] = param;
            }

            mappedStatement.sql = parseSql;

            mappedStatement.resultMap = resultMap;
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
                    String parameter = matcher.group(i);
                    parameter = parameter.substring(2);
                    parameter = parameter.substring(0, parameter.length() - 1);
                    paramList.add(parameter);
                }
            }

            return paramList;
        }
    }

    public Integer getTimeout(){
        return 10;
    }
}
