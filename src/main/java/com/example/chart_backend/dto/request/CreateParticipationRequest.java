package com.example.chart_backend.dto.request;

import java.time.LocalDate;

import com.example.chart_backend.entity.Participation.InsuranceType;

public class CreateParticipationRequest {
	private Long userId;
	private InsuranceType insuranceType;
	private LocalDate startDate;
	private LocalDate endDate;
	private String companyName;
	private String workplaceAddress;
	private String position;
	private String currency;
	private Double salary;
	private Double insuranceSalary;
	private Integer totalTime;
	private Integer delayedTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public InsuranceType getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(InsuranceType insuranceType) {
		this.insuranceType = insuranceType;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getWorkplaceAddress() {
		return workplaceAddress;
	}

	public void setWorkplaceAddress(String workplaceAddress) {
		this.workplaceAddress = workplaceAddress;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Double getInsuranceSalary() {
		return insuranceSalary;
	}

	public void setInsuranceSalary(Double insuranceSalary) {
		this.insuranceSalary = insuranceSalary;
	}

	public Integer getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}

	public Integer getDelayedTime() {
		return delayedTime;
	}

	public void setDelayedTime(Integer delayedTime) {
		this.delayedTime = delayedTime;
	}
}


