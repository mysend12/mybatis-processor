package io.my.demo.entity;

import io.my.mybatis.annotation.crud.Find;
import io.my.mybatis.annotation.crud.Modify;
import io.my.mybatis.annotation.crud.Remove;
import io.my.mybatis.annotation.field.Id;
import io.my.mybatis.annotation.model.OrderBy;
import io.my.mybatis.annotation.table.RepositoryMaker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RepositoryMaker(packageLocation = "io.my.demo.repository")
public class User {
    @Id
    private Long id;

    @Find @Modify @Remove
    private String loginId;
    private String password;

    @Find(isList = true)
    @Find(isList = true, isLimit = true)
    @Find(isList = true, isOrderBy = true, orderBy = OrderBy.DESC)
    private Long userGroupId;

}
