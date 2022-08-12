package com.ericlee.pstudio.alpha.global.security;

import com.ericlee.pstudio.alpha.global.utils.RandomUtil;
import kr.re.nsr.crypto.Hash;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 대한민국 국가 표준(KS X 3262) 암호함수인 LSH를 기반으로 하는 PasswordEncoder
 */
@RequiredArgsConstructor
@Component
public class LshPasswordEncoder implements PasswordEncoder {
    private final RandomUtil randomUtil;
    private final Hash.Algorithm algorithm = Hash.Algorithm.LSH512_512;

    @Override
    public String encode(CharSequence rawPassword) {
        return hashString(rawPassword.toString(), randomUtil.generateRandomString(10));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String[] tokens = encodedPassword.split("\\.");
        String salt = tokens[0];

        String requested = hashString(rawPassword.toString(), salt);

        return encodedPassword.equals(requested);
    }

    private String hashString(String data, String salt) {
        Hash engine = Hash.getInstance(algorithm);

        engine.update(data.getBytes(StandardCharsets.UTF_8));
        engine.update(salt.getBytes(StandardCharsets.UTF_8));
        byte[] hashBytes = engine.doFinal();

        return String.format("%s.%s", salt, Base64.getEncoder().encodeToString(hashBytes));
    }
}
