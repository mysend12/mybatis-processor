package io.my.mybatis.util;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;

import io.my.mybatis.annotation.RepositoryMaker;

public class RepositoryUtil {

    private RepositoryUtil() {
        throw new IllegalAccessError();
    }

    public static Class<?> getReturnType(RepositoryMaker repository) throws ClassNotFoundException {
        try {
            return repository.returnType();
        } catch (MirroredTypeException e) {
            return Class.forName(e.getTypeMirror().toString());
        }
    }

    public static List<Class<?>> getParams(RepositoryMaker repository) {
        List<Class<?>> classList = new ArrayList<>();

        try {
            return List.of(repository.params());
        } catch (MirroredTypesException e) {
            e.getTypeMirrors().forEach(typeMirror -> {
                try {
                    classList.add(Class.forName(typeMirror.toString()));
                } catch (ClassNotFoundException e1) {
                }
            });
        }

        return classList;
    }
    
}
