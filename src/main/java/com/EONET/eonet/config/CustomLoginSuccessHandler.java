package com.EONET.eonet.config;

import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.status.CardStatus;
import com.EONET.eonet.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException{
        String username = authentication.getName();
        Member member = memberService.findByUsername(username);

        if (member.getCardStatus() == CardStatus.NOT_REGISTERED) {
            response.sendRedirect("/cardRegister");
        } else {
            response.sendRedirect("/postList");
        }
    }
}
