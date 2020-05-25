package com.example.mongodb.api;

import com.example.mongodb.dto.BaseResponse;
import com.example.mongodb.dto.ProductCast;
import com.example.mongodb.dto.UpdateCastRequest;
import com.example.mongodb.model.Order;
import com.example.mongodb.model.Product;
import com.example.mongodb.model.ProductModel;
import com.example.mongodb.repository.OrderRepository;
import com.example.mongodb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderApiController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

//    get all product
    @RequestMapping("/products")
    public BaseResponse getAllProduct() {
        BaseResponse response = new BaseResponse();
        List<Product> optLstProuct = productRepository.findAll();
//        1, new account
//        2, Just Order
        if (optLstProuct.isEmpty()) {
            response.setCode("99");
            response.setMessage("Found not data");
            response.setData(null);
        }else {
            response.setCode("00");
            response.setMessage("Find data success");
            response.setData(optLstProuct);
        }
        return response;
    }


//    get product prodcut now in user
    @RequestMapping("/products/{idUser}")
    public BaseResponse getListProductInCast(@PathVariable("idUser") String idUser) {
        BaseResponse response = new BaseResponse();
        Optional<Order> optionalOrder = orderRepository.findByBuyerAndStatus(idUser, 1);
//        1, new account
//        2, Just Order
        if (!optionalOrder.isPresent()) {
            Order order = new Order();
            order.setBuyer(idUser);
//            order.setId("");
            order.setStatus(1);
            order.setCreatedAt(new Date());
            orderRepository.save(order);
            response.setCode("00");
            response.setMessage("Create new cast for" + idUser);
            response.setData(order);
        }else {
            Order exits = optionalOrder.get();
            response.setCode("00");
            response.setMessage("'Find order thành công for" + idUser);
            response.setData(exits);
        }
        return response;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public  BaseResponse updateCast(@PathVariable("id") String id,
            @RequestBody UpdateCastRequest updateCastRequest) {
        BaseResponse response = new BaseResponse();
        Optional<Order> optOrder = orderRepository.findById(id);
        if(!optOrder.isPresent()) {
            response.setCode("99");
            response.setMessage("Data not found");
            response.setData(null);
            return response;
        }
        Order exitsOrder = optOrder.get();
        if(exitsOrder.getStatus() != 1) {
            response.setCode("99");
            response.setMessage("Cast invalid");
            response.setData(null);
            return response;
        }

        ProductModel productAdd = null;
        ProductModel productDelete = null;
        Boolean checkAddProduct = true;
//        List get from clent
        List<ProductModel> productInCast = exitsOrder.getListProduct();
        //        checkname
//        list get from database
        List<ProductCast> lstProduct = updateCastRequest.getListProductCast();

        for (ProductCast p : lstProduct) {
//            add product
            if(p.getType() == 1) {
                if(productInCast == null){
                    productInCast = new ArrayList<>();
                }
                if(productInCast.isEmpty()) {
//                        new ko có trong list product tring cast thì thêm mới cho nó
                    productAdd = addListProduct(p);
                    response.setCode("00");
                    response.setMessage("Thêm sản phẩm vào giỏ thành công");
                }else {
                    for (ProductModel pm : productInCast) {
                        if (p.getId().equals(pm.getId()) && p.getNumber() > 0) {
                            checkAddProduct = false;
                        }
                    }
                    for (ProductModel pm : productInCast) {
//                      p.getNumber la so san pham muon them
//                      check sô sản phẩm hiện tại trong DB lớn hon number
                    if (p.getId().equals(pm.getId()) && p.getNumber() > 0) {
                        pm.setNumber(p.getNumber() + pm.getNumber());
                        response.setCode("00");
                        response.setMessage("Đã tăng số lượng thành công");
                    }else if (checkAddProduct){
                        productAdd = addListProduct(p);
                        response.setCode("00");
                        response.setMessage("Thêm sản phẩm vào giỏ thành công");
                        }
                    }
                }
                if(productAdd != null) {
                    productInCast.add(productAdd);
                }
            }
//            delete
            if (p.getType() == 2) {
                for (ProductModel pm : productInCast) {
//                    p.getNumber la so san pham muon them
//                    check sô sản phẩm hiện tại trong DB lớn hon number
                    if(p.getId().equals(pm.getId())
                            && p.getNumber() > 0 &&
                            p.getNumber() <= pm.getNumber()) {
                        pm.setNumber(pm.getNumber() - p.getNumber());
//                        nêu giảm xuốn bằng 0 thì xóa luôn sản phẩm khỏi cast
                        response.setMessage("Đã giảm số lượng sản phẩm");
                        if(pm.getNumber() == 0) {
                            productDelete = pm;
                            response.setMessage("đã xóa sản phẩm");
                        }else {
                            response.setCode("99");
                            response.setMessage("Data invalid");
                        }
                    }
                    response.setCode("00");
                }
                if(productDelete != null) {
                    productInCast.remove(productDelete);
                }
            }
        }
        exitsOrder.setListProduct(productInCast);
        response.setCode("00");
        response.setMessage("Update giỏ hàng thành công");
        response.setData(orderRepository.save(exitsOrder));
        return response;
    }

//    thanh toán
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.POST)
    public BaseResponse paymentOrder(@PathVariable("id") String id) {
        BaseResponse response = new BaseResponse();
        Optional<Order> optOrder = orderRepository.findById(id);
        if(!optOrder.isPresent()) {
            response.setCode("99");
            response.setData(null);
            response.setMessage("Data not found");
        }
        Order exOrder = optOrder.get();
        exOrder.setStatus(2);
        exOrder.setCreatedAt(new Date());
        response.setMessage("Thanh toán thành cống");
        response.setCode("00");
        return response;
    }

    public ProductModel addListProduct(ProductCast cast) {
        ProductModel productModel = new ProductModel();
        productModel.setNumber(cast.getNumber());
        productModel.setId(cast.getId());
//                        lấy thông tin sản phẩm trong db
        Product exitProduct = productRepository.findById(cast.getId()).get();
        productModel.setPrice(exitProduct.getPrice());
        productModel.setImage(exitProduct.getImage());
        productModel.setName(exitProduct.getName());
        return productModel;
    }
}