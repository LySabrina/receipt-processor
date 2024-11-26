package com.example.receipt_processor.service;

import com.example.receipt_processor.dto.ItemDTO;
import com.example.receipt_processor.dto.ReceiptDTO;
import com.example.receipt_processor.models.Item;
import com.example.receipt_processor.models.Receipt;
import com.example.receipt_processor.repository.ItemRepository;
import com.example.receipt_processor.repository.ReceiptRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Handles logic associated with Receipts
 */
@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ItemRepository itemRepository;

    public ReceiptService(ReceiptRepository receiptRepository, ItemRepository itemRepository){
        this.receiptRepository = receiptRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Processes the Receipt and assign appropriate points
     * @param receiptDTO the receipt sent by the user
     * @return Receipt with updated points
     */
    public Receipt process(ReceiptDTO receiptDTO) {
        List<Item> itemsList = new ArrayList<>();
        for (ItemDTO itemDTO : receiptDTO.getItems()) {
            Item item = new Item(itemDTO.getShortDescription().trim(), itemDTO.getPrice());
            Item existedItem = itemRepository.itemWithDescription(item.getShortDescription());
            if( existedItem == null ){
                existedItem = itemRepository.save(item);
            }
            itemsList.add(existedItem);
        }
        int totalPoints = retailerPoints(receiptDTO.getRetailer()) +
                totalPoints(receiptDTO.getTotal()) +
                itemsPoints(receiptDTO.getItems()) +
                descriptionPoints(receiptDTO.getItems()) +
                purchaseDatePoints(receiptDTO.getPurchaseDate()) +
                purchaseTimePoints(receiptDTO.getPurchaseTime());

        Receipt receipt = new Receipt(UUID.randomUUID(),
                receiptDTO.getRetailer(),
                receiptDTO.getPurchaseDate(),
                receiptDTO.getPurchaseTime(),
                itemsList,
                receiptDTO.getTotal(),
                totalPoints
        );
        return receiptRepository.save(receipt);

    }

    /**
     * Fetches the Receipt with associated UUID
     * @param uuid the uuid associated with receipt
     * @return the receipt associated with uuid or null if not found
     */
    public Receipt getReceipt(UUID uuid){
        Optional<Receipt> receiptOptional = receiptRepository.findById(uuid);
        if(receiptOptional.isPresent()){
            return receiptOptional.get();
        }
        return null;
    }

    // METHODS ASSOCIATED WITH GIVING POINTS
    /**
     * Awards 1 point for every alphanumeric character in the retailer name
     * @param retailer the retail store
     * @return points
     */
    public int retailerPoints(String retailer) {
        int points = 0;
        for (int i = 0; i < retailer.length(); i++) {
            char c = retailer.charAt(i);
            if (c >= 48 && c <= 57 || c >= 65 && c <= 90 || c >= 97 && c <= 122) {
                points++;
            }
        }
        System.out.println("Retailer - " + points);
        return points;

    }

    /**
     * Handles awarding points based on the total
     * Gives 50 points if total is a round number with no cents
     * Gives 25 points if the total is a multiple of 0.25
     * @param total the total cost
     * @return points
     */
    public int totalPoints(double total) {
        int points = 0;
        BigDecimal totalBD = new BigDecimal(Double.toString(total));
        BigDecimal multiple = new BigDecimal(Double.toString(0.25));

        if (totalBD.remainder(multiple).doubleValue() == 0) {
            points += 25;
        }
        if (total % 1 == 0) {
            points += 50;
        }
        System.out.println("Total Points - " + points);
        return points;
    }

    /**
     * Handles awarding points based on the number of items purchased.
     * Every 2 items purchased, award 5 points
     * @param items items purchased
     * @return points
     */
    public int itemsPoints(List<ItemDTO> items) {
        int itemsLen = items.size() / 2;
        System.out.println("Item Point - " + itemsLen * 5);
        return itemsLen * 5;
    }

    /**
     * Handles the trimmed length of each product
     * If length of receipt is a multiple of 3
     * Multiple the price by 0.2 then round up and return that number
     * @return points
     */
    public int descriptionPoints(List<ItemDTO> items) {
        int points = 0;
        for (ItemDTO item : items) {
            if (item.getShortDescription().length() % 3 == 0) {
                System.out.println(item.getPrice() * 0.2);
                int itemPoint = (int) Math.ceil(item.getPrice() * 0.2);
                points += itemPoint;
            }
        }
        System.out.println("Description points - " + points);
        return points;
    }

    /**
     * Handles awarding points based on the purchased date
     * Award 6 points if the purchased date is odd
     * @param purchaseDate the date products were purchased
     * @return points
     */
    public int purchaseDatePoints(LocalDate purchaseDate) {
        int day = purchaseDate.getDayOfMonth();
        int points = 0;
        if (day % 2 != 0) {
            points += 6;
        }
        System.out.println("Purchase Date - " + points);
        return points;
    }

    /**
     * Handles awarding points based on the purchase time
     * Awards 10 points if purchase time is between 2pm and 4pm exclusive
     * @param purchaseTime the time products were purchased
     * @return points
     */
    public int purchaseTimePoints(LocalTime purchaseTime) {
        int points = 0;
        if (purchaseTime.isAfter(LocalTime.of(14, 0)) && purchaseTime.isBefore(LocalTime.of(16, 0))) {
            points += 10;
        }
        System.out.println("Purchase TIme - " + points);
        return points;
    }
}
