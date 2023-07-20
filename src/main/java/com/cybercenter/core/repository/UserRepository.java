package com.cybercenter.core.repository;

import com.cybercenter.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findFirstByEmail(String email);
    User findFirstByPhoneNumber(String email);

}
