package com.lul.booking.infrastructure.client;

import com.lul.booking.infrastructure.client.config.FeignClientConfig;
import com.lul.booking.infrastructure.client.dto.CreatePaymentRequest;
import com.lul.booking.infrastructure.client.dto.PaymentDTO;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for payment-service REST API
 */
@FeignClient(
        name = "payment-service",
        configuration = FeignClientConfig.class
)
public interface PaymentServiceClient {

    /**
     * Create payment (Saga Step 3)
     * Returns PaymentDTO on success, throws BusinessException on error
     */
    @PostMapping("/payments")
    @Retry(name = "paymentService")
    PaymentDTO createPayment(@RequestBody CreatePaymentRequest request);
}
