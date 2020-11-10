package io.my.mybatis.annotation.crud;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.my.mybatis.wrapper.Finds;

@Repeatable(Finds.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Find {
    boolean isList() default false;
    boolean isOrderBy() default false;
    boolean isLimit() default false;
}