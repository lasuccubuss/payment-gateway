package com.susana_garcia.payment_gateway.application;


import com.susana_garcia.payment_gateway.domain.TransactionNotFoundException;
import com.susana_garcia.payment_gateway.domain.TransactionStatus;
import com.susana_garcia.payment_gateway.domain.User;
import com.susana_garcia.payment_gateway.infraestructure.PaymentRequest;
import com.susana_garcia.payment_gateway.infraestructure.persistence.TransactionEntity;
import com.susana_garcia.payment_gateway.infraestructure.persistence.TransactionRepository;
import com.susana_garcia.payment_gateway.infraestructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;//variável declarada

    private final UserRepository userRepository;


    public PaymentResponse createTransaction(PaymentRequest request) {

        User sender = userRepository.findById(request.userId()).orElseThrow(
                () -> new RuntimeException("Usuário não encontrado"));

        //fazendo a verificação do dinheiro
        if(sender.getBalance().compareTo(BigDecimal.valueOf(request.amount().value())) < 0){
            throw new RuntimeException("Saldo insuficiente!");
        }

        //atualiza o valor do saldo
        BigDecimal novoSaldo =  BigDecimal.valueOf(request.amount().value());//corrigindo o valor
        sender.setBalance(sender.getBalance().subtract(novoSaldo));//subtraindo o valor

        //comunicando ao banco de dados o valor atualizado
        userRepository.save(sender);

        //--------------------- TUDO AQUI É TRANSAÇÃO PURA ----------------//
        //objeto de domínio para a Entidade
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(request.amount().value());
        transactionEntity.setStatus(TransactionStatus.CREATED);//definindo o Status com ENUM
        transactionEntity.setCurrency(request.amount().currency());
        transactionEntity.setMethod(request.paymentMethod());
        transactionEntity.setCustomerName(request.customer().name());
        transactionEntity.setCustomerAddress(request.customer().address());

        TransactionEntity save = transactionRepository.save(transactionEntity);

        return new PaymentResponse(
                save.getId(),
                save.getStatus(),
                save.getMethod(),
                new PaymentResponse.AmountResponse(save.getAmount(), save.getCurrency()),
                new PaymentResponse.CustomerResponse(save.getCustomerName(), save.getCustomerAddress())
        );

    }

    //---------------------- ISSO AQUI SÓ ENCONTRA O ID DO USUÁRIO -------------------//
    public PaymentResponse findById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(()-> new TransactionNotFoundException(""));

            return new PaymentResponse(
                    id,
                    transaction.getStatus(),
                    transaction.getMethod(),
                    new PaymentResponse.AmountResponse(transaction.getAmount(), transaction.getCurrency()),
                    new PaymentResponse.CustomerResponse(transaction.getCustomerName(), transaction.getCustomerAddress())
            );
        }

    }
