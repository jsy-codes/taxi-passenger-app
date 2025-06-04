package com.EONET.eonet.controller;

//
//import com.example.project.domain.Member;
//import com.example.project.domain.TaxiPost;
//import com.example.project.dto.TaxiPostDto;
//import com.example.project.repository.TaxiPostRepository;
//import com.example.project.repository.MemberRepository;
import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.TaxiParticipant;
import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.dto.TaxiPostDto;
import com.EONET.eonet.repository.MemberRepository;
import com.EONET.eonet.repository.TaxiPostRepository;
import com.EONET.eonet.service.MemberService;
import com.EONET.eonet.service.TaxiPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

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
        TaxiPost post = taxiPostService.getPostById(id);
        model.addAttribute("post", post);
        return "postDetail"; // postDetail.html로 이동
    }

    // Create a new post
    @PostMapping
    public String createPost(@ModelAttribute TaxiPostDto dto) {
        Member writer = memberRepository.findOptionalById(dto.getWriterId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime parsedTime = LocalDateTime.parse(dto.getDepartureTime(), formatter);

        TaxiPost post = new TaxiPost();
        post.setWriter(writer);
        post.setDestination(dto.getDestination());
        post.setDeparture(dto.getDeparture());
        post.setDepartureTime(parsedTime);
        post.setDestinationLat(dto.getDestinationLat());
        post.setDestinationLon(dto.getDestinationLon());
        post.setDepartureLat(dto.getDepartureLat());
        post.setDepartureLon(dto.getDepartureLon());
        post.setExpectedFare(dto.getExpectedFare());
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
            model.addAttribute("memberId", member.getId()); // createPost.html로 넘김
        }

        return "post/createPost"; // templates/post/createPost.html로 렌더링
    }

    @PostMapping("/{postId}/join")
    @ResponseBody
    public ResponseEntity<String> joinPost(
            @PathVariable Long postId,
            @RequestParam Long StudentId) {

        TaxiPost post = taxiPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        String studentIdStr = String.valueOf(StudentId);
        Member student = memberRepository.findOptionalById(studentIdStr)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        long currentCount = post.getParticipants().size();

        if (currentCount >= 4) {
            return ResponseEntity.badRequest().body("참여 인원이 가득 찼습니다.");
        }
        boolean alreadyJoined = post.getParticipants().stream()
                .anyMatch(p -> p.getMember().getId().equals(StudentId));
        if (alreadyJoined) {
            return ResponseEntity.badRequest().body("이미 참여하셨습니다.");
        }

        TaxiParticipant participant = new TaxiParticipant();
        participant.setTaxiPost(post);
        participant.setMember(student);

        post.getParticipants().add(participant);
        taxiPostRepository.save(post);

        return ResponseEntity.ok("참여가 완료되었습니다.");
    }
}
