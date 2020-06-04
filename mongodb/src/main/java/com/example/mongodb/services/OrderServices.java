package com.example.mongodb.services;

import com.example.mongodb.model.Order;
import com.example.mongodb.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServices {
    @Autowired
    MongoTemplate mongoTemplate;

    //tìm kiếm product theo name và giá tiền
    public List<Product> search(String name, long price, Pageable pageable){
        Query query = new Query();
        //check name tồn tài mới thêm điều kiện search
        if(!name.isEmpty()){
            query.addCriteria(Criteria.where("name").regex(name));
        }
        //check giá lớn hơn 0  mới thêm điều kiện search
        if(price > 0){
            query.addCriteria(Criteria.where("price").lte(price));
        }
        //nếu khác null là phân trang và sắp xếp theo pageanable
        if(pageable != null){
            query.with(pageable);
        }
        List<Product> lstProduct = mongoTemplate.find(query, Product.class);
        return lstProduct;
    }
}
