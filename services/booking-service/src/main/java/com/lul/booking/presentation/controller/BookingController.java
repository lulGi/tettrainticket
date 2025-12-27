package com.lul.booking.presentation.controller;

import com.lul.booking.application.dto.request.CreateBookingRequest;
import com.lul.booking.application.dto.response.BookingDTO;
import com.lul.booking.application.service.BookingApplicationService;
import com.lul.booking.application.service.BookingSagaService;
import com.lul.common.core.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingApplicationService bookingService;
    private final BookingSagaService sagaService;

    /**
     * Create new booking (starts Saga orchestration)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        BookingDTO booking = bookingService.createBooking(request, userId);
        return ResponseEntity.ok(
                ApiResponse.success(booking, "Booking created successfully. Waiting for payment confirmation.")
        );
    }

    /**
     * Get booking by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingDTO>> getBooking(@PathVariable String id) {
        BookingDTO booking = bookingService.getBooking(id);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    /**
     * Get current user's bookings
     */
    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getMyBookings(Authentication authentication) {
        String userId = authentication.getName();
        List<BookingDTO> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    /**
     * Cancel booking
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(
            @PathVariable String id,
            @RequestParam(required = false) String reason
    ) {
        sagaService.cancelBooking(id, reason != null ? reason : "User requested cancellation");
        return ResponseEntity.ok(ApiResponse.success(null, "Booking cancelled successfully"));
    }
}