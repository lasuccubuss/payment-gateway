package com.susana_garcia.payment_gateway.domain;

public record Transaction(
        Long id,
        Long amount,
        String currency,
        PaymentMethod method,
        Customer customer
) {
}
