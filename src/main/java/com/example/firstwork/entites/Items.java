package com.example.firstwork.entites;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "t_items")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private String name;

    private String description;

    private double price;

    private int amount;

    private int stars;

    private String smallPictureUrl;

    private String largePictureUrl;

    private Date addedDate;

    private boolean inTopPage;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brands brand;

    @ManyToMany
    private List<Categories> categories;
}
