package com.susana_garcia.payment_gateway.domain;

import com.susana_garcia.payment_gateway.application.PaymentResponse;
import com.susana_garcia.payment_gateway.application.PaymentService;
import com.susana_garcia.payment_gateway.infraestructure.PaymentRequest;
import com.susana_garcia.payment_gateway.infraestructure.persistence.TransactionEntity;
import com.susana_garcia.payment_gateway.infraestructure.persistence.TransactionRepository;
import com.susana_garcia.payment_gateway.infraestructure.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // JUnit 5
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// 1. Dizemos ao JUnit para usar a extensão do Mockito (o motor dos dublês)
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    // 2. O Dublê (@Mock): Criamos um Repositório de mentira.
    // Ele não conecta no banco, ele só finge.
    @Mock
    private TransactionRepository repository;

    @Mock
    private UserRepository userRepository;

    // 3. O Protagonista (@InjectMocks): O Service que vamos testar.
    // O Mockito é esperto: ele cria o Service e INJETA o repositório fake dentro dele automaticamente.
    @InjectMocks
    private PaymentService paymentService;


    // Aqui virão os testes...
    @Test
    void shouldCreateTransactionSuccessfully(){
        PaymentRequest request = new PaymentRequest(
                17L,
                1L,
                PaymentMethod.PIX, //objeto específico
                new PaymentRequest.AmountRequest(1000L, "BRL"), //objeto específico
                new PaymentRequest.CustomerRequest("Nome", "Endereço")
        );

        //transação de usuário rico
        User richUser = new User();
        richUser.setId(1L);
        richUser.setBalance(new BigDecimal(2000L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(richUser));

        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setAmount(request.amount().value());
        transactionEntity.setMethod(request.paymentMethod());
        transactionEntity.setCustomerName(request.customer().name());
        transactionEntity.setId(1L);
        transactionEntity.setStatus(TransactionStatus.CREATED);

        when(repository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        PaymentResponse response = paymentService.createTransaction(request);

        //VERIFICAÇÃO DO JUNIT
        assertNotNull(response);//garantindo que a resposta não vem vazia
        assertEquals(TransactionStatus.CREATED, response.status());//garantindo que o Status veio igual ao Enum CREATED
        assertEquals(1L, response.id());//id do PaymentResponse

    }

    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient(){
        User sender = new  User();
        sender.setId(1L);
        sender.setBalance(BigDecimal.TEN);

        PaymentRequest request = new PaymentRequest(
                1L,
                1L,
                PaymentMethod.PIX,
                new PaymentRequest.AmountRequest(1000L, "BRL"), //objeto específico
                new PaymentRequest.CustomerRequest(null, null)
        );

        //buscando por um usuário específico
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        Exception erro = assertThrows(RuntimeException.class, () -> paymentService.createTransaction(request));
        assertEquals("Saldo insuficiente!", erro.getMessage());
    }
}