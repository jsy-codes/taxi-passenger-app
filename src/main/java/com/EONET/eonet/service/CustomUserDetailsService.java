package com.EONET.eonet.service;


import com.EONET.eonet.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.findByUsername(username);
        System.out.println("로그인 시도: " + username+ " member: "+member.getPassword());
        if (member == null) {

            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .build();
    }

}