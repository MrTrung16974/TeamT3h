package com.example.admin_mvc.api;

import com.example.admin_mvc.dto.BaseResponse;
import com.example.admin_mvc.dto.ProductRequest;
import com.example.admin_mvc.model.ProductModel;
import com.example.admin_mvc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/v1/api")
public class ProductAPIController {

    @Autowired
    private ProductRepository productRepository;

//    @RequestParam(value =/ "/product", method= RequestMethod.GET)
    @GetMapping("/products")
    public BaseResponse getAllProduct() {
        BaseResponse response = new BaseResponse();
        response.setMessge("Success");
        response.setCode("00");
        response.setData(productRepository.findAll());
        return response;
    }

    @GetMapping("/product")
    public BaseResponse getDescriptionProduct(@RequestParam("id") Integer id) {
        BaseResponse response = new BaseResponse();
        Optional<ProductModel> optProduct = productRepository.findById(id);
        if(optProduct.isPresent()) {
            response.setCode("00");
            response.setMessge("Success");
            response.setData(optProduct.get());
        }else {
            response.setCode("400");
            response.setMessge("Error");
            response.setData(null);
        }

        return response;
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public BaseResponse createProduct(@RequestBody ProductRequest productRequest) {
        BaseResponse response = new BaseResponse();
        if(productRequest.getName().isEmpty() &&
                productRequest.getDescription().isEmpty() && productRequest.getPrice().isEmpty()
                || Float.parseFloat(productRequest.getPrice()) <= 0 && Integer.parseInt(productRequest.getStar()) <= 0
                && productRequest.getImage().isEmpty()) {
            response.setCode("400");
            response.setMessge("Data invalid");
            response.setData(null);
            return response;
        }

        ProductModel newProduct = new ProductModel();
        newProduct.setName(productRequest.getName());
        newProduct.setDescription(productRequest.getDescription());
        newProduct.setPrice(Float.parseFloat(productRequest.getPrice()));
        newProduct.setStar(Integer.parseInt(productRequest.getStar()));
        newProduct.setImage(productRequest.getImage());
        ProductModel exits = productRepository.save(newProduct);

        response.setCode("00");
        response.setMessge("Create new Product Success");
        response.setData(exits);
        return response;
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.PUT)
    public BaseResponse updateProduct(@PathVariable Integer id,
                                      @RequestBody ProductRequest productRequest) {
        BaseResponse response = new BaseResponse();
        Optional<ProductModel> optProduct = productRepository.findById(id);

        if(optProduct.isPresent()) {
            ProductModel exitProduct =  optProduct.get();
            if(!productRequest.getName().isEmpty()) {
                exitProduct.setName(productRequest.getName());
            }
            if(!productRequest.getDescription().isEmpty()) {
                exitProduct.setDescription(productRequest.getDescription());
            }
            if(!productRequest.getPrice().isEmpty() && Float.parseFloat(productRequest.getPrice()) > 0) {
                exitProduct.setPrice(Float.parseFloat(productRequest.getPrice()));
            }
            response.setCode("00");
            response.setMessge("Update Product Success");
            response.setData(productRepository.save(exitProduct));
        }else {
            response.setCode("99");
            response.setMessge("Data Not found");
            response.setData(null);
        }
        return response;
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public BaseResponse deleteProduct(@PathVariable Integer id) {
        BaseResponse response = new BaseResponse();
        try {
            Optional<ProductModel> optProduct = productRepository.findById(id);
            if(!optProduct.isPresent()) {
                response.setCode("99");
                response.setMessge("Data Not found");
                response.setData(null);
            }else {
                productRepository.deleteById(id);
                response.setCode("00");
                response.setMessge("Delete Product Success");
                response.setData(optProduct.get());
            }
        }catch (Exception e) {
            response.setCode("900");
            response.setMessge("System error :" +  e.getStackTrace());
            response.setData(null);
        }
        return response;
    }

    @RequestMapping(value = "/product/search", method = RequestMethod.GET)
    public BaseResponse searchProduct(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam("page") int page,
                                      @RequestParam("perPage") int perPage) {
        BaseResponse response = new BaseResponse();
        try {
            Pageable pageable = PageRequest.of(page, perPage,
                    Sort.by(Sort.Direction.DESC, "name"));
            List<ProductModel> lstProduct = productRepository.findByNameContaining(name, pageable);
            if(lstProduct.isEmpty()) {
                response.setCode("99");
                response.setMessge("Data Not found");
                response.setData(null);
            }else {
                response.setCode("200");
                response.setMessge("List product by key : " + name);
                response.setData(lstProduct);
            }
        }catch (Exception e) {
            response.setCode("900");
            response.setMessge("System error :" +  e.getStackTrace());
            response.setData(null);
        }
        return response;
    }
}
