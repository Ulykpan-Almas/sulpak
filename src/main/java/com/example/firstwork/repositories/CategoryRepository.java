package com.example.firstwork.repositories;

import com.example.firstwork.entites.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {

    Categories findByName(String name);

}
