package com.example.mongodb.dto;

import java.util.List;

public class UpdateCartRequest {
    private List<ProductCart> lstProductCart;
    private String name;

    public List<ProductCart> getLstProductCart() {
        return lstProductCart;
    }

    public void setLstProductCart(List<ProductCart> lstProductCart) {
        this.lstProductCart = lstProductCart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
