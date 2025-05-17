package com.EONET.eonet.service;

import com.EONET.eonet.domain.Member;
import com.EONET.eonet.domain.TaxiPost;
import com.EONET.eonet.repository.TaxiPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxiPostService {

    private final TaxiPostRepository taxiPostRepository;

    public TaxiPost getPostById(Long id) {
        TaxiPost post = taxiPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        checkAndUpdateStatus(post);

        return post;
    }

    public TaxiPost createPost(TaxiPost post) {
        return taxiPostRepository.save(post);
    }

    public List<TaxiPost> getAllPosts() {
        return taxiPostRepository.findAll();
    }

    public TaxiPost updatePost(Long id, TaxiPost updatedPost) {
        TaxiPost post = getPostById(id);

        post.setDeparture(updatedPost.getDeparture());
        post.setDestination(updatedPost.getDestination());
        post.setDepartureTime(updatedPost.getDepartureTime());
        post.setExpectedFare(updatedPost.getExpectedFare());
        post.setExpectedTime(updatedPost.getExpectedTime());
        post.setMaxPeople(updatedPost.getMaxPeople());
        post.setStatus(updatedPost.getStatus());

        return taxiPostRepository.save(post);
    }

    public String joinPost(Long postId, Member currentUser) {
        TaxiPost post = getPostById(postId);

        if (post.getStatus() == TaxiPost.Status.마감) {
            throw new IllegalStateException("이미 마감된 게시글입니다.");
        }

        if (post.getParticipants().contains(currentUser)) {
            throw new IllegalStateException("이미 참가한 사용자입니다.");
        }

        post.getParticipants().add(currentUser);
        post.setCurrentPeople(post.getCurrentPeople() + 1);

        if (post.getCurrentPeople() >= post.getMaxPeople()) {
            post.setStatus(TaxiPost.Status.마감);
        }

        taxiPostRepository.save(post);
        return "참가 완료";
    }

    public void checkAndUpdateStatus(TaxiPost post) {
        // 상태가 모집중이고, 출발 시간이 현재보다 이전이면 마감 처리
        if (post.getStatus() == TaxiPost.Status.모집중 &&
                post.getDepartureTime() != null &&
                post.getDepartureTime().isBefore(LocalDateTime.now())) {

            post.setStatus(TaxiPost.Status.마감);
            taxiPostRepository.save(post);
        }
    }

    public TaxiPost closePost(Long id) {
        TaxiPost post = getPostById(id);

        if (post.getStatus() == TaxiPost.Status.마감) {
            throw new IllegalStateException("이미 마감된 게시글입니다.");
        }

        post.setStatus(TaxiPost.Status.마감);
        return taxiPostRepository.save(post);
    }

    public int getParticipantCount(Long postId) {
        TaxiPost post = getPostById(postId);
        return post.getParticipants().size();
    }


}