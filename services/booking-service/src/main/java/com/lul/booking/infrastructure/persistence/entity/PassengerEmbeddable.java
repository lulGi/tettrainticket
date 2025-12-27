package com.lul.booking.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassengerEmbeddable {

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "identity_number", nullable = false, length = 12)
    private String identityNumber;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
}