package com.demo.springclearn.autowirte;

import com.demo.springclearn.autowirte.beanpostprocessor.CusAutowiredAnnotationBeanPostProcessor;
import com.demo.springclearn.autowirte.filter.CommonFilter;
import com.demo.springclearn.autowirte.filter.CusAnnotationFilter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

public class AutoWriteBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
        // 即使将BeanPostProcessor的beanDefinition对象加入注册中心，也是不生效的，源码中是通过getBeanPostProcessors方法去拿BeanPostProcessor集合，
        // 我们没把对象加入BeanPostProcessors中，要用下面重写的方法，通过beanFactory加入
        checkScanArguments();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(generateCusFilterType(this.scanFilterType));
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(this.basePackage);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            beanDefinition.setScope(null);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            beanDefinitionRegistry.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        // 将BeanPostProcessor对象加入BeanPostProcessors集合中，getBean时，会在方法中调用(populateBean ---> postProcessProperties)
        configurableListableBeanFactory.addBeanPostProcessor(new CusAutowiredAnnotationBeanPostProcessor(configurableListableBeanFactory));
    }

    /**
     * 检查参数
     */
    private void checkScanArguments() {
        if (StringUtils.isEmpty(this.basePackage)) {
            this.basePackage = DEFAULT_BASE_PACKAGE;
        }
        if (this.scanFilterType == null) {
            this.scanFilterType = DEFAULT_SCAN_FILTER_TYPE;
        }
    }

    /**
     * 构建扫描规则
     * @param type 规则类型
     * @return 扫描规则
     */
    private TypeFilter generateCusFilterType(Integer type) {
        TypeFilter typeFilter = null;
        if (CommonFilter.COMMON_FILTER_ANNOTATION_TYPE.equals(type)) {
            typeFilter = new CusAnnotationFilter(this.scanAnnotationClass);
        }
        return typeFilter;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public AutoWriteBeanDefinitionRegistry setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    public Integer getScanFilterType() {
        return scanFilterType;
    }

    public AutoWriteBeanDefinitionRegistry setScanFilterType(Integer scanFilterType) {
        this.scanFilterType = scanFilterType;
        return this;
    }

    public Class<? extends Annotation> getScanAnnotationClass() {
        return scanAnnotationClass;
    }

    public AutoWriteBeanDefinitionRegistry setScanAnnotationClass(Class<? extends Annotation> scanAnnotationClass) {
        this.scanAnnotationClass = scanAnnotationClass;
        return this;
    }

    /**
     * 默认的扫描基础包
     */
    private static final String DEFAULT_BASE_PACKAGE = "com.demo.springclearn";
    /**
     * 扫描基础包
     */
    private String basePackage;
    /**
     * 默认的扫描过滤器类型
     */
    private static final Integer DEFAULT_SCAN_FILTER_TYPE = CommonFilter.COMMON_FILTER_ANNOTATION_TYPE;
    /**
     * 扫描过滤器类型
     */
    private Integer scanFilterType;
    /**
     * 自定义扫描注解(只接受注解类型class)
     */
    private Class<? extends Annotation> scanAnnotationClass;
}