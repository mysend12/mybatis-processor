package io.my.mybatis.annotation.crud;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.my.mybatis.model.OrderBy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Find {
    boolean isList() default false;
    OrderBy orderBy() default OrderBy.ASC;
    String orderColumnName() default "";
    int limit() default 0;
}