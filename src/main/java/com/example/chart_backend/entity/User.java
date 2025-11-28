package com.example.chart_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String userPassword;

  @Column(length = 100)
  private String userFullname;

  @Column(name = "user_email")
  private String userEmail;

  @Column(length = 15)
  private String userPhone;

  // ==== Thông tin cá nhân mở rộng ====
  private String bhxhNumber; // Mã số BHXH
  private String citizenId; // CCCD
  private LocalDate dateOfBirth; // Ngày sinh
  private String address; // Địa chỉ

  // Avatar: liên kết tới FileMgmt qua cột avatar_file_id
  @Column(name = "avatar_url")
  private String avatarUrl;

  // ==== Thông tin thẻ BHYT ====
  private String cardNumber; // Mã thẻ
  private LocalDate cardIssuedDate; // Ngày cấp
  private LocalDate cardExpiryDate; // Ngày hết hạn
  private String hospitalRegistered; // Nơi đăng ký khám chữa bệnh
  private String cardStatus; // ACTIVE / EXPIRED

  // Nếu sau này dùng Participation thì bật lại:
  // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval =
  // true)
  // private List<Participation> participations;
}
