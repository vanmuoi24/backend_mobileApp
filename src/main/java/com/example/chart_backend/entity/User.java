package com.example.chart_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // ==== Thông tin đăng nhập ====
  @Column(nullable = false, unique = true)
  private String userEmail;

  @Column(nullable = false)
  private String userPassword;

  @Column(length = 100)
  private String userFullname;

  @Column(length = 15)
  private String userPhone;

  // ==== Thông tin cá nhân mở rộng ====
  private String bhxhNumber; // Mã số BHXH
  private String citizenId; // CCCD
  private LocalDate dateOfBirth; // Ngày sinh
  private String address; // Địa chỉ
  private String avatarUrl; // Ảnh đại diện

  // ==== Thông tin thẻ BHYT ====
  private String cardNumber; // Mã thẻ
  private LocalDate cardIssuedDate; // Ngày cấp
  private LocalDate cardExpiryDate; // Ngày hết hạn
  private String hospitalRegistered; // Nơi đăng ký khám chữa bệnh
  private String cardStatus; // ACTIVE / EXPIRED

  // // Quan hệ 1-nhiều với Participation
  // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  // private List<Participation> participations;
}
