package com.cybercenter.core.secority.Service;

import com.cybercenter.core.secority.Repository.VerificationRepository;
import com.cybercenter.core.secority.constant.VerifyCodeType;
import com.cybercenter.core.secority.dto.VerificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerificationService {

    private final VerificationRepository verificationRepository;

    /**
     * Method for generating verify code and put it in cache.
     *
     * @param username - cache key
     * @param type     - verify code type
     * @return cache value (generated verify code)
     */
    public Integer generateVerifyCode(String username, VerifyCodeType type) {
        VerificationDTO verificationDTO = verificationRepository.findVerifyCode(username, type);
        if (ObjectUtils.isEmpty(verificationDTO)) {
            log.debug("Try to generate new verify code for: {}", username);
            Random random = new Random();
            int verifyCode = 100000 + random.nextInt(900000);
            createNewVerificationDTO(username, verifyCode, type);
            return verifyCode;
        } else
            return verificationDTO.getVerifyCode();
    }

    /**
     * Method for create VerificationDTO and put it in cache.
     *
     * @param username   - cache key
     * @param verifyCode -
     * @param type       - VerifyCodeType object
     */
    private void createNewVerificationDTO(String username, int verifyCode, VerifyCodeType type) {
        log.debug("Try to add new verify code record for: {}", username);
        VerificationDTO verificationDTO = VerificationDTO.builder()
                .verifyCode(verifyCode)
                .username(username)
                .build();
        verificationRepository.addVerifyCode(verificationDTO, type);
    }

    public boolean validateVerifyCode(String username, Integer verifyCode, VerifyCodeType type) {
        VerificationDTO dto = verificationRepository.findVerifyCode(username, type);
        return !ObjectUtils.isEmpty(dto) && Objects.equals(dto.getVerifyCode(), verifyCode);
    }
}
