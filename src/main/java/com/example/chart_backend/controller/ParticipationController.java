package com.example.chart_backend.controller;


import com.example.chart_backend.dto.request.CreateParticipationRequest;
import com.example.chart_backend.entity.Participation;
import com.example.chart_backend.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.chart_backend.dto.response.ApiResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1/participations")
public class ParticipationController {

	@Autowired
	private ParticipationService participationService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Participation>>> getAllParticipations() {
		List<Participation> list = participationService.getAllParticipations();
		return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách thành công", list));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Participation>> getParticipationById(@PathVariable Long id) {
		return participationService.getParticipationById(id)
				.map(participation -> ResponseEntity.ok(
						new ApiResponse<>(true, "Lấy chi tiết thành công", participation)))
				.orElse(ResponseEntity
						.ok(new ApiResponse<>(false, "Không tìm thấy Participation với id: " + id, null)));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Participation>> createParticipation(@RequestBody CreateParticipationRequest request) {
		Participation created = participationService.createParticipation(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Tạo Participation thành công", created));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Participation>> updateParticipation(@PathVariable Long id,
			@RequestBody Participation participation) {
		try {
			Participation updated = participationService.updateParticipation(id, participation);
			return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật Participation thành công", updated));
		} catch (RuntimeException e) {
			return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteParticipation(@PathVariable Long id) {
		participationService.deleteParticipation(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Xóa Participation thành công", null));
	}

	@GetMapping("/by-user/{userId}")
	public ResponseEntity<ApiResponse<List<Participation>>> getByUserId(@PathVariable Long userId) {
		List<Participation> list = participationService.getParticipationsByUserId(userId);
		return ResponseEntity.ok(new ApiResponse<>(true, "Lấy Participation theo userId thành công", list));
	}

	@GetMapping("/by-user/{userId}/type/{insuranceType}")
	public ResponseEntity<ApiResponse<List<Participation>>> getByUserIdAndInsuranceType(
			@PathVariable Long userId,
			@PathVariable Participation.InsuranceType insuranceType) {

		List<Participation> list = participationService.getParticipationsByUserIdAndInsuranceType(userId,
				insuranceType);
		return ResponseEntity
				.ok(new ApiResponse<>(true, "Lấy Participation theo userId và InsuranceType thành công", list));
	}
}
