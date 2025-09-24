package com.ravemaster.inventory.repository;

import com.ravemaster.inventory.domain.entity.TransactionLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionLineRepository extends JpaRepository<TransactionLine, UUID> {

}
