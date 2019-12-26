package com.demo.springclearn.handler.filter;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class AnnotationFilter<T> implements TypeFilter {

    private Class<T> cls;

    public AnnotationFilter(Class<T> cls) {
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