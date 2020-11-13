package io.my.demo.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.my.demo.base.RepositoryBase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class UserGropuRepositoryTests extends RepositoryBase {
    
    @Test
    void selectUserGroupsTest() {
        userGroupRepo.selectUserGroups().forEach(System.out::println);
        assertNotNull(userGroupRepo.selectUserGroups());
    }

    @Test
    void selectUserGroupListTest() {
        userGroupRepo.selectUserGroupList().forEach(System.out::println);
        assertNotNull(userGroupRepo.selectUserGroupList());
    }
    
}
