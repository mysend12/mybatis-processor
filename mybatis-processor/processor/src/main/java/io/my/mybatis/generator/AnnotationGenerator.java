package io.my.mybatis.generator;

import com.squareup.javapoet.AnnotationSpec;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationGenerator {
    private static Logger logger = LoggerFactory.getLogger(AnnotationGenerator.class);
    
    private AnnotationGenerator() {
        throw new IllegalAccessError();
    }
    
    public static AnnotationSpec selectAnnotation(String selectQuery) {
        return generateAnnotation(Select.class, selectQuery);
    }
    
    public static AnnotationSpec insertAnnotation(String insertQuery) {
        return generateAnnotation(Insert.class, insertQuery);
    }

    public static AnnotationSpec updateAnnotataion(String updateQuery) {
        return generateAnnotation(Update.class, updateQuery);
    }

    public static AnnotationSpec deleteAnnotation(String deleteQuery) {
        return generateAnnotation(Delete.class, deleteQuery);
    }

    public static AnnotationSpec generateAnnotation(Class<?> clz, String query) {
        return AnnotationSpec.builder(clz)
                            .addMember("value", "$S", query)
                            .build();
    }
}
