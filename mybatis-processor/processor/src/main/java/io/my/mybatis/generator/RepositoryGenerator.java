package io.my.mybatis.generator;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import io.my.mybatis.annotation.Id;
import io.my.mybatis.annotation.RepositoryMaker;
import io.my.mybatis.util.RepositoryUtil;


public class RepositoryGenerator {

    private static final String REPOSITORY = "Repository";

    private RepositoryGenerator() {
        throw new IllegalAccessError();
    }
    
    public static void generateCode(Element annotationElement, String packageName, Filer filer) throws IOException,
            ClassNotFoundException {

        RepositoryMaker repositoryMaker = annotationElement.getAnnotation(RepositoryMaker.class);
        String repositoryName = annotationElement.getSimpleName().toString() + REPOSITORY;

        TypeElement typeElement = (TypeElement) annotationElement;

        String tableName = repositoryMaker.table();

        // Generate items map
        Builder builder = TypeSpec.interfaceBuilder(repositoryName);
        
        // get Class Fields
        List<Element> fieldList = RepositoryUtil.getFieldList(typeElement);

        // Generate Methods
        MethodSpec select = generateSelect(annotationElement, fieldList, tableName);
        
        // add Method in Builder
        builder.addMethod(select);
        builder.addAnnotation(Mapper.class).addModifiers(Modifier.PUBLIC);
        
        // Write file
        JavaFile javaFile = JavaFile.builder(packageName, builder.build()).build();
        javaFile.writeTo(filer);
    }

    private static MethodSpec generateSelect(Element annotationElement, List<Element> fieldList, String tableName) throws ClassNotFoundException {

        String idFieldName = null;
        String idColumnName = null;
        Class<?> idClass = annotationElement.asType().getClass();

        for (Element e : fieldList) {
            Id id = e.getAnnotation(Id.class);
            if (id != null) {
                idFieldName = e.toString();
                idColumnName = id.id().equals("") ? idFieldName : id.id();
                idClass = e.asType().getClass();
            }
        }

        if (idFieldName == null) {
            return null;
        }

        String selectQuery = selectQuery(tableName, idColumnName, idFieldName);
        System.out.println(selectQuery);

        AnnotationSpec selectAnnotation = selectAnnotation(selectQuery);

        return MethodSpec.methodBuilder("findById")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addAnnotation(selectAnnotation)
                        .addParameter(idClass, idFieldName)
                        .returns(Class.forName(annotationElement.toString()))
                        .build()
        ;
    }

    private static String selectQuery(String tableName, String columnName, String fieldName) {
        return new StringBuilder().append("SELECT * FROM ")
                                .append(tableName)
                                .append(" WHERE ")
                                .append(columnName)
                                .append("=#{")
                                .append(fieldName)
                                .append("}")
                                .toString();
    }

    private static AnnotationSpec selectAnnotation(String selectQuery) {
        return AnnotationSpec.builder(Select.class)
                            .addMember("value", selectQuery)
                            .build();
    }


}
