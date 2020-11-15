// package io.my.demo.repository;

// import java.util.List;

// import org.apache.ibatis.annotations.Mapper;
// import org.apache.ibatis.annotations.ResultMap;
// import org.apache.ibatis.annotations.Select;

// import io.my.demo.entity.User;

// @Mapper
// public interface UserRepository {
//     List<User> selectUsers();

//     @ResultMap("userResultMap")
//     @Select("SELECT * FROM `USER_INFO` ui JOIN `USER` u ON u.ID = ui.USER_ID JOIN USER_GROUP ug ON u.USER_GROUP_ID = ug.ID ORDER BY ug.GROUP_NAME ASC")
//     List<User> selectUserList();
// }
