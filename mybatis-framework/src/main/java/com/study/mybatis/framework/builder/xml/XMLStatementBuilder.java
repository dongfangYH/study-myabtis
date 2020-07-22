package com.study.mybatis.framework.builder.xml;

import com.study.mybatis.framework.builder.BaseBuilder;
import com.study.mybatis.framework.builder.MapperBuilderAssistant;
import com.study.mybatis.framework.enums.SQLType;
import com.study.mybatis.framework.mapping.MappedStatement;
import com.study.mybatis.framework.parsing.XNode;
import com.study.mybatis.framework.session.Configuration;


/**
 * 根据mapper xml 文件构建 MappedStatement
 *
 * StatementId = mapper interface + . + methodName
 */
public class XMLStatementBuilder extends BaseBuilder {

    private final XNode xNode;
    private final MapperBuilderAssistant assistant;
    private final Class<?> mapperInterface;

    public XMLStatementBuilder(Configuration configuration, Class<?> mapperInterface, XNode xNode,
                               MapperBuilderAssistant assistant) {
        super(configuration);
        this.mapperInterface = mapperInterface;
        this.xNode = xNode;
        this.assistant = assistant;
    }

    /**
     * 解析statement node ，相当于 mapper xml 的一条查询语句
     */
    public void parseStatementNode(){

        String id = mapperInterface.getName() + "." + xNode.getName();
        String sql = xNode.getSql();
        MappedStatement ms = new MappedStatement.Builder(getConfiguration(), id, SQLType.SELECT, sql).build();
        assistant.addMappedStatement(ms);
        assistant.addMapper(mapperInterface);

    }

}
