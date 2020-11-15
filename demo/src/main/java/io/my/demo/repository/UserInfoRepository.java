// package io.my.demo.repository;

// import java.util.List;

// import org.apache.ibatis.annotations.Mapper;
// import org.apache.ibatis.annotations.ResultMap;
// import org.apache.ibatis.annotations.Select;

// import io.my.demo.entity.UserInfo;

// @Mapper
// public interface UserInfoRepository {
//     public List<UserInfo> selectUserInfos();

//     @ResultMap("userInfoResultMap")
//     @Select("SELECT * FROM USER_INFO ui JOIN `USER` u ON ui.USER_ID = u.ID")
//     public List<UserInfo> selectUserInfoList();
   
// }
