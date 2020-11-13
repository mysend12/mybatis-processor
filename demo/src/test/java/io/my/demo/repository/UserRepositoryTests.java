package io.my.demo.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.my.demo.base.RepositoryBase;
import io.my.demo.entity.User;

class UserRepositoryTests extends RepositoryBase {
    
    @Test
    void selectUsersTest() {
        List<User> userList = userRepo.selectUsers();
        userList.forEach(System.out::println);
        assertNotNull(userList);
    }

    @Test
    void selectUserListTest() {
        List<User> userList = userRepo.selectUserList();
        userList.forEach(System.out::println);
        assertNotNull(userList);
    }
    
}
