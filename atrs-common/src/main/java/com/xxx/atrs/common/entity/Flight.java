package com.xxx.atrs.common.entity;

import com.xxx.atrs.common.constant.FlightStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "atrs_flight", indexes = {
        @Index(name = "idx_city_date", columnList = "departureCity, arrivalCity, flightDate"),
        @Index(name = "idx_date", columnList = "flightDate")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String flightNo;

    @Column(nullable = false, length = 50)
    private String airline;

    @Column(nullable = false, length = 50)
    private String departureCity;

    @Column(nullable = false, length = 50)
    private String arrivalCity;

    @Column(nullable = false, length = 100)
    private String departureAirport;

    @Column(nullable = false, length = 100)
    private String arrivalAirport;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private LocalDate flightDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;

    @JsonIgnoreProperties("flight")
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<FlightSeat> seats = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = FlightStatus.SCHEDULED;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
