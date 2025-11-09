package com.example.chart_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private Long id;
    private String userFullname;
    private String userEmail;
    private String userPhone;

}
