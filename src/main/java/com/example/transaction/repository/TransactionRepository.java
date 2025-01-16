package com.example.transaction.repository;

import com.example.transaction.modelo.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findByCategory(String category);
    List<Transaction> findByDateBetween(Date startDate, Date endDate);
    @Query("SELECT DISTINCT t.category FROM Transaction t")
    List<String> findDistinctCategories();

}
