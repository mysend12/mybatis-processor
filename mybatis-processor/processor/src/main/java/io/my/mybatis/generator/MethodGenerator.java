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

import io.my.mybatis.annotation.crud.Find;
import io.my.mybatis.annotation.crud.Modify;
import io.my.mybatis.annotation.crud.Remove;
import io.my.mybatis.annotation.field.Id;
import io.my.mybatis.model.OrderBy;
import io.my.mybatis.util.NamingStrategy;

public class MethodGenerator {
    private static Logger logger = LoggerFactory.getLogger(MethodGenerator.class);
    private static String ENTITY = "entity";

    private MethodGenerator() {
        throw new IllegalAccessError();
    }

    public static List<MethodSpec> generateSelectList(
        Element annotationElement, 
        List<Element> fieldElementList, 
        String tableName) {

        List<MethodSpec> result = new ArrayList<>();
        TypeName returnType = TypeName.get(((TypeElement) annotationElement).asType());
        
        for (Element e : fieldElementList) {
            Find[] finds = e.getAnnotationsByType(Find.class);
            if (finds.length > 1) {
                List<MethodSpec> methods = generateSelectList(e, returnType, tableName);
                if (!methods.isEmpty()) { result.addAll(methods); }
            } else {
                MethodSpec method = generateSelect(e, returnType, tableName);
                if (method != null) { result.add(method); }
            }
            
        }

        return result;
    }

    public static List<MethodSpec> generateSelectList(Element e, TypeName returnType, String tableName) {
        List<MethodSpec> result = new ArrayList<>();
        Find[] finds = e.getAnnotationsByType(Find.class);

        String fieldName = e.toString();
        String columnName = NamingStrategy.columnName(e);

        for (Find find : finds) {
            String selectQuery = generateSelectQuery(find, tableName, columnName, fieldName);
            boolean isList = find.isList();
            
            if (selectQuery == null) continue;
                        
            String methodName = methodName(find, fieldName);
            
            AnnotationSpec selectAnnotation = AnnotationGenerator.selectAnnotation(selectQuery);
            result.add(MethodSpec.methodBuilder(methodName)
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addAnnotation(selectAnnotation)
                        .addParameter(TypeName.get(e.asType()), fieldName)
                        .returns(
                            isList ? 
                                ParameterizedTypeName.get(ClassName.get(List.class), returnType) : 
                                returnType)
                        .build())
            ;
        }

        return result;
    }

    public static MethodSpec generateSelect(Element e, TypeName returnType, String tableName) {
        String fieldName = e.toString();

        Find find = e.getAnnotation(Find.class);
        boolean isList = false;

        if (find != null) {
            fieldName = e.toString();
            isList = find.isList();
        } else if (e.getAnnotation(Id.class) == null) {
            return null;
        }

        String selectQuery = generateSelectQuery(find, tableName, NamingStrategy.columnName(e), fieldName);
        if (selectQuery == null) return null;
        
        String methodName = methodName(find, fieldName);
        
        AnnotationSpec selectAnnotation = AnnotationGenerator.selectAnnotation(selectQuery);
        
        return MethodSpec.methodBuilder(methodName)
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addAnnotation(selectAnnotation)
                        .addParameter(TypeName.get(e.asType()), fieldName)
                        .returns(
                            isList ? 
                                ParameterizedTypeName.get(ClassName.get(List.class), returnType) : 
                                returnType)
                        .build()
        ;
    }

    private static String generateSelectQuery(
        Find find, 
        String tableName, 
        String columnName, 
        String fieldName
    ) {
        if (find == null) {
            return QueryGenerator.selectQuery(tableName, columnName, fieldName);
        }

        String orderColumnName = find.orderColumnName();
        int limit = find.limit();
        OrderBy order = find.orderBy();
        
        return QueryGenerator.selectQuery(
            tableName, columnName, fieldName, orderColumnName, order, limit);
    }

    private static String methodName(Find find, String fieldName) {
        StringBuilder sb = new StringBuilder().append("findBy").append(NamingStrategy.firstCharUpper(fieldName));

        if (find != null) {
            methodName(sb, find);
        }

        return sb.toString();
    }

    private static void methodName(StringBuilder sb, Find find) {

        String order = find.orderBy().getOrder();
        String orderColumnName = find.orderColumnName();
        int limit = find.limit();

        if (!orderColumnName.equals("")) {
            sb.append("OrderBy").append(orderColumnName).append(order);
        }

        if (limit > 0) {
            sb.append("Limit").append(limit);
        }
    }

    public static MethodSpec generateInsert(TypeElement typeElement, List<Element> fieldElementList, String tableName) {
        List<String> fieldList = new ArrayList<>();
        List<String> columnList = new ArrayList<>();
        
        fieldElementList.forEach(e -> {
            Id id = e.getAnnotation(Id.class);

            if (id != null && id.isAutoIncrement()) {
                return;
            } else {
                columnList.add(NamingStrategy.columnName(e));
                fieldList.add(e.toString());
            }
        });

        String insertQuery = QueryGenerator.insertQuery(tableName, columnList, fieldList);
        AnnotationSpec insertAnnotation = AnnotationGenerator.insertAnnotation(insertQuery);
        
        TypeName classTypeName = TypeName.get(typeElement.asType());

        return MethodSpec.methodBuilder("insertEntity")
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

            if (id != null) {
                conditionField = e.toString();
                conditionColumn = NamingStrategy.columnName(e);
            } else {
                columnList.add(NamingStrategy.columnName(e));
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
        String conditionColumn = NamingStrategy.columnName(element);

        for (Element e : fieldElementList) {
            Id id = e.getAnnotation(Id.class);
            
            if (id != null && !id.isUpdate() || e.equals(element)) {
                continue;
            } else {
                columnList.add(NamingStrategy.columnName(e));
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

        return MethodSpec.methodBuilder("updateBy" + String.valueOf(NamingStrategy.firstCharUpper(fieldName)))
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addAnnotation(updateAnnotation)
                        .addParameter(classTypeName, ENTITY)
                        .returns(TypeName.INT)
                        .build();
    }

    public static List<MethodSpec> generateDeleteList(List<Element> fieldElementList, String tableName) {
        List<MethodSpec> result = new ArrayList<>();
        
        for (Element e : fieldElementList) {
            MethodSpec method = generateDelete(e, tableName);

            if (method != null) {
                result.add(method);
            }
        }

        return result;
    }

    public static MethodSpec generateDelete(Element e, String tableName) {
        String fieldName = null;

        Id id = e.getAnnotation(Id.class);
        Remove remove = e.getAnnotation(Remove.class);

        if (id != null) {
            fieldName = e.toString();
        } else if (remove != null) {
            fieldName = e.toString();
        }
        
        String columnName = NamingStrategy.columnName(e);

        if (fieldName == null || columnName == null) {
            return null;
        }

        String deleteQuery = QueryGenerator.deleteQuery(tableName, columnName, fieldName);

        AnnotationSpec deleteAnnotation = AnnotationGenerator.deleteAnnotation(deleteQuery);
        
        return MethodSpec.methodBuilder("deleteBy" + String.valueOf(NamingStrategy.firstCharUpper(fieldName)))
                        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
                        .addAnnotation(deleteAnnotation)
                        .addParameter(TypeName.get(e.asType()), fieldName)
                        .returns(TypeName.INT)
                        .build()
        ;
    }


}
