package com.ericlee.pstudio.alpha.util;

import com.ericlee.pstudio.alpha.global.security.LshPasswordEncoder;
import com.ericlee.pstudio.alpha.global.utils.RandomUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LshPasswordEncoderTest {

    @Spy
    private RandomUtil randomUtil;

    @InjectMocks
    private LshPasswordEncoder passwordEncoder;

    @RepeatedTest(10)
    @DisplayName("PasswordEncoder 검증 (성공)")
    @Test
    void validWithSuccess() {
        String password = randomUtil.generateRandomString(6);

        String encodedHash = passwordEncoder.encode(password);
        System.out.printf("Encoded: %s\n", encodedHash);

        assertThat(passwordEncoder.matches(password, encodedHash)).isTrue();
    }

    @RepeatedTest(10)
    @DisplayName("PasswordEncoder 검증 (실패)")
    @Test
    void validWithFailure() {
        String password = randomUtil.generateRandomString(6);

        String encodedHash = passwordEncoder.encode(password);
        System.out.printf("Encoded: %s\n", encodedHash);

        String wrongPassword = String.format("%s%s", password, randomUtil.generateRandomString(4));

        assertThat(passwordEncoder.matches(wrongPassword, encodedHash)).isFalse();
    }

}
