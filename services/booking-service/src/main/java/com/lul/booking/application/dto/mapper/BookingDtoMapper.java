package com.lul.booking.application.dto.mapper;

import com.lul.booking.application.dto.request.PassengerRequest;
import com.lul.booking.application.dto.response.BookingDTO;
import com.lul.booking.application.dto.response.PassengerDTO;
import com.lul.booking.domain.booking.aggregate.Booking;
import com.lul.booking.domain.booking.valueobject.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingDtoMapper {

    @Mapping(target = "status", expression = "java(booking.getStatus().name())")
    BookingDTO toDTO(Booking booking);

    List<BookingDTO> toDTOs(List<Booking> bookings);

    PassengerDTO toDTO(Passenger passenger);

    default Passenger toDomain(PassengerRequest request) {
        return Passenger.create(
                request.getFullName(),
                request.getIdentityNumber(),
                request.getPhoneNumber()
        );
    }

    default List<Passenger> toDomainList(List<PassengerRequest> requests) {
        return requests.stream()
                .map(this::toDomain)
                .toList();
    }
}