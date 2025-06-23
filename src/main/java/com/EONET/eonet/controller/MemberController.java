package com.EONET.eonet.controller;

import com.EONET.eonet.service.MemberService;
import com.EONET.eonet.domain.Member;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
//    @PostMapping("/updateBalance")
//    public String updateBalance(@RequestParam("balance") int balance,
//                                @AuthenticationPrincipal UserDetails userDetails) {
//        // 현재 로그인된 사용자 가져오기
//        Member member = memberService.findByUsername(userDetails.getUsername());
//        memberService.updateBalance(member, balance);
//        return "redirect:/updateBalance"; // 업데이트 후 리디렉트
//    }

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        if (memberService.existsByStudentId(form.getStudentId())) {
            result.rejectValue("studentId", "duplicate", "이미 등록된 학번입니다.");
            return "members/createMemberForm";
        }
        Member member = new Member();
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        member.setPassword(encodedPassword);
        member.setId(form.getId());
        member.setStudentId(form.getStudentId());
        member.setEmail(form.getEmail());
        member.setCardNumber(form.getCardNumber());
        memberService.join(member);
        return "redirect:/login";
    }

//    @GetMapping("/updateBalance")
//    public String list(Model model) {
//        List<Member> members = memberService.findMembers();
//        model.addAttribute("members", members);
//        return "members/setting";
//    }
}