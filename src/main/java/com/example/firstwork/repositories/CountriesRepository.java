package com.example.firstwork.repositories;

;
import com.example.firstwork.entites.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface CountriesRepository extends JpaRepository<Countries, Long> {

    Countries findByName(String name);

}
