package com.example.mongodb.controller;

import com.example.mongodb.dto.BaseResponse;
import com.example.mongodb.model.Product;
import com.example.mongodb.services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductAPIController {
    @Autowired
    OrderServices orderServices;

    @RequestMapping("/search")
    public BaseResponse searchProduct(@RequestParam(value = "name",required = false)String name,
                                      @RequestParam(value = "price",required = false) Long price,
                                      @RequestParam(value = "page") int page,
                                      @RequestParam(value = "pageSize") int pageSize){
        BaseResponse response = new BaseResponse();
        Pageable pageable = PageRequest.of(page,pageSize,
                Sort.by(Sort.Direction.DESC,"price"));
        List<Product> lstProduct = orderServices.search(name,price,pageable);
        response.setData(lstProduct);
        response.setCode("00");
        response.setMessage("Tìm kiếm thành công");
        return response;
    }
}
