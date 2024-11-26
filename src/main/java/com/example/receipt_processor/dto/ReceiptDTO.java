package com.example.receipt_processor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ReceiptDTO {
    @NotBlank
    private String retailer;
    @NotNull
    private LocalDate purchaseDate;
    @NotNull
    private LocalTime purchaseTime;


    @NotEmpty
    @Valid
    private List<ItemDTO> items;

    @Positive
    private double total;
}
