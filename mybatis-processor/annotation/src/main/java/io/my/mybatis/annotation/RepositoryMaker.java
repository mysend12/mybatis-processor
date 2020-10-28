package io.my.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RepositoryMaker {
    String id();
    Class<?> returnType() default String.class;
    Class<?>[] params();
    String[] paramNames();
}
