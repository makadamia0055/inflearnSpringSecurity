package com.cos.security1.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN
    @CreationTimestamp
    private Timestamp createDate;

    private String provider; // 어디를 통해 로그인 했는지 : 구글, 카카오 등
    private String providerId; // 해당 프로바이더에서 제공하는 ID 값
}