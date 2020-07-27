package com.study.mybatis.framework.session;

import com.study.mybatis.framework.binding.MapperRegistry;
import com.study.mybatis.framework.builder.MapperBuilderAssistant;
import com.study.mybatis.framework.builder.xml.XMLStatementBuilder;
import com.study.mybatis.framework.enums.CollectionType;
import com.study.mybatis.framework.enums.SQLType;
import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.executor.SimpleExecutor;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.mapping.ResultMap;
import com.study.mybatis.framework.mapping.ResultMapping;
import com.study.mybatis.framework.parameter.DefaultParameterHandler;
import com.study.mybatis.framework.parameter.ParameterHandler;
import com.study.mybatis.framework.parsing.XNode;
import com.study.mybatis.framework.resultset.DefaultResultSetHandler;
import com.study.mybatis.framework.resultset.ResultSetHandler;
import com.study.mybatis.framework.statement.RoutingStatementHandler;
import com.study.mybatis.framework.statement.StatementHandler;
import com.study.mybatis.framework.transaction.Transaction;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    private String mapperLocation;
    private String driver;
    private String url;
    private String username;
    private String password;

    private boolean parse = false;
    private Connection connection;

    public void initConnection(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    //保存注册的mapper
    private final MapperRegistry mapperRegistry = new MapperRegistry(this);
    private final Map<String, MappedStatement> mappedStatements = new HashMap<>();


    public boolean hasStatement(String statementId){
        return mappedStatements.containsKey(statementId);
    }

    public MappedStatement getMappedStatement(String statementId){
        return mappedStatements.get(statementId);
    }

    public Executor newExecutor(Transaction tx){
        Executor executor = new SimpleExecutor(this, tx);
        return executor;
    }

    public void setMapperLocation(String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    public Configuration parse(){
        if (parse){
            throw new RuntimeException("configuration has been parse.");
        }
        parse = true;

        // 解析mapper xml 文件
        List<MapperFile> mapperFileList = resolveMapperFiles(mapperLocation);

        for (MapperFile mapperFile : mapperFileList){

            File mapperInterface = mapperFile.getMapperClass();
            File mapperXML = mapperFile.getMapperXml();

            String rootPath = ClassLoader.getSystemClassLoader().getResource("").getPath();

            String mapperClassName = mapperInterface.getPath().substring(rootPath.length());
            mapperClassName = mapperClassName.substring(0, mapperClassName.length() - ".class".length());
            mapperClassName = mapperClassName.replaceAll("/", "\\.");

            //dom4j parse mapper xml
            SAXReader reader = new SAXReader(false);
            reader.setEntityResolver((publicId, systemId) -> new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes())));
            try {

                Class<?> mapperClass = Class.forName(mapperClassName);

                Method[] methods = mapperClass.getDeclaredMethods();

                Map<String, List<Method>> methodMaps = new HashMap<>();

                for (Method method : methods){
                    String methodName = method.getName();
                    if (methodMaps.containsKey(methodName)){
                        throw new RuntimeException("cannot determine exact method.");
                    }else {
                        List<Method> methodList = new ArrayList<>();
                        methodList.add(method);
                        methodMaps.put(methodName, methodList);
                    }
                }

                // 不做xml文档校验
                Document document = reader.read(mapperXML);
                Element root = document.getRootElement();

                String namespace = root.attributeValue("namespace");
                MapperBuilderAssistant assistant = new MapperBuilderAssistant(this, namespace);

                // 先只管select 和resultMap
                List<Element> selectStatement = root.elements("select");
                List<Element> resultMapStatement = root.elements("resultMap");


                if (null == resultMapStatement || resultMapStatement.size() == 0){
                    throw new RuntimeException("no resultMap definition.");
                }

                Map<String, ResultMap> resultMapMap = new HashMap<>();


                // 解析mapper xml文件 result map
                for (Element element : resultMapStatement){
                    String id = element.attributeValue("id");
                    String type = element.attributeValue("type");

                    List<Element> idColumn = element.elements("id");
                    List<Element> columns = element.elements("result");

                    ArrayList<ResultMapping> resultMappingList = new ArrayList<>();

                    for (Element idElement : idColumn){
                        String column = idElement.attributeValue("column");
                        String property = idElement.attributeValue("property");
                        ResultMapping resultMapping = new ResultMapping.Builder(this,
                                true, column, property).build();
                        resultMappingList.add(resultMapping);
                    }

                    for (Element columnElement : columns){
                        String column = columnElement.attributeValue("column");
                        String property = columnElement.attributeValue("property");
                        ResultMapping resultMapping = new ResultMapping.Builder(this,
                                false, column, property).build();
                        resultMappingList.add(resultMapping);
                    }

                    Class<?> clazz = Class.forName(type);
                    ResultMap resultMap = new ResultMap.Builder(this, id, clazz, resultMappingList).build();
                    resultMapMap.put(id, resultMap);
                }


                if (null != selectStatement){
                    for (Element element : selectStatement){
                        String id = element.attributeValue("id");
                        String parameterType = element.attributeValue("parameterType");
                        String resultMap = element.attributeValue("resultMap");
                        String sql = element.getTextTrim();



                        Method method = methodMaps.get(id).get(0);
                        Class<?> returnType = method.getReturnType();
                        //TODO

                        XNode xNode = new XNode(id, resultMapMap.get(resultMap) ,parameterType, sql, SQLType.SELECT, CollectionType.LIST);
                        new XMLStatementBuilder(this, mapperClass, xNode, assistant).parseStatementNode();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return this;
    }

    private List<MapperFile> resolveMapperFiles(String mapperLocation) {

        final List<MapperFile> result = new ArrayList<>();

        URL mapperUrl = ClassLoader.getSystemResource(mapperLocation.replaceAll("\\.", "/"));
        File mapperDir = new File(mapperUrl.getFile());
        File[] mapperFiles = mapperDir.listFiles(file -> {
            if (file.isDirectory()) return false;
            String fileName = file.getName();
            return fileName.endsWith(".class") || fileName.endsWith(".xml");
        });

        Map<String, List<File>> fileMap = new HashMap<>();

        for (File file : mapperFiles){
            String[] filePieces = file.getName().split("/");
            String fileName = filePieces[filePieces.length - 1];
            String prefix = fileName.substring(0, fileName.lastIndexOf("."));
            if (fileMap.containsKey(prefix)){
                fileMap.get(prefix).add(file);
            }else {
                List<File> fileList = new ArrayList<>();
                fileList.add(file);
                fileMap.put(prefix, fileList);
            }
        }

        fileMap.forEach((s, files) -> {
            if (files.size() == 2){

                File mapperClass;
                File mapperXml;

                if (files.get(0).getName().endsWith(".class")){
                    mapperClass = files.get(0);
                    mapperXml = files.get(1);
                }else {
                    mapperClass = files.get(1);
                    mapperXml = files.get(0);
                }
                result.add(new MapperFile(mapperClass, mapperXml));
            }
        });

        return result;
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * mapper 文件
     */
    private static final class MapperFile{
        private final File mapperClass;
        private final File mapperXml;

        public MapperFile(File mapperClass, File mapperXml) {
            this.mapperClass = mapperClass;
            this.mapperXml = mapperXml;
        }

        public File getMapperClass() {
            return mapperClass;
        }

        public File getMapperXml() {
            return mapperXml;
        }
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }


    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object[] parameterObjects) {
        StatementHandler statementHandler = new RoutingStatementHandler(mappedStatement, parameterObjects, executor);
        return statementHandler;
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object[] parameterObjects) {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObjects, this);
        return parameterHandler;
    }

    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement,  ParameterHandler parameterHandler,
                                                ResultHandler resultHandler) {
        ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler);
        return resultSetHandler;
    }

    public Integer getDefaultStatementTimeout(){
        return 10;
    }

    public Connection getConnection(){
        return connection;
    }
}
