# mybatis-processor

## Introduce

spring boot에서 사용할 목적으로 만들어본 annotation-processor를 이용하여 작성한 mybatis의 기본적인 CRUD를 자동으로 작성해주는 토이 프로젝트입니다.

## How to Use

### 소스 다운로드 -> 메이븐 레파지토리 등록

소스 다운로드 후, mybatis-processor 로컬 메이븐 레파지토리에 등록
```
C:\workspace3\mybatis-processor\mybatis-processor> mvn clean install
```

### 의존성 주입

``` xml
<dependency>
    <groupId>io.my.mybatis</groupId>
    <artifactId>annotation</artifactId>
    <version>0.0.1</version>
</dependency>
<dependency>
    <groupId>io.my.mybatis</groupId>
    <artifactId>processor</artifactId>
    <version>0.0.1</version>
</dependency>

<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.3</version>
</dependency>
```

### Entity 클래스 생성  

주의사항: Join 기능은 구현되지 않았으므로, 테이블의 컬럼들을 그대로 필드로 가져와야 합니다.

예시) 
``` sql
CREATE TABLE IF NOT EXISTS user (
  `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
  `login_id` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `user_group_id` INTEGER DEFAULT NULL,
  UNIQUE KEY USER_UNIQUE_LOGIN_ID (login_id)
);
```

``` java
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
```

### 레파지토리 생성 방법
```
C:\workspace3\mybatis-processor\demo> mvn clean install
```
clean을 붙여주는 이유는, 기존에 레파지토리가 이미 만들어져 있는 상태에서 수정하는 경우 해당 레파지토리를 삭제 후 다시 레파지토리를 생성하기 위함입니다.

## Annotation 소개

### @RepositoryMaker

Repository를 생성할 Entity 클래스에 적용합니다.

``` java
String table() default "";
String packageLocation() default "";
```

1. table(): 만약 Entity 클래스와 테이블 명이 다를 경우, 기준이 되는 table 명을 적어줄 수 있습니다.
2. packageLocation(): 패키지를 적어주지 않을 경우, io.my.mybatis.repositories 패키지에 레파지토리가 생성됩니다.

예시)

1. Entity Class

``` java
@RepositoryMaker(packageLocation = "io.my.demo.repository")
public class User { ... }

```
2. Generated Repository

``` java
package io.my.demo.repository;

@Mapper
public interface UserRepository { ... }
```

### @ColumnName

필드명과 컬럼명이 다른 경우, ColumnName으로 컬럼 명을 알려줘야 합니다.

예시) 


### @Id

``` java
boolean isAutoIncrement() default true;
boolean isUpdate() default false;
```

기본적인 코드는 전부 @Id를 적용한 필드를 기준으로 작성됩니다.
1. User findById(Long id);
2. int updateById(User entity);
3. int deleteById(Long id);
4. int insertEntity(User entity);

insert 메서드의 경우, isAutoIncrement를 false로 할 경우 id 값도 명시해줘야 합니다.

### @Find

적용 필드를 조건절로 하는 select 쿼리를 생성해줍니다.

``` java
boolean isList() default false;
boolean isOrderBy() default false;
boolean isLimit() default false;
OrderBy orderBy() default OrderBy.ASC;
String orderColumnName() default "id";
```

사용 예시)
``` java
@Find(isList = true, isOrderBy = true, orderBy = OrderBy.DESC)
private Long userGroupId;
```

결과
``` java
@Select("SELECT * FROM user WHERE user_group_id=#{userGroupId} ORDER BY id DESC")
List<User> findByUserGroupIdOrderById(Long userGroupId);
```

@Find 사용시 주의사항: 하나의 필드에 대해서 isList()는 일관성이 유지되어야 합니다.

잘못된 사용 예시)  

``` java
@Find
@Find(isList = true)
@ColumnName(columnName = "login_id")
private String login;
```

아래와 같이 코드가 생성되어, 에러 발생
``` java
@Select("SELECT * FROM user WHERE login_id=#{loginId}")
User findByLoginId(String loginId);
@Select("SELECT * FROM user WHERE login_id=#{loginId}")
List<User> findByLoginId(String loginId);
```

### Modify

적용 필드를 조건절로 하는 update 쿼리를 생성해줍니다.

적용 예시
``` java
@Modify
private String login;
```

``` java
@Update("UPDATE user SET password=#{password}, user_group_id=#{userGroupId} WHERE login_id=#{loginId}")
int updateByLoginId(User entity);
```

### Remove

적용 필드를 조건절로 하는 delete 쿼리를 생성해줍니다.

적용 예시
``` java
@remove
private String login;
```

``` java
@Delete("DELETE FROM user WHERE login_id=#{loginId}")
int deleteByLoginId(String loginId);
```