package com.study.mybatis.framework.binding;

import com.study.mybatis.framework.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Mapper代理，实现对mapper 接口的代理
 * @param <T>
 */
public class MapperProxy<T> implements InvocationHandler {

    private final Map<Method, MapperMethodInvoker> methodCache;
    private final Class<T> mapperInterface;
    private final SqlSession sqlSession;

    public MapperProxy(Map<Method, MapperMethodInvoker> methodCache, Class<T> mapperInterface, SqlSession sqlSession) {
        this.methodCache = methodCache;
        this.mapperInterface = mapperInterface;
        this.sqlSession = sqlSession;
    }

    /**
     * InvocationHandler 代理增强，调用mapper.xxx()方法实际执行该invoke方法
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return methodCache.computeIfAbsent(method, m ->
            new PlainMethodInvoker(
                    new MapperMethod(sqlSession.getConfiguration(), method, mapperInterface)
            )
        ).invoke(proxy, method, args, sqlSession);

    }

    interface MapperMethodInvoker {
        Object invoke(Object proxy, Method method, Object[] args, SqlSession session) throws Throwable;
    }

    private static class PlainMethodInvoker implements MapperMethodInvoker{
        private final MapperMethod mapperMethod;

        public PlainMethodInvoker(MapperMethod mapperMethod) {
            this.mapperMethod = mapperMethod;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args, SqlSession session) throws Throwable {
            return mapperMethod.execute(session, args);
        }
    }
}
