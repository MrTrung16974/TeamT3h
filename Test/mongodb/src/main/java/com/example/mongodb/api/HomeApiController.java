package com.example.mongodb.api;

import com.example.mongodb.model.Product;
import com.example.mongodb.repository.OrderRepository;
import com.example.mongodb.repository.ProductRepository;
import com.example.mongodb.services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class HomeApiController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderServices orderServices;

    @RequestMapping("/product/search")
    public List<Product> index(Model model,
                               @RequestParam(value = "name", required = false)String name,
                               @RequestParam("page") int page,
                               @RequestParam("perPage") int perPage){
        List<Product> lstProduct = productRepository.findAll();
        return lstProduct;
    }
}
