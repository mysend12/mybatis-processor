package io.my.mybatis.util;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

public class RepositoryUtil {

    private RepositoryUtil() {
        throw new IllegalAccessError();
    }

    public static List<Element> getFieldList(TypeElement typeElement) {
        List<Element> fieldList = new ArrayList<>();

        typeElement.getEnclosedElements().forEach(type -> {
            if (type.getKind() == ElementKind.FIELD) {
                fieldList.add(type);
            }
        });

        return fieldList;
    }

    public static Class<?> getClass(TypeElement typeElement) throws ClassNotFoundException {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        String className = typeElement.getQualifiedName().toString();
        return Class.forName(className, true, systemClassLoader);
    }

    // public static Class<?> getReturnType(RepositoryMaker repository) {
    //     try {
    //         return repository.returnType();
    //     } catch (MirroredTypeException e) {
    //         try {
    //             return Class.forName(e.getTypeMirror().toString());
    //         } catch (ClassNotFoundException e1) {
    //             e1.printStackTrace();
    //         }
    //     }
    //     return null;
    // }

    // public static List<Class<?>> getParams(RepositoryMaker repository) {
    //     List<Class<?>> classList = new ArrayList<>();

    //     try {
    //         return List.of(repository.params());
    //     } catch (MirroredTypesException e) {
    //         e.getTypeMirrors().forEach(typeMirror -> {
    //             try {
    //                 classList.add(Class.forName(typeMirror.toString()));
    //             } catch (ClassNotFoundException e1) {
    //             }
    //         });
    //     }

    //     return classList;
    // }
}
