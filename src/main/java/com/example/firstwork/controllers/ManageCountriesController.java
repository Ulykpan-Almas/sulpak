package com.example.firstwork.controllers;

import com.example.firstwork.entites.Countries;
import com.example.firstwork.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ManageCountriesController {

    @Autowired
    private ItemsService itemService;

    @GetMapping("/addCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addCountryPage(){
        return "addCountry";
    }

    @PostMapping("/addCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addCountry(@RequestParam(name = "name") String name,
                             @RequestParam(name = "code") String code){
        Countries country = new Countries(null, name, code);
        itemService.addCountry(country);
        return "redirect:/admin_countries";
    }

    @PostMapping("/editCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String editCountry(@RequestParam(name = "country_id") Long country_id,
                                @RequestParam(name = "name") String name,
                             @RequestParam(name = "code") String code){
        Countries country_initial = itemService.getCountry(country_id);
        Countries country = itemService.getCountryByName(name);

        if (country_initial != null) {
            country_initial.setCode(code);
            if (country == null){
                country_initial.setName(name);
            }
            itemService.saveCountry(country_initial);
        }

        return "redirect:/admin_countries";
    }

    @PostMapping("/deleteCountry")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteCountry(@RequestParam(name = "country_id") Long country_id){
        Countries country_initial = itemService.getCountry(country_id);

        if (country_initial != null) {
            itemService.deleteCountry(country_initial);
        }

        return "redirect:/admin_countries";
    }
}
