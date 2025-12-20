package com.susana_garcia.payment_gateway.domain;

public record Customer(
        String name,
        String address
        //Futuramente aqui entra CPF, Email, etc.
) {
}
