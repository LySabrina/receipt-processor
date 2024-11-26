package com.example.receipt_processor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemDTO {
    @NotBlank
    private String shortDescription;

    @Positive
    @NotNull
    private double price;
}
