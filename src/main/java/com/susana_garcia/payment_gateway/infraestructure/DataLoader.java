package com.susana_garcia.payment_gateway.infraestructure;

import com.susana_garcia.payment_gateway.domain.User;
import com.susana_garcia.payment_gateway.infraestructure.persistence.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Se o banco já tiver gente, não faz nada
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("🌱 Povoando o banco de dados...");

        // Criando o Usuário 1 (O Rico) - ID será gerado como 1
        User user1 = new User();
        user1.setFullname("Susana Payer");
        user1.setEmail("susana@teste.com");
        user1.setPassword("123456");
        user1.setBalance(new BigDecimal("1000.00"));

        // Criando o Usuário 2 (O Recebedor) - ID será gerado como 2
        User user2 = new User();
        user2.setFullname("João Receiver");
        user2.setEmail("joao@teste.com");
        user2.setPassword("123456");
        user2.setBalance(new BigDecimal("500.00"));

        userRepository.saveAll(Arrays.asList(user1, user2));

        System.out.println("✅ Banco povoado com sucesso!");
    }
}