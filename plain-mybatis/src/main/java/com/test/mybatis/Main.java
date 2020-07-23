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


        //************** 第一阶段 **************
        // 第一步，读取mybaits-config.xml配置文件
        Properties config = ResourceLoader.load("config.properties");

        // 第二步，构建SqlSessionFactory

        // DefaultSqlSessionFacotroy
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);

        //************** 第二阶段 **************
        // 第三步，打开SqlSession
        // DefaultSqlSession
        SqlSession session = sqlSessionFactory.openSession();

        // 第四步，获取Mapper接口对象
        UserMapper userMapper = session.getMapper(UserMapper.class);

        //************** 第三阶段 **************
        // 第五步，调用Mapper接口对象的方法操作数据库
        List<User> users = userMapper.selectUsersByPage(new HashMap<>());

        //List<User> users = session.selectList("com.study.mybatis.mapper.UserMapper.selectUsersByArray");

        // 第六步，业务处理
        System.out.println(users.size());

    }

}
