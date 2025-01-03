package com.example.receipt_processor.repository;

import com.example.receipt_processor.models.Receipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, UUID> {

}

