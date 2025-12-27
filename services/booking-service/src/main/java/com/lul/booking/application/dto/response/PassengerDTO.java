package com.lul.booking.application.dto.response;

import lombok.Data;

@Data
public class PassengerDTO {
    private String fullName;
    private String identityNumber;
    private String phoneNumber;
}