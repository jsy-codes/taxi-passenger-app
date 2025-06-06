package com.EONET.eonet.domain;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TaxiParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "taxi_post_id",nullable = false)
    private TaxiPost post;

    @ManyToOne
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    public TaxiParticipant() {}

}
