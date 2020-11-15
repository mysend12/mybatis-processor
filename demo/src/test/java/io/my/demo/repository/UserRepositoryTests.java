package io.my.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.my.demo.base.RepositoryBase;
import io.my.demo.entity.User;

class UserRepositoryTests extends RepositoryBase {
    
    @Test
    void findByIdTest() {
        assertNotNull(userRepo.findById(1L));
    }

    @Test
    void findByLoginIdTest() {
        assertEquals("USER1", userRepo.findByLoginId("USER1").getLoginId());
    }

    @Test
    void findByUserGroupIdTest() {
        List<User> userList = userRepo.findByUserGroupId(1L);
        userList.forEach(entity -> assertEquals(1L, entity.getUserGroupId()));
    }

    @Test
    void findByUserGroupIdLimitTest() {
        List<User> userList = userRepo.findByUserGroupIdLimit(1L, 2);
        userList.forEach(entity -> assertEquals(1L, entity.getUserGroupId()));
        assertEquals(2, userList.size());
    }

    @Test
    void findByUserGroupIdOrderByTest() {
        List<User> userList = userRepo.findByUserGroupIdOrderById(1L);
        userList.forEach(entity -> assertEquals(1L, entity.getUserGroupId()));
        assertEquals(2, userList.size());
    }

    @Test
    void insertEntityTest() {
        User entity = new User();
        entity.setLoginId("loginIdTest");
        entity.setPassword("passwordTest");
        entity.setUserGroupId(1L);
        assertEquals(1L, userRepo.insertEntity(entity));
    }

    @Test
    void updateByIdTest() {
        User entity = new User();
        entity.setLoginId("loginIdTest");
        entity.setPassword("passwordTest");
        entity.setUserGroupId(1L);
        entity.setId(1L);
        assertEquals(1, userRepo.updateById(entity));
    }

    @Test
    void deleteByIdTest() {
        assertEquals(1, userRepo.deleteById(1L));
    }
    
}
