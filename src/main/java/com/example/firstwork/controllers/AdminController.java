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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ItemsService itemService;

    @Autowired
    private UserService userService;

    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String adminBrands(Model model) {

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        model.addAttribute("currentUser", getUserData());

        return "admin_brands";
    }



    @GetMapping("/admin_countries")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String adminCountries(Model model) {

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        model.addAttribute("currentUser", getUserData());

        return "admin_countries";
    }

    @GetMapping("/admin_items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String adminItems(Model model) {

        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Brands> brands = itemService.getAllBrands();
        model.addAttribute("brands", brands);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("currentUser", getUserData());

        return "admin_items";
    }
    @GetMapping("/admin_orders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String adminOrders(Model model) {

        List<Orders> orders = itemService.getAllOrders();

        double total = orders.stream().mapToDouble(Orders::getTotal_price).sum();// sum

        model.addAttribute("total", total);

        model.addAttribute("orders", orders);

        model.addAttribute("currentUser", getUserData());

        return "admin_orders";
    }

    @GetMapping("/admin_items/{Id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String item_details(Model model, @PathVariable(value = "Id") Long id){
        Items item = itemService.getItem(id);
        if (item != null) {

            List<Brands> brands = itemService.getAllBrands();
            model.addAttribute("brands", brands);

            model.addAttribute("item", item);

            List<Categories> categories = itemService.getAllCategories();

            categories.removeIf(x -> item.getCategories().contains(x)); //

            model.addAttribute("categories", categories);

            model.addAttribute("currentUser", getUserData());

            List<Pictures> pictures = itemService.getPicturesByItemId(id);
            model.addAttribute("pictures", pictures);

            return "details_item";
        }
        else {
            return "redirect:/admin_items";
        }
    }

    @GetMapping("/admin_categories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String adminCategories(Model model) {

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        model.addAttribute("currentUser", getUserData());

        return "admin_categories";
    }

    @GetMapping("/admin_users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminUsers(Model model) {

        List<Users> users = userService.getAllUsers();
        model.addAttribute("users", users);

        model.addAttribute("currentUser", getUserData());

        return "admin_users";
    }

    @GetMapping("/admin_roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminRoles(Model model) {

        List<Roles> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);

        model.addAttribute("currentUser", getUserData());

        return "admin_roles";
    }

    @GetMapping("/admin_users/{Id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String user_details(Model model, @PathVariable(value = "Id") Long id){
        Users user = userService.getUserById(id);
        if (user!= null) {

            model.addAttribute("user", user);
            List<Roles> roles = userService.getAllRoles();

            roles.removeIf(x -> user.getRoles().contains(x));

            model.addAttribute("roles", roles);

            model.addAttribute("currentUser", getUserData());

            return "details_user";
        }
        else {
            return "redirect:/admin_users";
        }
    }

}
