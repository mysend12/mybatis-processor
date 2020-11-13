package io.my.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import io.my.demo.base.RepositoryBase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestRepoTests extends RepositoryBase {

    @Test
    void nowTest() {
        log.info("{}", testRepo.now());
        assertEquals(LocalDateTime.now().toLocalDate(), testRepo.now().toLocalDate());
    }
    
}
