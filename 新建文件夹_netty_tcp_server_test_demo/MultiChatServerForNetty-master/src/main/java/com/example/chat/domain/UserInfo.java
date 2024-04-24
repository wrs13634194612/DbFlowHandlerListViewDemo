package com.example.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserInfo {
    private long userNumber;
    @Column(nullable = false, length = 30)
    private String userName;
    @Id
    @Column(nullable = false, length = 30)
    private String userId;
    @Column(nullable = false, length = 32)
    private String password;
    private LocalDateTime createTime;

}
