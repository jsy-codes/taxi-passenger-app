package com.EONET.eonet.controller;

import com.EONET.eonet.domain.Member;
import com.EONET.eonet.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    /**
     * 로그인 페이지 반환
     */
    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login"; // login.html 템플릿 반환
    }

    /**
     * 현재 로그인한 사용자 정보 가져오기
     */
    public Member getLoggedInMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return memberService.findByUsername(authentication.getName()); // ID 기반으로 DB에서 조회
        }
        return null;
    }
}
