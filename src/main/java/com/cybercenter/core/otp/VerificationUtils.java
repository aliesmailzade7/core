package com.cybercenter.core.otp;

import com.cybercenter.core.constant.VerifyCodeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationUtils {

    private final VerificationCache verificationCache;

    /**
     * Method for generating verify code and put it in cache.
     *
     * @param key      - cache key
     * @param type     - verify code type
     * @return cache value (generated verify code)
     */
    public Integer generateVerifyCode(String key, VerifyCodeType type) {
        Verification verification = verificationCache.findVerifyCode(key, type);
        if (ObjectUtils.isEmpty(verification)) {
            log.debug("Try to generate new verify code for: {}", key);
            Random random = new Random();
            int verifyCode = 100000 + random.nextInt(900000);
            createNewVerificationDTO(key, verifyCode, type);
            return verifyCode;
        } else
            return verification.getVerifyCode();
    }

    /**
     * Method for create VerificationDTO and put it in cache.
     *
     * @param key        - cache key
     * @param verifyCode -
     * @param type       - VerifyCodeType object
     */
    private void createNewVerificationDTO(String key, int verifyCode, VerifyCodeType type) {
        log.debug("Try to add new verify code record for: {}", key);
        Verification verification = Verification.builder()
                .verifyCode(verifyCode)
                .key(key)
                .build();
        verificationCache.addVerifyCode(verification, type);
    }

    public boolean validateVerifyCode(String key, Integer verifyCode, VerifyCodeType type) {
        Verification dto = verificationCache.findVerifyCode(key, type);
        return !ObjectUtils.isEmpty(dto) && Objects.equals(dto.getVerifyCode(), verifyCode);
    }
}
