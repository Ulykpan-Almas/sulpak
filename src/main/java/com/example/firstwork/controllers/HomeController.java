package com.example.firstwork.controllers;

import com.example.firstwork.entites.*;
import com.example.firstwork.service.ItemsService;
import com.example.firstwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemsService itemService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {

        Iterable<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("currentUser", getUserData());

        return "index";
    }

    @GetMapping("/top")
    public String top(Model model) {
        ArrayList<Items> items = itemService.getAllItemsInTop();
        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("currentUser", getUserData());

        return "index";
    }

    @GetMapping("/403")
    public String accessDenied(Model model){
        model.addAttribute("currentUser", getUserData());
        return "403";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("currentUser", getUserData());

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        return "profile";
    }
    @PostMapping("/add_to_basket")
    public String addToBasket(@RequestParam("item_id") Long id, HttpSession session){
        List<Basket> basket = (List<Basket>) session.getAttribute("basket");
        Integer basket_size = (Integer) session.getAttribute("basket_size");

        if (basket == null) {
            basket = new ArrayList<>();
            session.setAttribute("basket", basket);
        }
        Items item = itemService.getItem(id);
        if (basket.size() > 0){
            for (Basket basket1 : basket){
                if (basket1.getItem().getId().equals(id)){
                    basket1.setAmount(basket1.getAmount() + 1);
                    session.setAttribute("basket", basket);
                    if (basket_size == null){
                        session.setAttribute("basket_size", 1);
                    }
                    else {
                        session.setAttribute("basket_size", basket_size + 1);
                    }

                    return "redirect:/item/" + id;
                }
            }
        }
        basket.add(new Basket(item, 1));
        session.setAttribute("basket", basket);

        if (basket_size == null){
            session.setAttribute("basket_size", 1);
        }
        else {
            session.setAttribute("basket_size", basket_size + 1);
        }

        return "redirect:/item/" + id;
    }

    @GetMapping("/basket")
    public String basket(Model model, HttpSession session) {
        List<Basket> basket = (List<Basket>) session.getAttribute("basket");
        double total = 0;
        if (basket == null){
            basket = new ArrayList<Basket>();
        }
        else {
            total = basket.stream().mapToDouble(e -> e.getAmount() * e.getItem().getPrice()).sum();
        }
        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("currentUser", getUserData());

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        model.addAttribute("total", total);

        model.addAttribute("baskets", basket);

        return "basket";
    }

    @PostMapping(value = "/addQuantity")
    public String addQuantity(HttpSession session,
                              @RequestParam(name = "id") Long id){
        List<Basket> items = (List<Basket>) session.getAttribute("basket");
        Integer basket_size = (Integer) session.getAttribute("basket_size");
        for(Basket b: items){
            if(b.getItem().getId().equals(id)){
                b.setAmount(b.getAmount() + 1);
                session.setAttribute("basket_size", basket_size + 1);
                session.setAttribute("basket", items);
                return "redirect:/basket";
            }
        }


        return "redirect:/basket";
    }

    @PostMapping(value = "/removeQuantity")
    public String removeQuantity(HttpSession session,
                                 @RequestParam(name = "id") Long id){
        List<Basket> items = (List<Basket>) session.getAttribute("basket");
        Integer basket_size = (Integer) session.getAttribute("basket_size");
        Items item = itemService.getItem(id);
        Basket basket = new Basket();

        for(Basket b: items){
            if(b.getItem().getId().equals(id)){
                if (b.getAmount() > 1){
                    b.setAmount(b.getAmount() - 1);
                }
                else {
                    items.remove(b);
                }
                session.setAttribute("basket_size", basket_size - 1);
                session.setAttribute("basket", items);
                return "redirect:/basket";
            }
        }


        return "redirect:/basket";
    }

    @PostMapping(value = "/checkIn")
    public String buyItems(HttpSession session){
        List<Basket> items = (List<Basket>) session.getAttribute("basket");
        Date date = new Date();
        for(Basket b: items){
            if (b.getItem().getAmount() < b.getAmount() && b.getItem().getAmount() > 0){
                itemService.addOrder(new Orders(null, b.getItem(), b.getItem().getAmount(), b.getItem().getAmount() * b.getItem().getPrice(), date));
                b.getItem().setAmount(0);
                itemService.saveItem(b.getItem());
            }
            else {
                itemService.addOrder(new Orders(null, b.getItem(), b.getAmount(), b.getAmount() * b.getItem().getPrice(), date));

                b.getItem().setAmount(b.getItem().getAmount() - b.getAmount());
                itemService.saveItem(b.getItem());
            }

        }
        session.removeAttribute("basket");
        session.setAttribute("basket_size", 0);

        return "redirect:/";
    }

    @PostMapping(value = "/clearBasket")
    public String clearBasket(HttpSession session){
        session.removeAttribute("basket");
        session.setAttribute("basket_size", 0);
        return "redirect:/basket";
    }



    @PostMapping(value = "/add_comment")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@RequestParam(name = "item_id") Long item_id,
                             @RequestParam(name = "comment_text") String comment_text, HttpSession session){

        Users user = getUserData();

        Date date = new Date();
        Items item = itemService.getItem(item_id);
        if (item != null){
            Comments comment = new Comments(null, comment_text, date, item, user);
            itemService.addComment(comment);

            return "redirect:/item/" + item_id;
        }
        else {
            return "redirect:/item/" + item_id + "?error";
        }

    }

    @PostMapping(value = "/editComment")
    @PreAuthorize("isAuthenticated()")
    public String editComment(@RequestParam(name = "comment_id") Long comment_id,
                              @RequestParam(name = "comment_text") String comment_text,
                              @RequestParam(name = "item_id") Long item_id){
        Comments comment = itemService.getComment(comment_id);
        comment.setComment(comment_text);
        itemService.saveComment(comment);
        return "redirect:/item/" + item_id;
    }

    @PostMapping(value = "/deleteComment")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@RequestParam(name = "comment_id") Long comment_id,
                                @RequestParam(name = "item_id") Long item_id){
        Comments comment = itemService.getComment(comment_id);
        itemService.deleteComment(comment);
        return "redirect:/item/" + item_id;
    }

    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }

}
