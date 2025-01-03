package com.example.receipt_processor.controller;

import com.example.receipt_processor.dto.ReceiptDTO;
import com.example.receipt_processor.dto.ReceiptIdDTO;
import com.example.receipt_processor.dto.ReceiptPointsDTO;
import com.example.receipt_processor.exceptions.NoIdFoundException;
import com.example.receipt_processor.models.Receipt;
import com.example.receipt_processor.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Controller class responsible for handling related receipt endpoints
 */
@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;
    public ReceiptController(ReceiptService receiptService){
        this.receiptService = receiptService;
    }

    /**
     *  Return type is ResponseEntity<> of type ? (generic)
     *  Successful ==> DTO object
     *  Failure ==> String error
     */

    /**
     * Process a receipt and assigns a generated UUID to this receipt
     * @param receiptDTO User's submitted receipt
     * @return response with the generated uuid for that receipt
     */
    @PostMapping("/process")
    //@Valid recursively checks if the annotation fits the specified valid annotations
    public ResponseEntity<ReceiptIdDTO> process(@RequestBody @Valid ReceiptDTO receiptDTO) {
        Receipt receipt = receiptService.process(receiptDTO);
        ReceiptIdDTO receiptIdDTO = new ReceiptIdDTO(receipt.getUuid());
        return ResponseEntity.status(200).body(receiptIdDTO);
    }

    /**
     * Gets points associated for that receipt
     * @param id the UUID of the receipt
     * @return response with the points for that receipt
     */
    @GetMapping("/{id}/points")
    public ResponseEntity<ReceiptPointsDTO> getPoints(@PathVariable String id) throws NoIdFoundException {
        Receipt receipt = receiptService.getReceipt(UUID.fromString(id));
        ReceiptPointsDTO pointsDTO = new ReceiptPointsDTO(receipt.getPoints());
        return ResponseEntity.status(200).body(pointsDTO);
    }
}
