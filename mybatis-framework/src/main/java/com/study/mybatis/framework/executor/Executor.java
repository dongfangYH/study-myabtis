package com.study.mybatis.framework.executor;

import com.study.mybatis.framework.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <E> List<E> query(MappedStatement ms, Object[] parameters) throws SQLException;
}
