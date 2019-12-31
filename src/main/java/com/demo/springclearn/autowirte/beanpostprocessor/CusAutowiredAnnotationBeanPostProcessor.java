package com.demo.springclearn.autowirte.beanpostprocessor;

import com.demo.springclearn.autowirte.annotation.Load;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

@Load
public class CusAutowiredAnnotationBeanPostProcessor extends AutowiredAnnotationBeanPostProcessor {

    public CusAutowiredAnnotationBeanPostProcessor(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
    }

    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
        System.out.println("postProcessProperties..."+beanName+"=>"+bean);
        return super.postProcessProperties(pvs, bean, beanName);
    }
}