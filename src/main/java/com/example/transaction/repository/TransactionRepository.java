package com.example.transaction.repository;

import com.example.transaction.modelo.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findByCategory(String category);
    List<Transaction> findByDateBetween(Date startDate, Date endDate);
}
