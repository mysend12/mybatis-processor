package io.my.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import io.my.demo.entity.UserGroup;

@Mapper
public interface UserGroupRepository {
    public List<UserGroup> selectUserGroups();

    @ResultMap("userGroupReslutMap")
    @Select("SELECT u.ID as user_id, u.LOGIN_ID as user_login_id, u.PASSWORD as user_passwod, ug.ID as user_group_id, ug.GROUP_NAME as user_group_name, ug.GROUP_NICK_NAME as user_group_nick_name FROM test.USER_GROUP ug LEFT JOIN test.`USER` u ON ug.ID = u.USER_GROUP_ID")
    public List<UserGroup> selectUserGroupList();
}
