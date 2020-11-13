package io.my.demo.entity;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "user")
@Alias("USER_INFO")
public class UserInfo {
    private Long id;
    private Long userId;
    private String name;
    private String nickName;
    private String email;
    private Character gender;

    private User user;
}
