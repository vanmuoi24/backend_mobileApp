package com.example.chart_backend.service;

import com.example.chart_backend.entity.Participation;
import com.example.chart_backend.entity.User;
import com.example.chart_backend.entity.Participation.InsuranceType;
import com.example.chart_backend.repository.ParticipationRepository;
import com.example.chart_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipationService {

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Participation> getAllParticipations() {
        return participationRepository.findAll();
    }

    public Optional<Participation> getParticipationById(Long id) {
        return participationRepository.findById(id);
    }

    public Participation createParticipation(Participation participation) {
        return participationRepository.save(participation);
    }

    public Participation updateParticipation(Long id, Participation updatedParticipation) {
        return participationRepository.findById(id)
                .map(participation -> {
                    participation.setUser(updatedParticipation.getUser());
                    participation.setInsuranceType(updatedParticipation.getInsuranceType());
                    participation.setStartDate(updatedParticipation.getStartDate());
                    participation.setEndDate(updatedParticipation.getEndDate());
                    participation.setCompanyName(updatedParticipation.getCompanyName());
                    participation.setWorkplaceAddress(updatedParticipation.getWorkplaceAddress());
                    participation.setPosition(updatedParticipation.getPosition());
                    participation.setCurrency(updatedParticipation.getCurrency());
                    participation.setSalary(updatedParticipation.getSalary());
                    participation.setInsuranceSalary(updatedParticipation.getInsuranceSalary());
                    participation.setTotalTime(updatedParticipation.getTotalTime());
                    participation.setDelayedTime(updatedParticipation.getDelayedTime());
                    return participationRepository.save(participation);
                })
                .orElseThrow(() -> new RuntimeException("Participation not found with id " + id));
    }

    public void deleteParticipation(Long id) {
        participationRepository.deleteById(id);
    }

    public List<Participation> getParticipationsByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(participationRepository::findByUser).orElse(List.of());
    }

    // Lấy theo userId và InsuranceType
    public List<Participation> getParticipationsByUserIdAndInsuranceType(Long userId, InsuranceType insuranceType) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(u -> participationRepository.findByUserAndInsuranceType(u, insuranceType))
                   .orElse(List.of());
    }
}
