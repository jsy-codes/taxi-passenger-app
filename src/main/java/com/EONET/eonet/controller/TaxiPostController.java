package com.EONET.eonet.controller;

//
//import com.example.project.domain.Member;
//import com.example.project.domain.TaxiPost;
//import com.example.project.dto.TaxiPostDto;
//import com.example.project.repository.TaxiPostRepository;
//import com.example.project.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.dto.TaxiPostDto;
import com.EONET.eonet.repository.MemberRepository;
import com.EONET.eonet.repository.TaxiPostRepository;
import com.EONET.eonet.service.CommentService;
import com.EONET.eonet.service.MemberService;
import com.EONET.eonet.service.TaxiPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final TaxiPostService taxiPostService;
    private final CommentService commentService;

    @RequestMapping("/postList")
    public String postList(Model model) {
        log.info("post controller");

        // 현재 로그인한 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            Member member = memberService.findByUsername(authentication.getName()); // 사용자 정보 조회
            model.addAttribute("member", member); // 모델에 추가
        }

        List<TaxiPost> posts = taxiPostRepository.findAll();
        model.addAttribute("posts", posts);

        return "post/postList";
    }
   /* public TaxiPostController(TaxiPostRepository taxiPostRepository, MemberRepository memberRepository) {
        this.taxiPostRepository = taxiPostRepository;
        this.memberRepository = memberRepository;
    }*/

    @GetMapping("/{id}")
    public String getPostDetail(@PathVariable Long id, Model model) {
        // 1) 게시글(포스트) 가져오기
        TaxiPost post = taxiPostService.getPostById(id);
        model.addAttribute("post", post);

        // 2) 현재 로그인한 사용자 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            Member loginMember = memberService.findByUsername(auth.getName());
            // 3) 작성자(member)와 로그인 사용자 비교
            boolean isOwner = loginMember.getId().equals(post.getWriter().getId());
            model.addAttribute("isOwner", isOwner);
            //  로그인 사용자 정보를 뷰에 직접 넘기고 싶으면
            model.addAttribute("loginMember", loginMember);
        } else {
            // 비로그인 상태면 무조건 false
            model.addAttribute("isOwner", false);
        }
        // 참여자 명단 필터링 (Member.participant가 이 post의 ID와 일치하는 사용자만)
        List<Member> participants = memberRepository.findAll().stream()
                .filter(m -> m.getParticipant() != null && m.getParticipant().equals(String.valueOf(id)))
                .collect(Collectors.toList());
        model.addAttribute("participants", participants);

        return "postDetail"; // resources/templates/postDetail.html
    }
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new SecurityException("로그인이 필요합니다.");
        }

        Member loginMember = memberService.findByUsername(auth.getName());

        List<Member> participants = memberRepository.findAll().stream()
                .filter(m -> id.toString().equals(m.getParticipant()))
                .toList();

        for (Member m : participants) {
            m.setParticipant(null);
            memberRepository.save(m);
        }


        taxiPostService.deletePost(id, loginMember.getId());

        return "redirect:/api/taxi-posts/postList";
    }

    @PostMapping("/cancel")
    @Transactional
    public String cancelParticipation() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(auth.getName());
        if (member.getParticipant() != null) {
            member.setParticipant(null); // 참여 상태 초기화
            memberRepository.save(member);
        }

        return "redirect:/api/taxi-posts/postList";
    }

    // Create a new post
    @PostMapping
    public String createPost(@ModelAttribute TaxiPostDto dto) {
        Member writer = memberRepository.findOptionalById(dto.getWriterId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime parsedTime = LocalDateTime.parse(dto.getDepartureTime(), formatter);
        int fare = taxiPostService.calculateFareFromTmap(
                dto.getDepartureLat(), dto.getDepartureLon(),
                dto.getDestinationLat(), dto.getDestinationLon()
        );

        TaxiPost post = new TaxiPost();
        post.setWriter(writer);
        post.setDestination(dto.getDestination());
        post.setDeparture(dto.getDeparture());
        post.setDepartureTime(parsedTime);
        post.setDestinationLat(dto.getDestinationLat());
        post.setDestinationLon(dto.getDestinationLon());
        post.setDepartureLat(dto.getDepartureLat());
        post.setDepartureLon(dto.getDepartureLon());
        post.setExpectedFare(fare);
        post.setExpectedTime(dto.getExpectedTime());


        taxiPostRepository.save(post);
        return "redirect:/api/taxi-posts/postList";
    }

    // List all posts
    @GetMapping
    public ResponseEntity<List<TaxiPostDto>> getAllPosts() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        List<TaxiPostDto> list = taxiPostRepository.findAll().stream()
                .map(post -> {
                    TaxiPostDto dto = new TaxiPostDto();
                    dto.setId(post.getId());
                   // dto.setWriterId(post.getWriter().getId());

                    dto.setDeparture(post.getDeparture());
                    dto.setDestination(post.getDestination());
                    dto.setDepartureTime(post.getDepartureTime().format(formatter));
                    dto.setExpectedFare(post.getExpectedFare());
                    dto.setExpectedTime(post.getExpectedTime());
                    dto.setMaxPeople(post.getMaxPeople());
                    dto.setStatus(post.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // 로그인한 사용자 ID 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            Member member = memberService.findByUsername(auth.getName());
            model.addAttribute("memberId", member.getId());// createPost.html로 넘김
            model.addAttribute("studentId", member.getStudentId());
        }

        return "post/createPost"; // templates/post/createPost.html로 렌더링
    }

    @PostMapping("/api/comments")
    public String saveComment(@RequestParam Long postId,
                              @RequestParam String content,
                              @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findByUsername(userDetails.getUsername());
        commentService.saveComment(postId, content, member);
        return "redirect:/api/taxi-posts/" + postId;
    }


    @PostMapping("/join")
    @Transactional
    public String joinPost(@RequestParam Long postId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }

        Member member = memberService.findByUsername(auth.getName());
        if (member.getParticipant() != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 참여중입니다.");
            return "redirect:/api/taxi-posts/" + postId;
        }

        // 참여자 수 확인
        List<Member> participants = memberRepository.findAll().stream()
                .filter(m -> postId.toString().equals(m.getParticipant()))
                .collect(Collectors.toList());

        if (participants.size() >= 4) {
            // 참여자 수 4명이상 못 받도록 하는 조건문
            redirectAttributes.addFlashAttribute("errorMessage", "참여 인원이 가득 찼습니다.");
            return "redirect:/api/taxi-posts/" + postId;
        }

        return "redirect:/api/taxi-posts/" + postId;
    }


}
