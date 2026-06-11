package com.xxx.atrs.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "atrs_order_passenger")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPassenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private BookingOrder order;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 18)
    private String idCard;

    @Column(length = 20)
    private String phone;

    @Column(unique = true, length = 50)
    private String ticketNo;

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
}
