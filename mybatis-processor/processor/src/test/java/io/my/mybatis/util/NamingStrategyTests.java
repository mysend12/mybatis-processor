package io.my.mybatis.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NamingStrategyTests {
    Logger logger = LoggerFactory.getLogger(NamingStrategy.class);

    @MethodSource
    @ParameterizedTest(name = "{0} 스네이크 변환 테스트")
    @DisplayName("카멜케이스 -> 스네이크 케이스 변환 테스트")
    void camelToSnakeTest(String str, String expected) {
        String actual = NamingStrategy.camelToSnake(str);
        logger.info("{} -> {}", str, actual);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> camelToSnakeTest(){
        return Stream.of(
            Arguments.of("helloWorld", "hello_world"), 
            Arguments.of("testCase", "test_case"), 
            Arguments.of("TestCase", "test_case")
        );
    }


}
