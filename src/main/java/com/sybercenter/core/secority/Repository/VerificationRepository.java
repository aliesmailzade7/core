package com.sybercenter.core.secority.Repository;

import com.sybercenter.core.secority.dto.VerificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class VerificationRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.verify-otp.duration}")
    private int VERIFY_OTP_DURATION;

    public void addVerifyOtp(VerificationDTO dto) {
        redisTemplate.opsForValue().set(dto.getUsername(), dto, VERIFY_OTP_DURATION, TimeUnit.SECONDS);
    }

    public VerificationDTO findVerifyOtp(String username) {
        return (VerificationDTO) redisTemplate.opsForValue().get(username);
    }

}
