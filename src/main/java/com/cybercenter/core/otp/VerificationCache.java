package com.cybercenter.core.otp;

import com.cybercenter.core.constant.VerifyCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class VerificationCache {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.verify-code.duration}")
    private int VERIFY_CODE_DURATION;

    public void addVerifyCode(Verification dto, VerifyCodeType type) {
        redisTemplate.opsForValue().set(type.name() + "_" + dto.getKey(), dto, VERIFY_CODE_DURATION, TimeUnit.SECONDS);
    }

    public Verification findVerifyCode(String key, VerifyCodeType type) {
        return (Verification) redisTemplate.opsForValue().get(type.name() + "_" + key);
    }

}
