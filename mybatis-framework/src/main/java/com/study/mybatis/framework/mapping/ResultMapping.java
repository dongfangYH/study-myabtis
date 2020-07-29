package com.study.mybatis.framework.mapping;

import com.study.mybatis.framework.session.Configuration;

public class ResultMapping implements Cloneable{

    private boolean isId;
    private String column;
    private String property;

    public boolean isId() {
        return isId;
    }

    public String getColumn() {
        return column;
    }

    public String getProperty() {
        return property;
    }

    ResultMapping(){
    }

    @Override
    protected ResultMapping clone(){
        try {
            return (ResultMapping)super.clone();
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }

    public static class Builder {
        private ResultMapping resultMapping = new ResultMapping();

        // 暂时不做typeHandler
        public Builder(Configuration configuration, Boolean isId, String column, String property){
            resultMapping.isId = isId;
            resultMapping.column = column;
            resultMapping.property = property;
        }

        public ResultMapping build(){
            return resultMapping.clone();
        }
    }

}
