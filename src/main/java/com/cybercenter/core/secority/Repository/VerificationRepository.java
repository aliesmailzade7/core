package com.cybercenter.core.secority.Repository;

import com.cybercenter.core.secority.constant.VerifyCodeType;
import com.cybercenter.core.secority.dto.VerificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class VerificationRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.verify-code.duration}")
    private int VERIFY_CODE_DURATION;

    public void addVerifyCode(VerificationDTO dto, VerifyCodeType type) {
        redisTemplate.opsForValue().set(type.name() + dto.getUsername(), dto, VERIFY_CODE_DURATION, TimeUnit.SECONDS);
    }

    public VerificationDTO findVerifyCode(String username, VerifyCodeType type) {
        return (VerificationDTO) redisTemplate.opsForValue().get(type.name() + username);
    }

}
