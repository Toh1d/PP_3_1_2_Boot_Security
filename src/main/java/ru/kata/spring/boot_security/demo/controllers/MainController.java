//package ru.kata.spring.boot_security.demo.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.kata.spring.boot_security.demo.models.User;
//import ru.kata.spring.boot_security.demo.services.UserServiceImp;
//
//import java.security.Principal;
//
//@RestController
//public class MainController {
//
//    private final UserServiceImp userServiceImp;
//
//    @Autowired
//    public MainController(UserServiceImp userServiceImp) {
//        this.userServiceImp = userServiceImp;
//    }
//
//
//    //Важно понимать, что Principal != User. сделать каст (User) principal.getName() не получится
//    @GetMapping("/authenticated")
//    public String pageForAuthenticatedUsers(Principal principal) {
//        //Это как получить email можно
//        User user = userServiceImp.findUserByUsername(principal.getName());
//        //это как получить пользователя можно
////        Authentication a = SecurityContextHolder.getContext().getAuthentication();
//        return "secured part of web services : " + user.getUsername() + " " + user.getEmail();
//    }
//}
