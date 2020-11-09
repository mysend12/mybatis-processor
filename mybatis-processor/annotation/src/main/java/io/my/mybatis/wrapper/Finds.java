package io.my.mybatis.wrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.my.mybatis.annotation.crud.Find;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Finds {
    Find[] value();
}
