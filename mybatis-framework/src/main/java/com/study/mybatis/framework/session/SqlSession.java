package com.study.mybatis.framework.session;

public interface SqlSession {

    // 返回一个mapper
    <T> T getMapper(Class<T> type);

    Configuration getConfiguration();
}
