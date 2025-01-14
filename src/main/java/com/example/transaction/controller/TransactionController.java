package com.example.transaction.controller;

import com.example.transaction.modelo.FinancialSummary;
import com.example.transaction.modelo.Transaction;
import com.example.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<>(transactionRepository.findAll(), HttpStatus.OK); //Retornar respuesta
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado ninguna transacción con el ID especificado"));
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionRepository.save(transaction), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails){
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            //validar si la transaccion esta presente o no
            Transaction updatedTransaction = transaction.get();
            //obtener el objeto Transaction que contiene el Optional transaction
            updatedTransaction.setType(transactionDetails.getType());
            updatedTransaction.setAmount(transactionDetails.getAmount());
            updatedTransaction.setCategory(transactionDetails.getCategory());
            updatedTransaction.setDate(transactionDetails.getDate());
            updatedTransaction.setDescription(transactionDetails.getDescription());
            transactionRepository.save(updatedTransaction);
            return ResponseEntity.ok(updatedTransaction);
        } else {
            throw new ResourceNotFoundException("La transaccion que intenta actualizar no esta creada en la base de datos");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTransaction(@PathVariable Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", true);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            throw new ResourceNotFoundException("Id no encontrado");
        }
    }
    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionByCategory(@PathVariable String category){
        List<Transaction> transactions =  transactionRepository.findByCategory(category);
        if (transactions.isEmpty()) { // Si no se encuentran transacciones, lanzar una excepción personalizada
            throw new ResourceNotFoundException("No se ha encontrado ninguna transacción para la categoría especificada");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    @GetMapping("/by-date")
    public ResponseEntity<?> getTransactionsByDate(
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No se ha encontrado ninguna transacción en el rango especificado");
        }
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
    @GetMapping("/summary")
    public FinancialSummary getFinancialSummary(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {
        List<Transaction> transactions = transactionRepository.findByDateBetween(start, end);
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == Transaction.Type.Income)
                .map(Transaction::getAmount) .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = transactions.stream()
                .filter(t -> t.getType() == Transaction.Type.Expense)
                .map(Transaction::getAmount) .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);
        return new FinancialSummary(totalIncome, totalExpenses, netBalance);
    }
}
