package io.my.demo.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import io.my.demo.repository.TestRepo;
import io.my.demo.repository.UserGroupRepository;
import io.my.demo.repository.UserInfoRepository;
import io.my.demo.repository.UserRepository;

@Transactional
@SpringBootTest
@ActiveProfiles(value = "test")
public class RepositoryBase {

    @Autowired
    protected TestRepo testRepo;

    @Autowired
    protected UserRepository userRepo;

    @Autowired
    protected UserInfoRepository userInfoRepo;

    @Autowired
    protected UserGroupRepository userGroupRepo;
}
