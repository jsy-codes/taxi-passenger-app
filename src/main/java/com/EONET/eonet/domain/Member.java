package com.EONET.eonet.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter @Setter
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    @Column(nullable = false)
    private String password; // 추후 암호화 적용

    private String studentId;
    @Column(nullable = false, unique = true)
    private String email; // 동아대 이메일
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    // 추후 역할/권한 추가 시 필요
    // @Enumerated(EnumType.STRING)
    // private Role role;
}
