package com.lul.payment.application.dto.mapper;

import com.lul.payment.application.dto.response.PaymentDTO;
import com.lul.payment.domain.payment.aggregate.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentDtoMapper {

    PaymentDTO toDto(Payment payment);
}
