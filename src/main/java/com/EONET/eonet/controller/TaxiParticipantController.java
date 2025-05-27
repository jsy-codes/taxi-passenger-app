package com.EONET.eonet.controller;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;


@RestController
@RequestMapping("/api/taxi-posts")
@RequiredArgsConstructor
public class TaxiPostController {

    private final TaxiPostRepository taxiPostRepository;
    private final MemberRepository memberRepository;
    private final TaxiParticipantRepository taxiParticipantRepository;

    @PostMapping("/{postId}/join")
    public ResponseEntity<String> joinPost(@PathVariable Long postId, @RequestParam Long memberId) {
        TaxiPost post = taxiPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        long currentCount = taxiParticipantRepository.countByPost(post);
        if (currentCount >= 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("참여 인원이 꽉 찼습니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        boolean alreadyJoined = taxiParticipantRepository.existsByPostAndMember(post, member);
        if (alreadyJoined) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 참여한 회원입니다.");
        }

        TaxiParticipant participant = new TaxiParticipant();
        participant.setPost(post);
        participant.setMember(member);
        taxiParticipantRepository.save(participant);

        return ResponseEntity.ok("참여 완료!");
    }
}
