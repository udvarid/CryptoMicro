package com.donat.crypto.user.repository;

import javax.transaction.Transactional;

import java.util.Optional;

import com.donat.crypto.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);


}