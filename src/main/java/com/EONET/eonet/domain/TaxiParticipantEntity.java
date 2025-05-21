package com.EONET.eonet.domain;


@Entity
public class TaxiParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private TaxiPost post;

    private boolean isPaid = false;

    // getters, setters, constructor
}
