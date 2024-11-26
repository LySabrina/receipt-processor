package com.example.receipt_processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class ReceiptIdDTO {
    private UUID id;
}
