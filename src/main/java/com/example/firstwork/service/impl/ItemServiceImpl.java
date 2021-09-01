package com.example.firstwork.service.impl;

import com.example.firstwork.entites.*;
import com.example.firstwork.repositories.*;
import com.example.firstwork.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemsService {


    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CountriesRepository countriesRepository;
    @Autowired
    private BrandsRepository brandsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CommentRepository commentRepository;





    @Override
    public Items addItem(Items item) {
        return itemRepository.save(item);
    }


    @Override
    public List<Items> getAllItems() {
        return itemRepository.findAll();
    }


    @Override
    public ArrayList<Items> getAllItemsInTop() {
        return itemRepository.findAllByInTopPageIsTrue();
    }


    @Override
    public ArrayList<Items> getAllItemsByNameLikeOrderByPriceAsc(String name) {
        return itemRepository.findAllByNameContainingOrderByPriceAsc(name);
    }


    @Override
    public ArrayList<Items> getAllItemsByBrandIdAndPriceBetweenAsc(Long id,double price1, double price2) {
        return itemRepository.findAllByBrandIdAndPriceBetweenOrderByPriceAsc(id, price1, price2);
    }


    @Override
    public ArrayList<Items> getAllItemsByBrandIdAndPriceBetweenDesc(Long id, double price1, double price2) {
        return itemRepository.findAllByBrandIdAndPriceBetweenOrderByPriceDesc(id, price1, price2);
    }


    @Override
    public ArrayList<Items> getAllItemsByNameLikeAndBrandIdAndPriceBetweenAsc(String name, Long id, double price1, double price2) {
        return itemRepository.findAllByNameContainingAndBrandIdAndPriceBetweenOrderByPriceAsc(name, id, price1, price2);
    }


    @Override
    public ArrayList<Items> getAllItemsByNameLikeAndBrandIdAndPriceBetweenDesc(String name, Long id, double price1, double price2) {
        return itemRepository.findAllByNameContainingAndBrandIdAndPriceBetweenOrderByPriceDesc(name, id, price1, price2);
    }


    @Override
    public ArrayList<Items> getAllItemsByBrandId(Long id) {
        return itemRepository.findAllByBrandId(id);
    }


    @Override
    public ArrayList<Items> getAllItemsByCategoryId(Long id) {
        return itemRepository.findAllByCategoriesId(id);
    }


    @Override
    public Items getItem(Long id) {
        return itemRepository.getOne(id);
    }


    @Override
    public void deleteItem(Items item) {
        itemRepository.delete(item);
    }


    @Override
    public Items saveItem(Items item) {
        return itemRepository.save(item);
    }






    @Override
    public Countries addCountry(Countries country) {
        return countriesRepository.save(country);
    }


    @Override
    public List<Countries> getAllCountries() {
        return countriesRepository.findAll();
    }


    @Override
    public Countries getCountry(Long id) {
        return countriesRepository.getOne(id);
    }


    @Override
    public void deleteCountry(Countries country) {
        countriesRepository.delete(country);
    }


    @Override
    public Countries saveCountry(Countries country) {
        return countriesRepository.save(country);
    }


    @Override
    public Countries getCountryByName(String name) {
        return countriesRepository.findByName(name);
    }








    @Override
    public Brands addBrand(Brands brand) {
        return brandsRepository.save(brand);
    }


    @Override
    public List<Brands> getAllBrands() {
        return brandsRepository.findAll();
    }


    @Override
    public Brands getBrand(Long id) {
        return brandsRepository.getOne(id);
    }


    @Override
    public void deleteBrand(Brands brand) {
        brandsRepository.delete(brand);
    }


    @Override
    public Brands saveBrand(Brands brand) {
        return brandsRepository.save(brand);
    }


    @Override
    public Brands getBrandByName(String name) {
        return brandsRepository.findByName(name);
    }








    @Override
    public Categories addCategory(Categories category) {
        return categoryRepository.save(category);
    }


    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }


    @Override
    public Categories getCategory(Long id) {
        return categoryRepository.getOne(id);
    }


    @Override
    public void deleteCategory(Categories category) {
        categoryRepository.delete(category);
    }


    @Override
    public Categories saveCategory(Categories category) {
        return categoryRepository.save(category);
    }


    @Override
    public Categories getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }








    @Override
    public Pictures addPicture(Pictures picture) {
        return pictureRepository.save(picture);
    }

    @Override
    public List<Pictures> getAllPictures() {
        return pictureRepository.findAll();
    }


    @Override
    public Pictures getPicture(Long id) {
        return pictureRepository.getOne(id);
    }


    @Override
    public void deletePicture(Pictures picture) {
        pictureRepository.delete(picture);
    }


    @Override
    public Pictures savePicture(Pictures picture) {
        return pictureRepository.save(picture);
    }


    @Override
    public List<Pictures> getPicturesByItemId(Long id) {
        return pictureRepository.findByShopItemId(id);
    }









    @Override
    public Orders addOrder(Orders order) {
        return orderRepository.save(order);
    }


    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }


    @Override
    public Orders getOrder(Long id) {
        return orderRepository.getOne(id);
    }


    @Override
    public void deleteOrder(Orders order) {
        orderRepository.delete(order);
    }


    @Override
    public Orders saveOrder(Orders order) {
        return orderRepository.save(order);
    }








    @Override
    public Comments addComment(Comments comment) {
        return commentRepository.save(comment);
    }


    @Override
    public List<Comments> getAllComments() {
        return commentRepository.findAll();
    }


    @Override
    public Comments getComment(Long id) {
        return commentRepository.getOne(id);
    }


    @Override
    public void deleteComment(Comments comment) {
        commentRepository.delete(comment);
    }


    @Override
    public Comments saveComment(Comments comment) {
        return commentRepository.save(comment);
    }


    @Override
    public List<Comments> getCommentsByItemId(Long item_id) {
        return commentRepository.findAllByItemIdOrderByAddedDateDesc(item_id);
    }
}
