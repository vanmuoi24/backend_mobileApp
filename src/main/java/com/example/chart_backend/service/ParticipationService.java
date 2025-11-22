package com.example.chart_backend.service;

import com.example.chart_backend.dto.request.CreateParticipationRequest;
import com.example.chart_backend.dto.request.ParticipationRequest;
import com.example.chart_backend.entity.Participation;
import com.example.chart_backend.entity.User;
import com.example.chart_backend.entity.Participation.InsuranceType;
import com.example.chart_backend.repository.ParticipationRepository;
import com.example.chart_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

	public Participation createParticipation(CreateParticipationRequest request) {
		if (request.getUserId() == null) {
			throw new IllegalArgumentException("userId is required");
		}
		if (request.getInsuranceType() == null) {
			throw new IllegalArgumentException("insuranceType is required");
		}

		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("User not found with id " + request.getUserId()));

		Participation participation = new Participation();
		participation.setUser(user);
		participation.setInsuranceType(request.getInsuranceType());
		participation.setStartDate(request.getStartDate());
		participation.setEndDate(request.getEndDate());
		participation.setCompanyName(request.getCompanyName());
		participation.setWorkplaceAddress(request.getWorkplaceAddress());
		participation.setPosition(request.getPosition());
		participation.setCurrency(request.getCurrency());
		participation.setSalary(request.getSalary());
		participation.setInsuranceSalary(request.getInsuranceSalary());
		participation.setTotalTime(request.getTotalTime());
		participation.setDelayedTime(request.getDelayedTime());

		return participationRepository.save(participation);
	}

	public Participation updateParticipation(Long id, ParticipationRequest req) {
        Participation participation = participationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participation not found with id " + id));

        // Lấy User từ userId
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + req.getUserId()));
        participation.setUser(user);

        // Map insuranceType (enum)
        if (req.getInsuranceType() != null) {
            participation.setInsuranceType(Participation.InsuranceType.valueOf(req.getInsuranceType()));
        }

        // Map các field còn lại
        if (req.getStartDate() != null) {
            participation.setStartDate(LocalDate.parse(req.getStartDate()));
        }
        if (req.getEndDate() != null) {
            participation.setEndDate(LocalDate.parse(req.getEndDate()));
        }

        participation.setCompanyName(req.getCompanyName());
        participation.setWorkplaceAddress(req.getWorkplaceAddress());
        participation.setPosition(req.getPosition());
        participation.setCurrency(req.getCurrency());
        participation.setSalary(req.getSalary());
        participation.setInsuranceSalary(req.getInsuranceSalary());
        participation.setTotalTime(req.getTotalTime());
        participation.setDelayedTime(req.getDelayedTime());

        return participationRepository.save(participation);
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
