package io.my.demo.entity;

import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("USER_GROUP")
public class UserGroup {
    private Long id;
    private String groupName;
    private String groupNickName;

    private List<User> userList;
}
