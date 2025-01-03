package com.example.receipt_processor.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorDTO {
    private String mainMsg;
    private List<Map<String,String>> errors;
}
