package com.test.mybatis;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Test {

    public static void main(String[] args) throws Exception{
        String ref = "com.test.mybatis.mapper.UserMapper";
        Class<?> clazz = Class.forName(ref);

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods){

            Class<?> returnType = method.getReturnType();

            System.out.print(method.getName() + " : " + returnType);
            if (returnType == Collection.class || returnType == List.class ||
                returnType == Set.class){
                ParameterizedType type =(ParameterizedType)method.getGenericReturnType();
                Class gn = (Class) type.getActualTypeArguments()[0];
                System.out.println(" : " + gn);
            }else {
                System.out.println();
            }

        }
    }
}
