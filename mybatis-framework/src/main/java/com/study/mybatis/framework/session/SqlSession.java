package com.study.mybatis.framework.session;

import java.util.List;

public interface SqlSession {

    // 返回一个mapper
    <T> T getMapper(Class<T> type);

    Configuration getConfiguration();


    <E> List<E> selectList(String statement);

    <E> List<E> selectList(String statement, Object parameter);

    <T> T selectOne(String statement, Object parameter);


}
