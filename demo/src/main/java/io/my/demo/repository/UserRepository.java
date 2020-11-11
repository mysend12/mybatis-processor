package io.my.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.my.demo.entity.User;

@Mapper
public interface UserRepository {
    List<User> selectUsers();
}
