package com.EONET.eonet.controller;

import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.status.CardStatus;
import com.EONET.eonet.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardController {

    @GetMapping("/cardRegister")
    public String cardRegisterForm() {
        return "card/cardRegister"; // templates/cardRegister.html 을 보여줌
    }
}
