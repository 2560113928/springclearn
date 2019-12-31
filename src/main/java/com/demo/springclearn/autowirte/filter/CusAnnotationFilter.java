package com.demo.springclearn.autowirte.filter;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class CusAnnotationFilter extends CommonFilter implements TypeFilter {

    private Class<? extends Annotation> cls;

    public CusAnnotationFilter(Class<? extends Annotation> cls) {
        this.cls = cls;
    }

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        AnnotationMetadata annotations = metadataReader.getAnnotationMetadata();
        if (annotations.hasAnnotation(cls.getName())) {
            return true;
        }
        return false;
    }
}