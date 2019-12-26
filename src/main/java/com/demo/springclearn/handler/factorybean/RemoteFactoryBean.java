package com.demo.springclearn.handler.factorybean;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RemoteFactoryBean<T> implements FactoryBean<T> {

    private Class<T> interfaceClass;

    public RemoteFactoryBean(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public T getObject() throws Exception {
        T proxyInstance = (T) Proxy.newProxyInstance(RemoteFactoryBean.class.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName() + ":RemoteFactoryBean");
                return null;
            }
        });
        return proxyInstance;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }
}