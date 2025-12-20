package com.susana_garcia.payment_gateway.infraestructure;

// Note que NÃO precisamos importar PaymentRequest, pois ele está no mesmo pacote!

import com.susana_garcia.payment_gateway.application.PaymentResponse;
import com.susana_garcia.payment_gateway.application.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
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
}