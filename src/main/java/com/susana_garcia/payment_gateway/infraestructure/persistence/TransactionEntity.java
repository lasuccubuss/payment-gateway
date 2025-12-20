package com.susana_garcia.payment_gateway.infraestructure.persistence;

import com.susana_garcia.payment_gateway.domain.PaymentMethod;
import com.susana_garcia.payment_gateway.domain.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    //Seguindo o conceito de Arquitetura Hexagonal: desacoplamento

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Long amount;
    private String currency;

    @Enumerated(EnumType.STRING) //vai salvar o PIX como texto no banco
    private PaymentMethod method;

    // Para simplificar, vamos achatar os dados do cliente aqui
    //em produção, teríamos que criar uma tabela separada para o cliente
    private String customerName;
    private String customerAddress;
}
