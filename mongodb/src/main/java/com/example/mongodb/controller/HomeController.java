package com.example.mongodb.controller;

import com.example.mongodb.model.Product;
import com.example.mongodb.repository.OrderRepository;
import com.example.mongodb.repository.ProductRepository;
import com.example.mongodb.services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderServices orderServices;
    @RequestMapping("/index")
    public String index(Model model){
        List<Product> lstProduct = productRepository.findAll();
        model.addAttribute("lstProduct",lstProduct);
        return "index";
    }
    @RequestMapping("/home")
    public String home(Model model){
        List<Product> lstProduct = productRepository.findAll();
        model.addAttribute("lstProduct",lstProduct);
        return "home";
    }
}
