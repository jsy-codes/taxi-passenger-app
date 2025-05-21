package com.EONET.eonet.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class TaxiPostDto {
    private Long id;
    private String writerId;
    private String departure;
    private String destination;
    private String departureTime;

    // 출발지: 좌표로 저장
    private Double departureLat;
    private Double departureLon;

    // 기존 그대로
    private Integer expectedFare;
    private String expectedTime;
    private Integer maxPeople;
    private String status;
}