package com.example.transaction.controller;

import com.example.transaction.modelo.Transaction;
import com.example.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired //
    private TransactionRepository transactionRepository;

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<>(transactionRepository.findAll(), HttpStatus.OK); //Retornar respuesta
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionRepository.findById(id)
                //validar si el optional que devuelve esta vacio o no
                .map(transaction -> new ResponseEntity<>(transaction, HttpStatus.OK))
                //Si el optional contiene TODOS los items de la transaccion retorna el ResponseEntity o respuesta HTTP
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                //Si el Optional está vacío (es decir, no se encontró la transacción), devuelve una ResponseEntity con estado HttpStatus.NOT_FOUND.
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<Transaction>> getTransactionByCategory(@PathVariable String category){
        List<Transaction> transactions =  transactionRepository.findByCategory(category);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    @GetMapping("/by-date")
    public ResponseEntity<List<Transaction>> getTransactionsByDate(
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);
        if (transactions.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "No se ha encontrado ninguna transacción en el rango especificado");
            errorResponse.put("timestamp", new Date());
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}