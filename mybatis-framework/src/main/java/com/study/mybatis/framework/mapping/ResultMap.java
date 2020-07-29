package com.study.mybatis.framework.mapping;

import com.study.mybatis.framework.enums.CollectionType;
import com.study.mybatis.framework.session.Configuration;

import java.util.ArrayList;

public class ResultMap implements Cloneable{

    private String id;
    private Class<?> type;
    private CollectionType collectionType;
    private ArrayList<ResultMapping> resultMappings;

    public String getId() {
        return id;
    }

    public Class<?> getType() {
        return type;
    }

    public ArrayList<ResultMapping> getResultMappings() {
        return resultMappings;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    ResultMap() {
    }

    @Override
    protected ResultMap clone(){
        try {
            return (ResultMap)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder{
        private ResultMap resultMap = new ResultMap();

        public Builder(Configuration configuration, String id, Class<?> type, ArrayList<ResultMapping> resultMappings){
            resultMap.id = id;
            resultMap.type = type;
            resultMap.resultMappings = resultMappings;
        }

        public ResultMap build(){
            ResultMap clone = resultMap.clone();
            clone.resultMappings = (ArrayList<ResultMapping>) resultMap.resultMappings.clone();
            return clone;
        }
    }
}
