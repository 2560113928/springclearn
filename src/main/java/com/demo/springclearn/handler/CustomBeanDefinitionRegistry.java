package com.demo.springclearn.handler;

import com.demo.springclearn.handler.factorybean.RemoteFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

public class CustomBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, EnvironmentAware {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        try {
            if (StringUtils.isEmpty(packageName)) {
                packageName = DEFAULT_PACKAGE_NAME;
            }
            List<Class<?>> classes = scanPackages(packageName);
            for (Class<?> beanClass : classes) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
                AbstractBeanDefinition beanDefinition = builder.getRawBeanDefinition();
                beanDefinition.setBeanClass(RemoteFactoryBean.class);
                beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClass);
                beanDefinitionRegistry.registerBeanDefinition(beanClass.getSimpleName(), beanDefinition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public CustomBeanDefinitionRegistry setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
        return this;
    }

    public CustomBeanDefinitionRegistry setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    private List<Class<?>> scanPackages(String basePackage) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new LinkedList<>();
        String packageSearchPath = generalPackagePath(basePackage);
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
            Class<?> clzz = Class.forName(metadataReader.getClassMetadata().getClassName());
            if (clzz.isAnnotationPresent(annotationClass)) {
                classes.add(clzz);
            }
        }
        return classes;
    }

    private String generalPackagePath(String basePackage) {
        return  ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + '/' + RESOURCE_PATTERN;
    }

    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(basePackage));
    }

    private static final String DEFAULT_PACKAGE_NAME = "";
    private static final String RESOURCE_PATTERN = "**/*.class";
    private ResourcePatternResolver resourcePatternResolver;
    private MetadataReaderFactory metadataReaderFactory;
    private Environment environment;
    private Class<? extends Annotation> annotationClass;
    private String packageName;
}