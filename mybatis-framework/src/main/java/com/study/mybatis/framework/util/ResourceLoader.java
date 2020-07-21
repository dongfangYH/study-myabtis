package com.study.mybatis.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceLoader {

    public static final Properties load(String path){
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return properties;
    }
}
