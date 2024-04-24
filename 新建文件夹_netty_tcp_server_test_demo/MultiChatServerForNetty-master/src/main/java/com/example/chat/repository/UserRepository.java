package com.example.chat.repository;

import com.example.chat.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
    UserInfo findByUserId(String userId);
}
