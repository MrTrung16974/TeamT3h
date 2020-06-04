package com.example.admin_mvc.controller;

import com.example.admin_mvc.api.ProductAPIController;
import com.example.admin_mvc.dto.BaseResponse;
import com.example.admin_mvc.dto.ProductRequest;
import com.example.admin_mvc.model.ProductModel;
import com.example.admin_mvc.repository.ProductRepository;
import com.example.admin_mvc.service.StoreFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAPIController productAPIController;

    @Autowired
    StoreFileService storeFileService;

    private List<ProductModel> lstProduct;

    @GetMapping(value = "/home")
    public String homeProduct(Model model, @RequestParam(value = "isSuccess", required = false) Boolean isSuccess){
        String message = "";
        isSuccess = false;
        if(lstProduct == null ) {
            lstProduct = productRepository.findAll();
        }
        model.addAttribute("lstProduct",lstProduct);
        if(isSuccess) {
            message = "Error";
        }
        model.addAttribute("message", message);
        return "index";
    }

    @GetMapping(value = "/test-api")
    @ResponseBody
    public String testAPT() {
        return "Hello APT";
    }

//    get all list product
    @GetMapping(value = "/api/product")
    @ResponseBody
    public BaseResponse getAllProduct(@RequestParam(value = "i", defaultValue = "0") Integer i) {
        BaseResponse response = new BaseResponse();
        if(i == 1) {
            response.setCode("00");
            response.setMessge("System errors");
            response.setData(null);
        }else {
            response.setCode("00");
            response.setMessge("Get all product successfuly");
            response.setData(lstProduct);
        }
        return response;
    }


    @GetMapping(value = "/search")
    public String searchProduct(Model model, @RequestParam("keyword") String keyword){
        String message = "";
        lstProduct = productRepository.searchProduct(keyword);
        model.addAttribute("lstProduct",lstProduct);
        model.addAttribute("message", message);
        return "redirect:/home";
    }

    @DeleteMapping(value = "/delete/{id}" )
    public String deleteProduct(Model model, @PathVariable("id") Integer id) {
        String message = null;
        if(id != null) {
            message = "Bạn đã xóa thành công product";
            productAPIController.deleteProduct(id);
            lstProduct = productRepository.findAll();
        }else {
            message = "Không tìm thấy sản phẩm";
        }
        model.addAttribute("message", message);
        return "redirect:/home";
    }

    @PostMapping(value = "/create-product")
    @ResponseBody //không trả về trang web
    public void createNewProduct(@RequestBody ProductRequest productRequest){
        String message = null;
        if(productRequest != null) {
            message = "Bạn đã thêm thành công product";
            productAPIController.createProduct(productRequest);
            lstProduct = productRepository.findAll();
        }else {
            message = "Bạn nhập sai òi";
        }
    }

    @RequestMapping(value = "/edit/{id}/detail")
    public String editProduct(Model model, @PathVariable("id") Integer id) {
        ProductModel productModel = null;
        for (ProductModel p : lstProduct) {
            if(p.getId() == id) {
                productModel = p;
            }
        }
        model.addAttribute("product", productModel);
        return "detail";
    }

    @PutMapping(value = "/edit/{id}")
    @ResponseBody
    public int editProduct(Model model,@PathVariable("id") int id, @RequestBody ProductRequest productRequest) {
        String message = null;
        System.out.println(productRequest);
//        ProductModel productModel = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        if (productRequest.getDescription() != null && !productRequest.getDescription().isEmpty()) {
            message = "Bạn đã thêm thành công product";
            productAPIController.updateProduct(id ,productRequest);
            lstProduct = productRepository.findAll();
        } else {
            message = "Bạn nhập sai òi";
            return 4;
        }
        model.addAttribute("message", message);
        return 0;
    }

    @PostMapping("/upload")
    @ResponseBody
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {
        String fileName = "";
        String fileLink = "http://localhost:8080/link/";
        try{
            if(file.isEmpty()) {
                throw new Exception();
            }
            fileName = storeFileService.store(file);
            fileLink += fileName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileLink;
    }
}
