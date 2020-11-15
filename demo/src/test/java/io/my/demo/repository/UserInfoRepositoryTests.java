package io.my.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.my.demo.base.RepositoryBase;
import io.my.demo.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class UserInfoRepositoryTests extends RepositoryBase {

    @Test
    void findByIdTest() {
        assertNotNull(userInfoRepo.findById(1L));
    }

    @Test
    void findByUserIdTest() {
        assertNotNull(userInfoRepo.findByUserId(1L));
    }

    @Test
    void findByNameTest() {
        userInfoRepo.findByName("HONG").forEach(entity -> assertEquals("HONG", entity.getName()));
    }

    @Test
    void findByNickNameTest() {
        assertEquals("HONG_NICK", userInfoRepo.findByNickName("HONG_NICK").getNickName());
    }

    @Test
    void findByEmailTest() {
        assertEquals("email@email.com", userInfoRepo.findByEmail("email@email.com").getEmail());
    }

    @Test
    void insertEntityTest() {
        UserInfo entity = new UserInfo();
        entity.setEmail("email");
        entity.setGender('F');
        entity.setName("name");
        entity.setNickName("nickName");
        entity.setUserId(6L);
        assertEquals(1, userInfoRepo.insertEntity(entity));
    }

    @Test
    void deleteByIdTest() {
        assertEquals(1, userInfoRepo.deleteById(1L));
    }


}
