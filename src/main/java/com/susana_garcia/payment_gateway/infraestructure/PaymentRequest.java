package com.susana_garcia.payment_gateway.infraestructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.susana_garcia.payment_gateway.domain.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(

        @NotNull
        @JsonProperty("order_id") Long orderId,

        @NotNull
        @JsonProperty("user_id") Long userId,

        @NotNull
        @JsonProperty("payment_method")PaymentMethod paymentMethod,

        @Valid
        @NotNull
        @JsonProperty("transaction_amount") AmountRequest amount,

        @Valid
        CustomerRequest customer
        ) {

    //Inner record ou Records internos para facilitar
    public record AmountRequest(
            @NotNull(message = "O valor é obrigatório")
            @Positive(message = "O valor deve ser maior que zero")
            Long value,

            @NotNull(message = "A moeda é obrigatória")
            String currency) {
    }

    //CustomerRequest é um inner record
    public record CustomerRequest(
            @NotBlank(message = "O nome é obrigatório")
            String name,

            @NotBlank(message = "O endereço é obrigatório")
            String address){
    }
}
