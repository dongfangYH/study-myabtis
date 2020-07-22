package com.study.mybatis.framework.session;

import com.study.mybatis.framework.binding.MapperRegistry;
import com.study.mybatis.framework.builder.MapperBuilderAssistant;
import com.study.mybatis.framework.builder.xml.XMLStatementBuilder;
import com.study.mybatis.framework.enums.SQLType;
import com.study.mybatis.framework.executor.Executor;
import com.study.mybatis.framework.executor.SimpleExecutor;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.mapping.ResultMap;
import com.study.mybatis.framework.mapping.ResultMapping;
import com.study.mybatis.framework.parsing.XNode;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class Configuration {

    private String mapperLocation;
    private String driver;
    private String url;
    private String username;
    private String password;

    private boolean parse = false;

    //保存注册的mapper
    private final MapperRegistry mapperRegistry = new MapperRegistry(this);
    private final Map<String, MappedStatement> mappedStatements = new HashMap<>();


    public boolean hasStatement(String statementId){
        return mappedStatements.containsKey(statementId);
    }

    public MappedStatement getMappedStatement(String statementId){
        return mappedStatements.get(statementId);
    }

    public Executor newExecutor(){
        Executor executor = new SimpleExecutor(this);
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
            try {

                Class<?> mapperClass = Class.forName(mapperClassName);

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

                    List<ResultMapping> resultMappingList = new ArrayList<>();

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

                        XNode xNode = new XNode(id, resultMapMap.get(resultMap) ,parameterType, sql, SQLType.SELECT);
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
            String prefix = file.getName().substring(0, file.getName().lastIndexOf("\\."));
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

}
