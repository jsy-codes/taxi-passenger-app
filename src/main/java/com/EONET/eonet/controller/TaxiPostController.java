package com.EONET.eonet.controller;

//
//import com.example.project.domain.Member;
//import com.example.project.domain.TaxiPost;
//import com.example.project.dto.TaxiPostDto;
//import com.example.project.repository.TaxiPostRepository;
//import com.example.project.repository.MemberRepository;
import com.EONET.eonet.dto.TaxiPostDto;
import com.EONET.eonet.repository.MemberRepository;
import com.EONET.eonet.repository.TaxiPostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/taxi-posts")
public class TaxiPostController {

    private final TaxiPostRepository taxiPostRepository;
    private final MemberRepository memberRepository;

    public TaxiPostController(TaxiPostRepository taxiPostRepository, MemberRepository memberRepository) {
        this.taxiPostRepository = taxiPostRepository;
        this.memberRepository = memberRepository;
    }

    // Create a new post
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody TaxiPostDto dto) {
        Member writer = memberRepository.findById(dto.getWriterId())
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
                    dto.setWriterId(post.getWriter().getId());
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
