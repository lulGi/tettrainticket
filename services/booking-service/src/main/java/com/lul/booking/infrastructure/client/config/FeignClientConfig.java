package com.lul.booking.infrastructure.client.config;

import com.lul.common.core.exception.BusinessException;
import com.lul.common.core.exception.ErrorCode;
import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignClientConfig {

    /**
     * Feign logger level
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Custom error decoder for mapping HTTP errors to BusinessException
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    /**
     * Custom Error Decoder
     */
    public static class CustomErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            log.error("Feign error - Method: {}, Status: {}", methodKey, response.status());

            // Handle specific HTTP status codes
            switch (response.status()) {
                case 404:
                    return new BusinessException(
                            ErrorCode.NOT_FOUND,
                            "Resource not found"
                    );
                case 409:
                    // Conflict - used by reserveSeats when not enough seats
                    return new BusinessException(
                            ErrorCode.SEAT_NOT_AVAILABLE,
                            "Not enough seats available"
                    );
                case 503:
                    // Service unavailable - will be retried by Resilience4j
                    return new feign.FeignException.ServiceUnavailable(
                            "Service unavailable",
                            response.request(),
                            null,
                            null
                    );
                default:
                    return new BusinessException(
                            ErrorCode.EXTERNAL_SERVICE_ERROR,
                            "External service error: HTTP " + response.status()
                    );
            }
        }
    }
}
