package io.my.mybatis.generator;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.my.mybatis.annotation.Find;
import io.my.mybatis.annotation.Id;
import io.my.mybatis.annotation.Modify;
import io.my.mybatis.util.NamingStrategy;

public class MethodGenerator {
    private static Logger logger = LoggerFactory.getLogger(MethodGenerator.class);
    private static String ENTITY = "entity";

    private MethodGenerator() {
        throw new IllegalAccessError();
    }

    public static List<MethodSpec> generateSelectList(
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

    public static MethodSpec generateSelect(TypeElement typeElement, Element e, String tableName) throws ClassNotFoundException {
        String fieldName = null;
        String columnName = null;
        TypeName returnType = TypeName.get(typeElement.asType());

        Id id = e.getAnnotation(Id.class);
        Find find = e.getAnnotation(Find.class);
        Modify modify = e.getAnnotation(Modify.class);

        if (id != null) {
            fieldName = e.toString();
            columnName = NamingStrategy.columnName(id.columnName(), fieldName);
        } else if (find != null) {
            fieldName = e.toString();
            columnName = NamingStrategy.columnName(find.columnName(), fieldName);
            returnType = find.isList() ? 
                        ParameterizedTypeName.get(ClassName.get(List.class), returnType) : 
                        returnType
            ;
        } else if (modify != null) {
            fieldName = e.toString();
            columnName = NamingStrategy.columnName(modify.columnName(), fieldName);
        }
        
        logger.info(
            "\nfieldName: {} \ncolumnName: {}", fieldName, columnName);

        if (fieldName == null || columnName == null) {
            return null;
        }

        String selectQuery = QueryGenerator.selectQuery(tableName, columnName, fieldName);

        AnnotationSpec selectAnnotation = AnnotationGenerator.selectAnnotation(selectQuery);
        
        return MethodSpec.methodBuilder("findBy" + String.valueOf(NamingStrategy.firstCharUpper(fieldName)))
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addAnnotation(selectAnnotation)
                        .addParameter(TypeName.get(e.asType()), fieldName)
                        .returns(returnType)
                        .build()
        ;
    }

    public static MethodSpec generateInsert(TypeElement typeElement, List<Element> fieldElementList, String tableName) {
        List<String> fieldList = new ArrayList<>();
        List<String> columnList = new ArrayList<>();

        fieldElementList.forEach(e -> {
            Id id = e.getAnnotation(Id.class);
            Find find = e.getAnnotation(Find.class);
            Modify modify = e.getAnnotation(Modify.class);

            if (id != null && id.isAutoIncrement()) {
                return;
            } else if (find != null && !find.columnName().equals("")) {
                columnList.add(find.columnName());
                fieldList.add(e.toString());
            } else if (modify != null && !modify.columnName().equals("")) {
                columnList.add(modify.columnName());
                fieldList.add(e.toString());
            } else {
                columnList.add(NamingStrategy.camelToSnake(e.toString()));
                fieldList.add(e.toString());
            }
        });

        String insertQuery = QueryGenerator.insertQuery(tableName, columnList, fieldList);
        AnnotationSpec insertAnnotation = AnnotationGenerator.insertAnnotation(insertQuery);
        
        TypeName classTypeName = TypeName.get(typeElement.asType());
        String className = typeElement.getSimpleName().toString();

        return MethodSpec.methodBuilder("insert" + className)
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addAnnotation(insertAnnotation)
                        .addParameter(classTypeName, ENTITY)
                        .returns(TypeName.INT)
                        .build()
        ;
    }

    public static List<MethodSpec> generateUpdateList(TypeElement typeElement, List<Element> fieldElementList, String tableName) {
        List<MethodSpec> result = new ArrayList<>();

        for (Element e : fieldElementList) {
            Id id = e.getAnnotation(Id.class);
            Modify modify = e.getAnnotation(Modify.class);
            if (id != null) {
                result.add(generateUpdateById(typeElement, fieldElementList, tableName));
            } else if (modify != null) {
                result.add(generateUpdateByModify(typeElement, e, fieldElementList, tableName));
            }
        }


        return result;
    }

    public static MethodSpec generateUpdateById(TypeElement typeElement, List<Element> fieldElementList, String tableName) {
        List<String> fieldList = new ArrayList<>();
        List<String> columnList = new ArrayList<>();
        String conditionField = null;
        String conditionColumn = null;

        for (Element e : fieldElementList) {
            Id id = e.getAnnotation(Id.class);
            Find find = e.getAnnotation(Find.class);
            Modify modify = e.getAnnotation(Modify.class);

            if (id != null) {
                conditionField = e.toString();
                conditionColumn = NamingStrategy.columnName(id.columnName(), conditionField);
            } else if (find != null && !find.columnName().equals("")) {
                columnList.add(find.columnName());
                fieldList.add(e.toString());
            } else if (modify != null && !modify.columnName().equals("")) {
                columnList.add(modify.columnName());
                fieldList.add(e.toString());
            } else {
                columnList.add(NamingStrategy.camelToSnake(e.toString()));
                fieldList.add(e.toString());
            }
        }

        if (conditionField == null || conditionColumn == null) {
            return null;
        }

        String updateQuery = QueryGenerator.updateQuery(tableName, columnList, fieldList, conditionField, conditionColumn);
        AnnotationSpec updateAnnotation = AnnotationGenerator.updateAnnotataion(updateQuery);

        return generateUpdate(typeElement, updateAnnotation, conditionField);
    }

    public static MethodSpec generateUpdateByModify(
        TypeElement typeElement, 
        Element element, 
        List<Element> fieldElementList, 
        String tableName
    ) {
        List<String> fieldList = new ArrayList<>();
        List<String> columnList = new ArrayList<>();
        String conditionField = element.toString();
        String conditionColumn = NamingStrategy.columnName(element.getAnnotation(Modify.class).columnName(), conditionField);

        for (Element e : fieldElementList) {
            Id id = e.getAnnotation(Id.class);
            Find find = e.getAnnotation(Find.class);
            Modify modify = e.getAnnotation(Modify.class);

            if (id != null && id.isUpdate()) {
                columnList.add(NamingStrategy.columnName(id.columnName(), conditionField));
                fieldList.add(e.toString());
            } else if (id != null) {
                continue;
            } else if (find != null && !find.columnName().equals("")) {
                columnList.add(find.columnName());
                fieldList.add(e.toString());
            } else if (modify != null) {
                if (e.equals(element)) {
                    continue;
                }
                columnList.add(NamingStrategy.columnName(modify.columnName(), conditionField));
                fieldList.add(e.toString());
            } else {
                columnList.add(NamingStrategy.camelToSnake(e.toString()));
                fieldList.add(e.toString());
            }
        }

        if (conditionField == null || conditionColumn == null) {
            return null;
        }

        String updateQuery = QueryGenerator.updateQuery(tableName, columnList, fieldList, conditionField, conditionColumn);
        AnnotationSpec updateAnnotation = AnnotationGenerator.updateAnnotataion(updateQuery);
        
        return generateUpdate(typeElement, updateAnnotation, conditionField);
    }

    public static MethodSpec generateUpdate(TypeElement typeElement, AnnotationSpec updateAnnotation, String fieldName) {
        TypeName classTypeName = TypeName.get(typeElement.asType());
        String className = typeElement.getSimpleName().toString();

        return MethodSpec.methodBuilder("update" + className + "By" + String.valueOf(NamingStrategy.firstCharUpper(fieldName)))
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addAnnotation(updateAnnotation)
                        .addParameter(classTypeName, ENTITY)
                        .returns(TypeName.INT)
                        .build();
    }

}
