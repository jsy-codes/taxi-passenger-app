package com.EONET.eonet.service;

import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.repository.TaxiPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxiPostService {

    private final TaxiPostRepository taxiPostRepository;

    public TaxiPost createPost(TaxiPost post) {
        return taxiPostRepository.save(post);
    }

    public List<TaxiPost> getAllPosts() {
        return taxiPostRepository.findAll();
    }
}