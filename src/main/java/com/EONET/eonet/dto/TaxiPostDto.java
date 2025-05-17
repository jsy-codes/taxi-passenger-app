package com.EONET.eonet.dto;

import java.time.LocalDateTime;

public class TaxiPostDto {
    private Long id;
    private Long writerId;
    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private Integer expectedFare;
    private String expectedTime;
    private Integer maxPeople;
    private String status;

    // getters and setters
    // ...
}