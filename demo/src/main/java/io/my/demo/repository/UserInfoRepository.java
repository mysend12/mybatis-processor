package io.my.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.my.demo.entity.UserInfo;

@Mapper
public interface UserInfoRepository {
    public List<UserInfo> selectUserInfos();
}
