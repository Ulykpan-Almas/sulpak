package com.example.firstwork.repositories;

import com.example.firstwork.entites.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Items, Long> {

    ArrayList<Items> findByName(String name);
    ArrayList<Items> findAllByInTopPageIsTrue();
    ArrayList<Items> findAllByNameContainingOrderByPriceAsc(String name);
    ArrayList<Items> findAllByBrandIdAndPriceBetweenOrderByPriceAsc(Long id, double price1, double price2);
    ArrayList<Items> findAllByBrandIdAndPriceBetweenOrderByPriceDesc(Long id, double price1, double price2);
    ArrayList<Items> findAllByNameContainingAndBrandIdAndPriceBetweenOrderByPriceAsc(String name, Long id, double price1, double price2);
    ArrayList<Items> findAllByNameContainingAndBrandIdAndPriceBetweenOrderByPriceDesc(String name, Long id, double price1, double price2);
    ArrayList<Items> findAllByBrandId(Long id);
    ArrayList<Items> findAllByCategoriesId(Long id);
}
