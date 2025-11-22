package com.example.chart_backend.dto.request;

// package com.example.chart_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipationRequest {
    private Long userId;
    private String insuranceType;

    private String startDate;
    private String endDate;

    private String companyName;
    private String workplaceAddress;
    private String position;
    private String currency;
    private Double salary;
    private Double insuranceSalary;
    private Integer totalTime;
    private Integer delayedTime;
}
