package com.example.firstwork.controllers;


import com.example.firstwork.entites.Brands;
import com.example.firstwork.entites.Categories;
import com.example.firstwork.entites.Roles;
import com.example.firstwork.entites.Users;
import com.example.firstwork.service.ItemsService;
import com.example.firstwork.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ItemsService itemService;

    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;


    @GetMapping("/registration")
    public String registration(Model model) {

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(Model model, @RequestParam("user_email") String email,
                          @RequestParam("user_password") String password,
                          @RequestParam("user_repassword") String re_password,
                          @RequestParam("full_name") String full_name){

        if (!password.equals(re_password)){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
//        Users user = new Users(null, email, password, full_name, null);
        Users user = new Users();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(full_name);
        if (!userService.saveUser(user)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        return "redirect:/";
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUserAdmin(Model model, RedirectAttributes redirectAttrs, @RequestParam("user_email") String email,
                               @RequestParam("user_password") String password,
                               @RequestParam("user_repassword") String re_password,
                               @RequestParam("full_name") String full_name){

        if (!password.equals(re_password)){
            redirectAttrs.addFlashAttribute("passwordError", "Пароли не совпадают");
            return "redirect:/admin_users?passworderror";
        }
//        Users user = new Users(null, email, password, full_name, null);
        Users user = new Users();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(full_name);
        if (!userService.saveUser(user)){
            redirectAttrs.addFlashAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "redirect:/admin_users?username_error";
        }

        return "redirect:/admin_users?success_user_add";
    }

    @PostMapping("/addRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRole(RedirectAttributes redirectAttrs,
                          @RequestParam("role") String role){
        Roles exist_role = userService.getRoleByName(role);
        if (exist_role == null){
            Roles roles = new Roles(null, role);
            userService.addRole(roles);
            return "redirect:/admin_roles?success_role_add";
        }
        return "redirect:/admin_roles?role_error";
    }

    @PostMapping("/editRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editRole(RedirectAttributes redirectAttrs,
                           @RequestParam("role") String role,
                           @RequestParam("role_id") Long role_id){
        Roles current_role = userService.getRole(role_id);
        Roles new_role = userService.getRoleByName(role);
        if (new_role == null){
            current_role.setRole(role);
            userService.addRole(current_role);
            return "redirect:/admin_roles?success_role_add";
        }
        return "redirect:/admin_roles?role_error";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("currentUser", getUserData());
        return "login";
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

    @PostMapping("/editprofile")
    @PreAuthorize("isAuthenticated()")
    public String editProfile(Model model, @RequestParam("email") String email,
                              @RequestParam("full_name") String full_name, RedirectAttributes redirectAttrs){
        Users user = getUserData();
        if (user != null){
            user.setFullName(full_name);
            userService.editUser(user);
            redirectAttrs.addFlashAttribute("currentUser", getUserData());

            List<Brands> brands = itemService.getAllBrands();
            redirectAttrs.addFlashAttribute("brands", brands);

            List<Categories> categories = itemService.getAllCategories();
            redirectAttrs.addFlashAttribute("categories", categories);

            return "redirect:/profile?success_fullname";
        }
        return "redirect:/profile?error";
    }

    @PostMapping("/deleteUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam("user_id") Long user_id){
        Users user = userService.getUserById(user_id);
        Users curr_user = getUserData();
        if (user != curr_user){
            userService.deleteUser(user);
            return "redirect:/admin_users";
        }
        return "redirect:/admin_users?user_delete_error";
    }

    @PostMapping("/editUser")
    @PreAuthorize("isAuthenticated()")
    public String editUser(Model model, @RequestParam("user_id") Long user_id,
                           @RequestParam("email") String email,
                           @RequestParam("full_name") String full_name, RedirectAttributes redirectAttrs){
        Users user = userService.getUserById(user_id);
        if (user != null){
            user.setFullName(full_name);
            userService.editUser(user);

            return "redirect:/admin_users/" + user_id;
        }
        return "redirect:/admin_users/" + user_id + "?error";
    }

    @PostMapping("/uploadavatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam("user_ava") MultipartFile file){

        if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")){
            try {

                Users currentUser = getUserData();

                String picName = DigestUtils.sha1Hex("avatar_" + currentUser.getId() + "_!Picture");

                byte []bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);

                currentUser.setPictureURL(picName);
                userService.editUser(currentUser);

                return "redirect:/profile?success_picture";

            }catch (Exception e){
                e.printStackTrace();
            }
        }


        return "redirect:/";

    }

    @GetMapping(value = "/viewphoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    byte[] viewProfilePhoto(@PathVariable("url") String url) throws IOException {

        String pictureURL = viewPath + defaultPicture;

        if (url != null && !url.equals("null")){
            pictureURL = viewPath + url+".jpg";
        }

        InputStream in;

        try {

            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();

        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath + defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);

    }


    @PostMapping("/editpassword")
    @PreAuthorize("isAuthenticated()")
    public String editPassword(Model model, @RequestParam("old_password") String old_password,
                               @RequestParam("new_password") String new_password,
                               @RequestParam("re_new_password") String re_new_password,
                               RedirectAttributes redirectAttrs){
        Users user = getUserData();
        if (user != null){
            if (passwordEncoder.matches(old_password, user.getPassword())){
                if (new_password.equals(re_new_password)){
                    user.setPassword(passwordEncoder.encode(new_password));
                    userService.editUser(user);

                    List<Brands> brands = itemService.getAllBrands();
                    redirectAttrs.addFlashAttribute("brands", brands);

                    List<Categories> categories = itemService.getAllCategories();
                    redirectAttrs.addFlashAttribute("categories", categories);

                    redirectAttrs.addFlashAttribute("currentUser", getUserData());

                    return "redirect:/profile?success_password";
                }
                return "redirect:/profile?error_newpassword";
            }
            return "redirect:/profile?error_oldpassword";
        }
        return "redirect:/profile?error";
    }

    @PostMapping("/assignrole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String assignRole(Model model, @RequestParam(name = "user_id") Long user_id,
                             @RequestParam(name = "role_id") Long role_id){
        Roles role = userService.getRole(role_id);
        if (role != null){
            Users user = userService.getUserById(user_id);
            if (user != null) {
                List<Roles> roles = user.getRoles();
                if (roles == null) {
                    roles = new ArrayList<>();
                }
                roles.add(role);

                userService.editUser(user);
                return "redirect:/admin_users/" + user_id;
            }
        }
        return "redirect:/admin_users?error_role";
    }

    @PostMapping("/declinerole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String declineRole(Model model, @RequestParam(name = "user_id") Long user_id,
                              @RequestParam(name = "role_id") Long role_id,
                              @RequestParam(name = "roles_size") int size){
        Roles role = userService.getRole(role_id);
        if (role != null && size > 1){
            Users user = userService.getUserById(user_id);
            if (user != null) {
                List<Roles> roles = user.getRoles();
                roles.remove(role);

                userService.editUser(user);
                return "redirect:/admin_users/" + user_id;
            }
        }
        return "redirect:/admin_users/" + user_id + "?role_error_dismiss";
    }

//    @PostMapping("/add_comment")
//    @PreAuthorize("isAuthenticated()")
//    private String addComment(@RequestParam("item_id") Long item_id,
//                              @RequestParam("comment_text") String comment_text,
//                              @RequestParam("user_id") Long user_id){
//
//        Date date = new Date();
//        Users user = userService.getUserById(user_id);
//        ShopItem item = itemService.getItem(item_id);
//        if (item != null){
//            Comments comment = new Comments();
//            comment.setComment(comment_text);
//            comment.setAddedDate(date);
//            comment.setAuthor(user);
//            comment.setItem(item);
//
//            itemService.addComment(comment);
//            return "redirect:/item/" + item_id;
//        }
//        else {
//            return "redirect:/item/" + item_id + "?error";
//        }
//
//    }

}
