package io.my.mybatis.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.my.mybatis.annotation.table.RepositoryMaker;
import io.my.mybatis.util.NamingStrategy;
import io.my.mybatis.util.RepositoryUtil;


public class RepositoryGenerator {
    private static Logger logger = LoggerFactory.getLogger(RepositoryGenerator.class);
    private static final String REPOSITORY = "Repository";

    private RepositoryGenerator() {
        throw new IllegalAccessError();
    }
    
    public static void generateCode(Element annotationElement, String packageName, Filer filer) throws IOException,
            ClassNotFoundException {

        RepositoryMaker repositoryMaker = annotationElement.getAnnotation(RepositoryMaker.class);
        String repositoryName = annotationElement.getSimpleName().toString() + REPOSITORY;

        TypeElement typeElement = (TypeElement) annotationElement;

        String tableName = repositoryMaker.table().equals("") ? 
                    NamingStrategy.camelToSnake(annotationElement.getSimpleName().toString()) : 
                    repositoryMaker.table()
        ;

        // Generate items map
        Builder builder = TypeSpec.interfaceBuilder(repositoryName);
        
        // Get Class Fields
        List<Element> fieldElementList = RepositoryUtil.getFieldList(typeElement);

        // Declare MethodSpec List
        List<MethodSpec> methodList = new ArrayList<>();

        // Generate Select Methods
        methodList.addAll(MethodGenerator.generateSelectList(annotationElement, fieldElementList, Object.class, tableName));

        // Generate Insert Method
        methodList.add(MethodGenerator.generateInsert(typeElement, fieldElementList, tableName));
        
        // Generate Update Method
        methodList.addAll(MethodGenerator.generateUpdateList(typeElement, fieldElementList, tableName));

        // Generate Delete Method
        methodList.addAll(MethodGenerator.generateDeleteList(fieldElementList, tableName));

        // Add all Methods in Builder
        methodList.forEach(builder::addMethod);

        builder.addAnnotation(Mapper.class)
                .addModifiers(Modifier.PUBLIC);
        
        // Write file
        JavaFile javaFile = JavaFile.builder(packageName, builder.build()).build();
        javaFile.writeTo(filer);
    }


}
