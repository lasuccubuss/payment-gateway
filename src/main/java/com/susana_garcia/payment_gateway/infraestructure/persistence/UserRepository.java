package com.susana_garcia.payment_gateway.infraestructure.persistence;

import com.susana_garcia.payment_gateway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
