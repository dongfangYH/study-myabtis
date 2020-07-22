package com.study.mybatis.framework.mapping;

import com.study.mybatis.framework.session.Configuration;

import java.util.List;

public class ResultMap {

    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappings;

    ResultMap() {
    }

    @Override
    protected ResultMap clone(){
        try {
            return (ResultMap)super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }

    public static class Builder{
        private ResultMap resultMap = new ResultMap();

        public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings){
            resultMap.id = id;
            resultMap.type = type;
            resultMap.resultMappings = resultMappings;
        }

        public ResultMap build(){
           return resultMap.clone();
        }
    }
}
