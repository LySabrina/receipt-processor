package com.example.receipt_processor.service;
import com.example.receipt_processor.dto.ItemDTO;
import com.example.receipt_processor.dto.ReceiptDTO;
import com.example.receipt_processor.models.Item;
import com.example.receipt_processor.models.Receipt;
import com.example.receipt_processor.repository.ItemRepository;
import com.example.receipt_processor.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReceiptServiceTest {

    @Mock
    ReceiptRepository receiptRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ReceiptService receiptService;

    ReceiptDTO receiptDTO;


    @BeforeEach
    public void setup(){

        ItemDTO i1 = new ItemDTO();
        i1.setPrice(20.10);
        i1.setShortDescription("des");
        ItemDTO i2 = new ItemDTO();
        i2.setShortDescription("desa");
        i2.setPrice(11.21);

        receiptDTO = new ReceiptDTO(
                "Target",
                LocalDate.of(2024,11,25),
                LocalTime.of(9,45),
                List.of(i1,i2),
                31.31
        );
    }

    @Test
    void process() {
        Receipt receipt = receiptService.process(receiptDTO);
        verify(receiptRepository, times(1)).save(any(Receipt.class));
        verify(itemRepository, times(2)).save(any(Item.class));
    }

    @Test
    void getReceipt() {

    }

    @Test
    void retailerPoints() {
        int actual = receiptService.retailerPoints(receiptDTO.getRetailer());
        assertThat(actual).isEqualTo(6);
    }

    @Test
    void retailerPointsWithNum() {
        int actual = receiptService.retailerPoints("Forever 21");
        assertThat(actual).isEqualTo(9);
    }

    @Test
    void totalPoints() {
        int actual = receiptService.totalPoints(receiptDTO.getTotal());
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void totalPointsRound(){
        int actual = receiptService.totalPoints(12.00);
        assertThat(actual).isEqualTo(75);
    }

    @Test
    void totalPointsMultiplier(){
        int actual = receiptService.totalPoints(0.25);
        assertThat(actual).isEqualTo(25);
    }

    @Test
    void itemsPointsEven() {
        int pointsPerPair = 5;
        int expectedPoints = receiptService.itemsPoints(receiptDTO.getItems());
        assertThat(expectedPoints).isEqualTo(pointsPerPair);
    }

    @Test
    void itemsPointsOdd(){
        int pointsPerPair = 5;
        ItemDTO i3 = new ItemDTO();
        i3.setPrice(20.00);
        i3.setShortDescription("des");

        List<ItemDTO> newList = new ArrayList<>(receiptDTO.getItems());
        newList.add(i3);

        int actual = receiptService.itemsPoints(newList);
        assertThat(actual).isEqualTo(pointsPerPair);
    }

    @Test
    void descriptionPoints() {
        int actual = receiptService.descriptionPoints(receiptDTO.getItems());
        assertThat(actual).isEqualTo(5);
    }

    @Test
    void purchaseDatePointsOdd() {
        LocalDate date = receiptDTO.getPurchaseDate();
        int actual = receiptService.purchaseDatePoints(date);
        assertThat(actual).isEqualTo(6);
    }

    @Test
    void purchaseDatePointsEven() {
        LocalDate date = LocalDate.of(2024,11,24);
        int actual = receiptService.purchaseDatePoints(date);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void purchaseTimePointsNotBetween() {
        int actual = receiptService.purchaseTimePoints(receiptDTO.getPurchaseTime());
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void purchaseTimePointsBetween2pmAnd4pm(){
        LocalTime time = LocalTime.of(15, 20);
        int actual = receiptService.purchaseTimePoints(time);
        assertThat(actual).isEqualTo(10);
    }
    @Test
    void purchaseTimePointsTimeInclusive(){
        LocalTime time = LocalTime.of(14, 0);
        int actual = receiptService.purchaseTimePoints(time);
        assertThat(actual).isEqualTo(0);
    }

}
