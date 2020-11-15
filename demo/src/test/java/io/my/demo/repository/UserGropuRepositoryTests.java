package io.my.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.my.demo.base.RepositoryBase;
import io.my.demo.entity.UserGroup;

class UserGropuRepositoryTests extends RepositoryBase {
    
    @Test
    void selectUserGroupTest() {
        assertNotNull(userGroupRepo.findById(1L));
    }

    @Test
    void findByGroupNameTest() {
        assertNotNull(userGroupRepo.findByGroupName("HOME1"));
    }

    @Test
    void findByGroupNickNameTest() {
        assertNotNull(userGroupRepo.findByGroupNickName("HOME1_NICK"));
    }

    @Test
    void inserEntityTest() {
        UserGroup entity = new UserGroup();
        entity.setGroupName("groupName");
        entity.setGroupNickName("groupNickName");
        assertEquals(1, userGroupRepo.insertEntity(entity));
    }

    @Test
    void updateByIdTest() {
        UserGroup entity = userGroupRepo.findById(1L);
        entity.setGroupName("updateGroupName");
        assertEquals(1, userGroupRepo.updateById(entity));
    }

    @Test
    void deleteByIdTest() {
        assertEquals(1, userGroupRepo.deleteById(1L));
    }
    
}
