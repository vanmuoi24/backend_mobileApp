package com.example.chart_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "participations")
@Getter
@Setter
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết tới User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private InsuranceType insuranceType; // Loại bảo hiểm: BHXH, BHTN, BHYT, BHTNLD_BNN

    private LocalDate startDate; // Từ tháng
    private LocalDate endDate; // Đến tháng

    @Column(length = 255)
    private String companyName; // Đơn vị công tác

    @Column(length = 255)
    private String workplaceAddress; // Địa chỉ làm việc

    @Column(length = 100)
    private String position; // Nghề nghiệp / Chức vụ

    @Column(length = 10)
    private String currency; // Loại tiền (VD: VND)

    private Double salary; // Mức lương
    private Double insuranceSalary; // Tiền lương đóng BH

    private Integer totalTime; // Tổng thời gian tham gia (tháng)
    private Integer delayedTime; // Thời gian chậm đóng (tháng)

    public enum InsuranceType {
        BHXH, BHTN, BHYT, BHTNLD_BNN
    }
}
