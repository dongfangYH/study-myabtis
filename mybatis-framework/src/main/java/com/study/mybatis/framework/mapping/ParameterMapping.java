package com.study.mybatis.framework.mapping;

import com.study.mybatis.framework.session.Configuration;

public class ParameterMapping {

    private Configuration configuration;

    private String property;

    public static class Builder{
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property){

        }
    }

}
