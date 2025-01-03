package com.example.receipt_processor.repository;

import com.example.receipt_processor.models.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
    @Query(value = "select * from Item where short_description=?1", nativeQuery = true)
    Item itemWithDescription(String description);
}
