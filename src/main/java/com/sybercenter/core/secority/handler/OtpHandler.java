package com.sybercenter.core.secority.handler;

import com.sybercenter.core.secority.Repository.VerificationRepository;
import com.sybercenter.core.secority.dto.VerificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class OtpHandler {

    private final VerificationRepository verificationRepository;

    /**
     * Method for generating OTP and put it in cache.
     *
     * @param username - cache key
     * @return cache value (generated OTP number)
     */
    public Integer generateOTP(String username) {
        VerificationDTO verifyOtp = verificationRepository.findVerifyOtp(username);
        if (ObjectUtils.isEmpty(verifyOtp)) {
            log.debug("Try to generate new otp for: {}", username);
            Random random = new Random();
            int otp = 100000 + random.nextInt(900000);
            createNewVerificationDTO(username, otp);
            return otp;
        } else
            return verifyOtp.getOtp();
    }

    /**
     * Method for create VerificationDTO and put it in cache.
     *
     * @param username - cache key
     * @param otp      - (generateOTP)
     */
    private void createNewVerificationDTO(String username, int otp) {
        log.debug("Try to add new otp record for: {}", username);
        VerificationDTO verificationDTO = VerificationDTO.builder()
                .otp(otp)
                .username(username)
                .build();
        verificationRepository.addVerifyOtp(verificationDTO);
    }

    public boolean validateOTP(String username, Integer otp) {
        VerificationDTO verifyOtp = verificationRepository.findVerifyOtp(username);
        return !ObjectUtils.isEmpty(verifyOtp) && Objects.equals(verifyOtp.getOtp(), otp);
    }
}
