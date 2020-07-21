package com.test.mybatis;

import com.study.mybatis.framework.session.SqlSession;
import com.study.mybatis.framework.session.SqlSessionFactory;
import com.study.mybatis.framework.session.SqlSessionFactoryBuilder;
import com.study.mybatis.framework.util.ResourceLoader;
import com.test.mybatis.entity.User;
import com.test.mybatis.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception{
        Properties config = ResourceLoader.load("config.properties");
        SqlSessionFactory sqlSessionFactory =  new SqlSessionFactoryBuilder().build(config);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.selectUsersByPage(new HashMap<String, Object>());
        System.out.println(users);
    }
}
