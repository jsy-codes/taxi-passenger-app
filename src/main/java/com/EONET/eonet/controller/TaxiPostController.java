package com.EONET.eonet.controller;

//
//import com.example.project.domain.Member;
//import com.example.project.domain.TaxiPost;
//import com.example.project.dto.TaxiPostDto;
//import com.example.project.repository.TaxiPostRepository;
//import com.example.project.repository.MemberRepository;
import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.dto.TaxiPostDto;
import com.EONET.eonet.repository.MemberRepository;
import com.EONET.eonet.repository.TaxiPostRepository;
import com.EONET.eonet.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/api/taxi-posts")
@RequiredArgsConstructor
public class TaxiPostController {

    private final TaxiPostRepository taxiPostRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    @RequestMapping("/postList")
    public String postList(Model model) {
        log.info("post controller");

        // 현재 로그인한 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            Member member = memberService.findByUsername(authentication.getName()); // 사용자 정보 조회
            model.addAttribute("member", member); // 모델에 추가
        }

        return "post/postList";
    }
   /* public TaxiPostController(TaxiPostRepository taxiPostRepository, MemberRepository memberRepository) {
        this.taxiPostRepository = taxiPostRepository;
        this.memberRepository = memberRepository;
    }*/

    // Create a new post
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody TaxiPostDto dto) {
        Member writer = memberRepository.findOptionalById(dto.getWriterId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        TaxiPost post = new TaxiPost(
                writer,
                dto.getDeparture(),
                dto.getDestination(),
                dto.getDepartureTime(),
                dto.getExpectedFare(),
                dto.getExpectedTime()
        );
        TaxiPost saved = taxiPostRepository.save(post);
        return ResponseEntity.created(URI.create("/api/taxi-posts/" + saved.getId())).build();
    }

    // List all posts
    @GetMapping
    public ResponseEntity<List<TaxiPostDto>> getAllPosts() {
        List<TaxiPostDto> list = taxiPostRepository.findAll().stream()
                .map(post -> {
                    TaxiPostDto dto = new TaxiPostDto();
                    dto.setId(post.getId());
                   // dto.setWriterId(post.getWriter().getId());
                    dto.setDeparture(post.getDeparture());
                    dto.setDestination(post.getDestination());
                    dto.setDepartureTime(post.getDepartureTime());
                    dto.setExpectedFare(post.getExpectedFare());
                    dto.setExpectedTime(post.getExpectedTime());
                    dto.setMaxPeople(post.getMaxPeople());
                    dto.setStatus(post.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

}
