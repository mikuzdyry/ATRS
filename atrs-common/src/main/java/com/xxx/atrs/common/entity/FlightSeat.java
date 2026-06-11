package com.xxx.atrs.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xxx.atrs.common.constant.SeatClass;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "atrs_flight_seat", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"flight_id", "seat_class"})
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_class", nullable = false, length = 20)
    private SeatClass seatClass;

    @Column(nullable = false)
    private Integer seatCount;

    @Column(nullable = false)
    @Builder.Default
    private Integer bookedCount = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Version
    @Column
    @Builder.Default
    private Integer version = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Transient
    public int getAvailableSeats() {
        return seatCount - bookedCount;
    }
}
