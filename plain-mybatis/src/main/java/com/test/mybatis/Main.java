package com.test.mybatis;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception{


        URL classpath = ClassLoader.getSystemResource("");
        String rootPath = classpath.getPath();

        URL mapperXmlUrl = ClassLoader.getSystemResource("com/test/mybatis/mapper");
        File mapperDir = new File(mapperXmlUrl.getFile());
        File[] xmls = mapperDir.listFiles(pathname -> pathname.getName().endsWith(".class"));

        File classFile = xmls[0];

        String path = classFile.getPath();

        String mapperClass = path.substring(rootPath.length());
        mapperClass = mapperClass.substring(0, mapperClass.length() - ".class".length());
        System.out.println(mapperClass);

    }

    private static class IgnoreDTDEntityResolver implements EntityResolver{
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
        }
    }
}
