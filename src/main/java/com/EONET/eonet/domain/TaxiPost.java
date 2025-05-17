package com.EONET.eonet.domain;
// 1. TaxiPost Entity (src/main/java/com/example/project/domain/TaxiPost.java)
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TaxiPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private Integer expectedFare;
    private String expectedTime;
    private Integer maxPeople = 4;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.RECRUITING;

    // Constructors
    public TaxiPost() {}

    public TaxiPost(Member writer, String departure, String destination,
                    LocalDateTime departureTime, Integer expectedFare,
                    String expectedTime) {
        this.writer = writer;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.expectedFare = expectedFare;
        this.expectedTime = expectedTime;
    }


