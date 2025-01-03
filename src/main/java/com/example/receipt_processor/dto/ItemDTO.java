package com.example.receipt_processor.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    @NotBlank   // states that the short description should not be empty
    private String shortDescription;

    @Positive   //states that the price should be a positive value, does not include 0 dollars
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    private double price;
}
