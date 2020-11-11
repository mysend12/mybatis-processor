package io.my.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.my.demo.entity.UserGroup;

@Mapper
public interface UserGroupRepository {
    public List<UserGroup> selectUserGroups();
}
