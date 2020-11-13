package io.my.demo.entity;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Alias("USER")
@ToString(exclude = "userGroup")
public class User {
    private Long id;
    private String loginId;
    private String password;
    private Long userGroupId;

    private UserGroup userGroup;
    private UserInfo userInfo;
}
