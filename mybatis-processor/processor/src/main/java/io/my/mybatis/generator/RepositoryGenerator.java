package io.my.mybatis.generator;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import org.apache.ibatis.annotations.Mapper;

import io.my.mybatis.annotation.RepositoryMaker;
import io.my.mybatis.annotation.RepositoryMakers;
import io.my.mybatis.util.RepositoryUtil;

public class RepositoryGenerator {

    private static final String REPOSITORY = "Repository";

    private RepositoryGenerator() {
        throw new IllegalAccessError();
    }
    
    public static void generateCode(Element annotationElement, String packageName, Filer filer) throws IOException,
            ClassNotFoundException {
        RepositoryMakers repositoryMakers = annotationElement.getAnnotation(RepositoryMakers.class);
        String repositoryName = annotationElement.getSimpleName().toString() + REPOSITORY;

        // Generate items map
        Builder builder = TypeSpec.interfaceBuilder(repositoryName);

        for (RepositoryMaker repository : repositoryMakers.value()) {
            Class<?> returnType = RepositoryUtil.getReturnType(repository);
            MethodSpec.Builder method = 
                    MethodSpec.methodBuilder(repository.id())
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .returns(returnType);
            List<Class<?>> paramList = RepositoryUtil.getParams(repository);

            for (int i = 0; i < repository.paramNames().length; i++) {
                method.addParameter(paramList.get(i), repository.paramNames()[i]);
            }

            builder.addMethod(method.build());
        }
        
        builder.addAnnotation(Mapper.class)
                .addModifiers(Modifier.PUBLIC);

        // Write file
        JavaFile javaFile = JavaFile.builder(packageName, builder.build()).build();
        javaFile.writeTo(filer);
    }

}
