package com.EONET.eonet.service;

import com.EONET.eonet.domain.Comment;
import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.repository.CommentRepository;
import com.EONET.eonet.repository.TaxiPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaxiPostRepository taxiPostRepository;

    public void saveComment(Long postId, String content, Member author) {
        TaxiPost post = taxiPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setCreatedAt(java.time.LocalDateTime.now());

        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
