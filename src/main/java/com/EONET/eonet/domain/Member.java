package com.EONET.eonet.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter @Setter
public class Member implements UserDetails {

    @Id
    @Column(name = "member_id")
    private String id;

    @Column(nullable = false)
    private String password; // 추후 암호화 적용

    private String studentId;
    @Column(nullable = false, unique = true)
    private String email; // 동아대 이메일

    private String cardNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 현재 권한 없음
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

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
