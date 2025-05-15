package com.EONET.eonet.controller;

import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.service.TaxiPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class TaxiPostController {

    private final TaxiPostService taxiPostService;

    @PostMapping
    public TaxiPost createPost(@RequestBody TaxiPost post) {
        return taxiPostService.createPost(post);
    }

    @GetMapping
    public List<TaxiPost> getAllPosts() {
        return taxiPostService.getAllPosts();
    }
}