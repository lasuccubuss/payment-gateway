package com.susana_garcia.payment_gateway.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
//Só de estender JpaRepository já é implementado um método .save() de graça

    List<TransactionEntity> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
