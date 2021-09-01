package com.example.firstwork.service;

import com.example.firstwork.entites.*;

import java.util.ArrayList;
import java.util.List;

public interface ItemsService {

    Items addItem(Items shopItem);
    List<Items> getAllItems();
    Items getItem(Long id);
    void deleteItem(Items item);
    Items saveItem(Items item);
    ArrayList<Items> getAllItemsInTop();
    ArrayList<Items> getAllItemsByNameLikeOrderByPriceAsc(String name);
    ArrayList<Items> getAllItemsByBrandIdAndPriceBetweenAsc(Long id, double price1, double price2);
    ArrayList<Items> getAllItemsByBrandIdAndPriceBetweenDesc(Long id, double price1, double price2);
    ArrayList<Items> getAllItemsByNameLikeAndBrandIdAndPriceBetweenAsc(String name, Long id, double price1, double price2);
    ArrayList<Items> getAllItemsByNameLikeAndBrandIdAndPriceBetweenDesc(String name, Long id, double price1, double price2);
    ArrayList<Items> getAllItemsByBrandId(Long id);
    ArrayList<Items> getAllItemsByCategoryId(Long id);

    Countries addCountry(Countries country);
    List<Countries> getAllCountries();
    Countries getCountry(Long id);
    void deleteCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountryByName(String name);

    Brands addBrand(Brands brand);
    List<Brands> getAllBrands();
    Brands getBrand(Long id);
    void deleteBrand(Brands brand);
    Brands saveBrand(Brands brand);
    Brands getBrandByName(String name);

    Categories addCategory(Categories category);
    List<Categories> getAllCategories();
    Categories getCategory(Long id);
    void deleteCategory(Categories category);
    Categories saveCategory(Categories category);
    Categories getCategoryByName(String name);
//    List<Categories> getAllCategoriesNotIn(List<Categories> categories);

    Pictures addPicture(Pictures picture);
    List<Pictures> getAllPictures();
    Pictures getPicture(Long id);
    void deletePicture(Pictures picture);
    Pictures savePicture(Pictures picture);
    List<Pictures> getPicturesByItemId(Long id);

    Orders addOrder(Orders order);
    List<Orders> getAllOrders();
    Orders getOrder(Long id);
    void deleteOrder(Orders order);
    Orders saveOrder(Orders order);

    Comments addComment(Comments comment);
    List<Comments> getAllComments();
    Comments getComment(Long id);
    void deleteComment(Comments comment);
    Comments saveComment(Comments comment);
    List<Comments> getCommentsByItemId(Long item_id);
}
