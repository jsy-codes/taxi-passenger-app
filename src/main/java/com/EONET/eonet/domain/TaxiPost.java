package com.EONET.eonet.domain;
// 1. TaxiPost Entity (src/main/java/com/example/project/domain/TaxiPost.java)
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 출발지: 사용자가 입력한 텍스트 (선택적으로 사용 가능)
    private String departure;

    // 출발지: 지도에서 선택한 정확한 좌표
    private Double departureLat;
    private Double departureLon;

    // 도착지: 정확한 주소 (선택한 건물)
    private String destination;
    private Double destinationLat;
    private Double destinationLon;


    private LocalDateTime departureTime;
    private Integer expectedFare;
    private String expectedTime;
    private Integer maxPeople = 4;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.RECRUITING;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Constructors

    public TaxiPost() {
    }

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
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaxiParticipant> participants = new ArrayList<>();


}

