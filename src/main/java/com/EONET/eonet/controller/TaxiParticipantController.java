package com.EONET.eonet.controller;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participants")
public class TaxiParticipantController {

    private final TaxiParticipantService participantService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestParam Long postId, @RequestParam Long memberId) {
        participantService.joinTaxiPost(postId, memberId);
        return ResponseEntity.ok("참여 완료");
    }
}
