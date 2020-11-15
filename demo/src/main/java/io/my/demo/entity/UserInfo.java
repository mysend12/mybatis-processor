package io.my.demo.entity;

import io.my.mybatis.annotation.crud.Find;
import io.my.mybatis.annotation.field.Id;
import io.my.mybatis.annotation.table.RepositoryMaker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RepositoryMaker(packageLocation = "io.my.demo.repository")
public class UserInfo {
    @Id
    private Long id;

    @Find
    private Long userId;

    @Find(isList = true)
    private String name;
    @Find
    private String nickName;
    @Find
    private String email;
    private Character gender;
}
