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

    public void deletePost(Long postId, String loginMemberId) {
        TaxiPost post = taxiPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        if (!post.getWriter().getId().equals(loginMemberId)) {
            throw new SecurityException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        taxiPostRepository.delete(post);
    }
    // 추가적인 기능(글쓰기, 수정, 삭제 등)도 여기서 작성 가능
}