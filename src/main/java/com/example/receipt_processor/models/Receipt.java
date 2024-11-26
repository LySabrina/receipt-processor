package com.example.receipt_processor.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Receipt {
    @Id
    private UUID uuid;

    private String retailer;

    private LocalDate purchaseDate;

    private LocalTime purchaseTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})

    private List<Item> items;


    private double total;

    private int points;


}
