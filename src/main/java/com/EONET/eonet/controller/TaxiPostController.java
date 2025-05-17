package com.EONET.eonet.controller;

import com.EONET.eonet.domain.Member;
import com.EONET.eonet.service.MemberService;
import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.service.TaxiPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class TaxiPostController {

    private final TaxiPostService taxiPostService;
    private final MemberService memberService;

    @PostMapping
    public TaxiPost createPost(@RequestBody TaxiPost post) {
        return taxiPostService.createPost(post);
    }

    @GetMapping
    public List<TaxiPost> getAllPosts() {
        return taxiPostService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            TaxiPost post = taxiPostService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody TaxiPost updatedPost) {
        try {
            TaxiPost updated = taxiPostService.updatePost(id, updatedPost);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinTaxiPost(@PathVariable Long id,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Member member = memberService.findByUsername(userDetails.getUsername());
            String result = taxiPostService.joinPost(id, member);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<?> closePost(@PathVariable Long id) {
        try {
            TaxiPost closed = taxiPostService.closePost(id);
            return ResponseEntity.ok(closed);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/participants/count")
    public ResponseEntity<?> getParticipantCount(@PathVariable Long id) {
        try {
            int count = taxiPostService.getParticipantCount(id);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public String test() {
        return "TaxiPostController is alive!";
    }
}