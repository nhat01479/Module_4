package com.cg.repository;

import com.cg.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositReposity extends JpaRepository<Deposit, Long> {
}
