package com.study.mybatis.framework.session;

import com.study.mybatis.framework.session.defaults.DefaultSqlSessionFactory;

import java.util.Properties;

import static com.study.mybatis.framework.Constant.*;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Properties properties) {

        String mapperLocation = properties.getProperty(MAPPER_LOCATION);
        String driver = properties.getProperty(DB_DRIVER);
        String url = properties.getProperty(DB_URL);
        String username = properties.getProperty(DB_USERNAME);
        String password = properties.getProperty(DB_PASSWORD);

        //校验
        Configuration configuration = new Configuration();
        configuration.setMapperLocation(mapperLocation);
        configuration.setDriver(driver);
        configuration.setUrl(url);
        configuration.setUsername(username);
        configuration.setPassword(password);

        configuration.initConnection();

        configuration.parse();
        return build(configuration);
    }

    public SqlSessionFactory build(Configuration configuration){
        return new DefaultSqlSessionFactory(configuration);
    }
}
