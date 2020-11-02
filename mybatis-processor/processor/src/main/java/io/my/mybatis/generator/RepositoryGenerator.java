package io.my.mybatis.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.my.mybatis.annotation.Find;
import io.my.mybatis.annotation.Id;
import io.my.mybatis.annotation.RepositoryMaker;
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
        
        // get Class Fields
        List<Element> fieldList = RepositoryUtil.getFieldList(typeElement);

        // Generate Methods
        List<MethodSpec> selectList = generateSelect(annotationElement, fieldList, Object.class, tableName);
        
        // add Method in Builder
        selectList.forEach(builder::addMethod);

        builder.addAnnotation(Mapper.class)
                .addModifiers(Modifier.PUBLIC);
        
        // Write file
        JavaFile javaFile = JavaFile.builder(packageName, builder.build()).build();
        javaFile.writeTo(filer);
    }

    private static List<MethodSpec> generateSelect(
        Element annotationElement, 
        List<Element> fieldList, 
        Class<?> returnType, 
        String tableName) throws ClassNotFoundException {

        List<MethodSpec> result = new ArrayList<>();
        
        for (Element e : fieldList) {
            MethodSpec method = generateSelect((TypeElement) annotationElement, e, tableName);

            if (method != null) {
                result.add(method);
            }
        }

        return result;
    }

    private static MethodSpec generateSelect(TypeElement typeElement, Element e, String tableName) throws ClassNotFoundException {
        String fieldName = null;
        String columnName = null;
        
        Id id = e.getAnnotation(Id.class);
        Find find = e.getAnnotation(Find.class);
        if (id != null) {
            fieldName = e.toString();
            columnName = columnName(id.fieldName(), fieldName);
        } else if (find != null) {
            fieldName = e.toString();
            columnName = columnName(find.fieldName(), fieldName);
        }
        logger.info(
            "\nfieldName: {} \ncolumnName: {}", fieldName, columnName);

        if (fieldName == null || columnName == null) {
            return null;
        }

        String selectQuery = selectQuery(tableName, columnName, fieldName);

        AnnotationSpec selectAnnotation = selectAnnotation(selectQuery);
        return MethodSpec.methodBuilder("findBy" + String.valueOf(NamingStrategy.firstCharUpper(fieldName)))
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addAnnotation(selectAnnotation)
                        .addParameter(TypeName.get(e.asType()), fieldName)
                        .returns(TypeName.get(typeElement.asType()))
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
                            .addMember("value", "$S", selectQuery)
                            .build();
    }

    private static String columnName(String column, String field) {
        if (column != null && !column.equals("")) {
            return column;
        } else {
            return NamingStrategy.camelToSnake(field);
        }
    }


}
