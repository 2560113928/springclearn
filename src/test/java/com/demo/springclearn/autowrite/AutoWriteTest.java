package com.demo.springclearn.autowrite;

import com.demo.springclearn.autowirte.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutoWriteTest {

    private static final String CONTEXT_AUTO_WRITE_PATH = "context-auto-write.xml";

    @Test
    public void autoWriteBeanDefinitionRegistryTest() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(CONTEXT_AUTO_WRITE_PATH);
        User user = (User) ctx.getBean("user");
        System.out.println(user.getAddress());
        ctx.close();
    }
}