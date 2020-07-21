package com.study.mybatis.framework.executor;

import com.study.mybatis.framework.session.Configuration;

public class BaseExecutor implements Executor{

    private Configuration configuration;

    public BaseExecutor(Configuration configuration) {
        this.configuration = configuration;
    }
}
