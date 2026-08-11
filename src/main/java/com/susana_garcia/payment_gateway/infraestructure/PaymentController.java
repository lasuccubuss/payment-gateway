package com.susana_garcia.payment_gateway.infraestructure;

// Note que NÃO precisamos importar PaymentRequest, pois ele está no mesmo pacote!

import com.susana_garcia.payment_gateway.application.PaymentResponse;
import com.susana_garcia.payment_gateway.application.PaymentService;
import com.susana_garcia.payment_gateway.domain.Transaction;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController { // Nome corrigido com 't'

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> processPayment(@Valid @RequestBody PaymentRequest request){
        paymentService.createTransaction(request);
        return ResponseEntity.ok("Pagamento recebido!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id){
        PaymentResponse response = paymentService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {
        return ResponseEntity.ok(paymentService.getTransactionHistory(1L));
    }

    @GetMapping("/user/{userId}/history")
    public ResponseEntity<List<PaymentResponse>> getHistory(@PathVariable Long userId) {

        // 1. Chama o nosso motor que acabamos de construir
        List<PaymentResponse> history = paymentService.getTransactionHistory(userId);

        // 2. Devolve a lista com o Status 200 (OK)
        return ResponseEntity.ok(history);
    }
}