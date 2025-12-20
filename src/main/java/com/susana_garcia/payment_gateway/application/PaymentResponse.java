package com.susana_garcia.payment_gateway.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.susana_garcia.payment_gateway.domain.PaymentMethod;
import com.susana_garcia.payment_gateway.domain.TransactionStatus;

public record PaymentResponse(

        Long id,

        @JsonProperty("status")
        TransactionStatus status, //Feito o Enum

        @JsonProperty("payment_method")
        PaymentMethod method,

        @JsonProperty("transaction_amount")
        AmountResponse amount_response,

        CustomerResponse customer

) {

    ///  --- Inner Records definidos aqui dentro ----
    public record AmountResponse(Long value, String currency) {
    }
    public record CustomerResponse(String name, String address){
    }
}
