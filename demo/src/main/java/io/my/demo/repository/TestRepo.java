package io.my.demo.repository;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestRepo {
    @Select("SELECT now() FROM DUAL")
    public LocalDateTime now();
}
