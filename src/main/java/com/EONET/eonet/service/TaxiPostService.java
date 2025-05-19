package com.EONET.eonet.service;

import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.repository.TaxiPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaxiPostService {

    private final TaxiPostRepository taxiPostRepository;

    public TaxiPost getPostById(Long id) {
        return taxiPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    }

    // 추가적인 기능(글쓰기, 수정, 삭제 등)도 여기서 작성 가능
}