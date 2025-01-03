package com.example.receipt_processor.controller;

import com.example.receipt_processor.dto.ItemDTO;
import com.example.receipt_processor.dto.ReceiptDTO;
import com.example.receipt_processor.exceptions.NoIdFoundException;
import com.example.receipt_processor.models.Item;
import com.example.receipt_processor.models.Receipt;
import com.example.receipt_processor.service.ReceiptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReceiptController.class)
public class ReceiptControllerTest {

    @MockitoBean
    ReceiptService receiptService;

    @Autowired
    MockMvc mockMvc;    //used to mock the request

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void process() throws Exception{
        //arrange
        ReceiptDTO receiptDTO = new ReceiptDTO(
                "Target",
                LocalDate.of(2024, 12, 12),
                LocalTime.of(12, 12),
                List.of(new ItemDTO("Pepsi", 1.20)),
                1.20
        );
//        when(receiptService.process(receiptDTO)).thenReturn(
//                new Receipt(UUID.randomUUID(),
//                        receiptDTO.getRetailer(),
//                        receiptDTO.getPurchaseDate(),
//                        receiptDTO.getPurchaseTime(),
//
//                        receiptDTO.getTotal(),
//                        20
//                        )
//        );
        String json = objectMapper.writeValueAsString(receiptDTO);

        //act
        mockMvc.perform(post("/receipts/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
        //assert
    }

    @Test
    void processInvalidReceipt() throws Exception{
        //arrange
        ReceiptDTO dto = new ReceiptDTO(
                "",
                LocalDate.of(2024, 12, 12),
                LocalTime.of(12, 12),
                List.of(new ItemDTO("Pepsi", 1.20)),
                1.20
        );

        String json = objectMapper.writeValueAsString(dto);
        //act
        mockMvc.perform(post("/receipts/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mainMsg").value("Invalid Receipt"))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].retailer").value("Retailer can not be blankz"))
        ;
        //assert
    }

    @Test
    void getPoints() throws Exception {
        UUID id = UUID.randomUUID();
        Receipt receipt = new Receipt(
                id,
                "Target",
                LocalDate.of(2024, 12, 12),
                LocalTime.of(12, 12),
                List.of(new Item("Pepsi", 1.20)),
                1.20,
                6);

        when(receiptService.getReceipt(id))
                .thenReturn(receipt);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/receipts/{id}/points", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(6));
    }

    @Test
    void getPointsFail() throws Exception{
        //arrange
        UUID id = UUID.randomUUID();
        when(receiptService.getReceipt(id)).thenThrow(new NoIdFoundException("Id not found"));


        //act
        mockMvc.perform(
                MockMvcRequestBuilders.get("/receipts/{id}/points", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isNotFound());
    }


}
