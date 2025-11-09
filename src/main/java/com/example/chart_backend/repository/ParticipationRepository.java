package com.example.chart_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.chart_backend.entity.Participation;
import com.example.chart_backend.entity.User;
import com.example.chart_backend.entity.Participation.InsuranceType;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
  // Lấy theo User
    List<Participation> findByUser(User user);

    // Lấy theo User và InsuranceType
    List<Participation> findByUserAndInsuranceType(User user, InsuranceType insuranceType);
}