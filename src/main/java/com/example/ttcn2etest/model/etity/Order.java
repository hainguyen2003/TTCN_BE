package com.example.ttcn2etest.model.etity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "`order`", schema = "edustar")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 20)
    @Column(name = "order_id", nullable = false, length = 20)
    private String orderId;


    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "service_manager_id", nullable = false)
    private Long serviceManagerId;

    @Column(name = "amount", nullable = false, length = 100)
    private String amount;

    @Size(max = 50)
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @ColumnDefault("'PENDING'")
    @Lob
    @Column(name = "status")
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "payment_date")
    private Instant paymentDate;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "image")
    private String image;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

}