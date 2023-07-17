package com.cybercenter.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    @Value("${auth.login.failed.max_attempt}")
    public int MAX_ATTEMPT;
    @Value("${auth.login.failed.duration_day}")
    private int DURATION;
    private final RedisTemplate<String, Object> redisTemplate;
    private final HttpServletRequest request;


     /**
     * method for calculate the number of failed logins from an IP address.
     *
     * @param key - ip address
     */
    public void loginFailed(final String key) {
        int attempts;
        try {
            attempts = (int) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            attempts = 0;
        }
        attempts++;
        redisTemplate.opsForValue().set(key, attempts, DURATION, TimeUnit.DAYS);
    }

    /**
     * method for check ip address is blocked.
     *
\    * @return boolean - is block ip address
     */
    public boolean isBlocked() {
        try {
            int attempts = (int) redisTemplate.opsForValue().get(getClientIP());
            return attempts >= MAX_ATTEMPT;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * method for extracting the user's IP.
     *
     * @return ip address
     */
    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
