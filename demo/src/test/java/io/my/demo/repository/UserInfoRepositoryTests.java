package io.my.demo.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.my.demo.base.RepositoryBase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class UserInfoRepositoryTests extends RepositoryBase {

    @Test
    void selectUserInfoTest() {
        userInfoRepo.selectUserInfos().forEach(System.out::println);
        assertNotNull(userInfoRepo.selectUserInfos());
    }
    
}
