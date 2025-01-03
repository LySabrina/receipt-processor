package com.example.receipt_processor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ReceiptDTO {
    @NotBlank(message = "Retailer can not be blank")
    private String retailer;
    @NotNull(message = "Purchase Date missing")
    private LocalDate purchaseDate;

    @NotNull(message = "Purchase Time missing")
    private LocalTime purchaseTime;

    @NotEmpty(message = "No items purchased")
    private List<ItemDTO> items;

    @Positive(message = "Negative Total Found")
    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Invalid Total Amount")
    private double total;
}
