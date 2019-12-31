package com.demo.springclearn.handler;

import com.demo.springclearn.handler.service.ServiceA;
import com.demo.springclearn.handler.service.ServiceB;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HandlerTest {

    private static final String CONTEXT_HANDLER_PATH = "context-handler.xml";

    @Test
    public void customBeanDefinitionRegistryTest() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(CONTEXT_HANDLER_PATH);
        ServiceA beanA = ctx.getBean(ServiceA.class);
        beanA.methodA();
        ServiceB beanB = ctx.getBean(ServiceB.class);
        beanB.methodB();
        ctx.close();
    }
}