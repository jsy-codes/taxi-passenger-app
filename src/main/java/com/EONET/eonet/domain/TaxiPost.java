package com.EONET.eonet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxiPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    private String departure;          // 출발지 주소
    private String destination;        // 목적지
    private LocalDateTime departureTime; // 출발 시간
    private Integer expectedFare;      // 예상 금액
    private String expectedTime;       // 도착 예상 시간
    private Integer maxPeople = 4;     // 최대 인원 (기본 4)

    @Enumerated(EnumType.STRING)
    private Status status;             // 모집 상태

    public enum Status {
        모집중, 마감
    }

    @ManyToMany
    @JoinTable(
            name = "taxi_post_participants",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> participants = new ArrayList<>();

    private int currentPeople = 1;
}