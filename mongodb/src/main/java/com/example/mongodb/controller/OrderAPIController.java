package com.example.mongodb.controller;

import com.example.mongodb.dto.BaseResponse;
import com.example.mongodb.dto.ProductCart;
import com.example.mongodb.dto.UpdateCartRequest;
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
public class OrderAPIController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

    //lấy sản phẩm hiện tại trong giỏ hàng của user theo id
    //Nếu không có tự động thêm mới giỏ hàng trống
    @RequestMapping("/products/{id}")
    public BaseResponse getListProductInCart(
            @PathVariable("id")String id){
        //Order có trạng thái = 1 chính là giỏ hàng hiện tại của user
        BaseResponse response = new BaseResponse();
        Optional<Order> optionalOrder = orderRepository.findByBuyerAndStatus(id,1);
        //Không tồn tại order nào có trạng thái = 1 tức là :
        //1,New account : Là tài khoản mới đc tạo chưa có giỏ hàng
        //2,Just ordered : Hóa đơn mới đc chuyển từ 1 -> 2.
        // Không còn hóa đơn nào ở trạng thái là 1 -> tức là giỏ hàng
        if(!optionalOrder.isPresent()){
            //Thực hiện tạo mới

            Order order = new Order();
            order.setBuyer(id);
            //order.setId("MH235");
            order.setStatus(1);
            orderRepository.save(order);

            response.setCode("00");
            response.setMessage("Create new cart for user " + id);
            response.setData(order);
        }else{
            Order exits = optionalOrder.get();
            response.setCode("00");
            response.setMessage("Get product in cart for user " + id);
            response.setData(exits);
        }
        return response;
    }

    //cập nhập lại giỏ hàng ( thêm, xóa, sửa ) theo id của người dùng
    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    public BaseResponse updateCart(
            //map với {id} ở trên requestMapping
            @PathVariable("id") String id,
            @RequestBody UpdateCartRequest updateCartRequest){
        BaseResponse response = new BaseResponse();
        //Tìm kiếm giỏ hàng trong DB
        Optional<Order> optOrder = orderRepository.findById(id);
        //nếu không tồn tại
        if(!optOrder.isPresent()){
            response.setData(null);
            response.setMessage("Data not found");
            response.setCode("99");
            return response;
        }
        //tồn tài thì lấy ra thông tin giỏ hàng
        Order exitsOrder = optOrder.get();
        //Trạng thái hóa đơn khác 1 tức là k phải giỏ hàng
        //thông báo lỗi
        if(exitsOrder.getStatus() != 1){
            response.setData(null);
            response.setMessage("Cart invalid");
            response.setCode("98");
            return response;
        }
        //Có thể check name của order có giống với
        //name của user gửi trong UpdateRequest
        //nếu khác không cho update

        //Code here

        //Lấy danh sách sản phẩm mà user muốn thêm
        List<ProductCart> lstProduct =
                updateCartRequest.getLstProductCart();

        //Lấy danh sách sản phẩm trong giỏ hàng hiện tại
        List<ProductModel> productInCart = exitsOrder.getListProduct();

        //Duyệt danh sách sản phẩm người dùng gửi lên
        for(ProductCart p : lstProduct){
            //kiểm tra type = 1 : là thêm || 2 là xóa
            //add product
            if(p.getType() == 1){
                //Sản phẩm trong giỏ hàng hiện tại trong DB không có
                if (productInCart == null) {
                    //Tạo mới giổ hàng trống
                    productInCart = new ArrayList<>();
                    //Tạo đối tượng sản phẩm trong giỏ hàng với các thông tin sau
                    ProductModel productModel = new ProductModel();
                    //số lượng client gửi lên
                    productModel.setNumber(p.getNumber());
                    //Mã sp client gửi lên
                    productModel.setId(p.getId());

                    //lấy thông tin sản phẩm trong DB
                    Product exitsProduct = productRepository.findById(p.getId()).get();
                    //Thêm giá
                    productModel.setPrice(exitsProduct.getPrice());
                    //Thêm name
                    productModel.setName(exitsProduct.getName());
                    //Thêm ảnh
                    productModel.setImage(exitsProduct.getImage());

                    //Thêm sản phẩm vưa tạo vào Danh sách sẩn phẩm trong giỏ
                    productInCart.add(productModel);

                    response.setCode("00");
                    response.setMessage("Thêm sản phẩm vào giỏ thành công");
                }else{
                    boolean isHaveInList = false;
                    //Duyệt sản phẩm trong danh sách sp trong giỏ hàng của DB đang có sẵn
                    for(int i=0; i < productInCart.size();i++){
                        ProductModel pm = productInCart.get(i);
                        //p.getNumber là số sản phẩm người dùng muốn thêm
                        //pm.getNumber là số sản phẩm hiện tại trong gio hàng của DB
                        //so sánh mã số sản phẩm hiện tại trong DB và mã người dùng
                        //gửi lên nếu = nhau tăng số lượng lên
                        if(p.getId().equals(pm.getId()) && p.getNumber() > 0){
                            //Đặt lại số lượng của sản phẩm trong giỏ hàng
                            pm.setNumber(pm.getNumber() + p.getNumber());
                            isHaveInList = true;
                            response.setCode("00");
                            response.setMessage("Đã tăng số lượng thành công");
                        }
                    }

                    if(!isHaveInList){
                        //Nếu sản phẩm gửi lên không có trong list product của
                        // cart thì thêm mới sản phẩm đó vào trong ds sản phẩm của
                        // Giỏ hàng
                        //Tạo mới model và set các trường
                        ProductModel productModel = new ProductModel();
                        //Số lượng
                        productModel.setNumber(p.getNumber());
                        productModel.setId(p.getId());
                        //lấy thông tin sản phẩm trong DB
                        Product exitsProduct = productRepository.findById(p.getId()).get();
                        productModel.setPrice(exitsProduct.getPrice());
                        productModel.setName(exitsProduct.getName());
                        productModel.setImage(exitsProduct.getImage());

                        //lưu lại sản phẩm vào giỏ hàng
                        productInCart.add(productModel);

                        response.setCode("00");
                        response.setMessage("Thêm sản phẩm vào giỏ thành công");

                    }
                }
            }
            //type = 2 là xóa
            //delete
            if(p.getType() == 2){
                boolean isHaveInList = false;
                for(int i = 0;i < productInCart.size();i++){
                    ProductModel pm =productInCart.get(i);
                    //p.getNumber là số lượng sản phẩm muốn thêm
                    //pm.getNumber là số lượng sản phẩm hiện tại
                    //so sánh mã số sản phẩm hiện tại trong DB
                    //và mã sp người dùng gửi lên
                    //nếu trùng thực hiện giảm số lượng sản phẩm
                    if(p.getId().equals(pm.getId()) && p.getNumber() > 0 &&
                            p.getNumber() <= pm.getNumber()){

                        //Giẩm số lượng sản phẩm
                        pm.setNumber(pm.getNumber() - p.getNumber());
                        response.setMessage("Đã giảm số lượng sản phẩm");

                        //Nếu giảm xuống == 0 thì xóa luôn sản phẩm khỏi list
                        if(pm.getNumber() == 0){
                            productInCart.remove(pm);
                            response.setMessage("Đã xóa sản phẩm ");
                        }

                        response.setCode("00");
                    }
                }
                if(!isHaveInList){
                    response.setCode("99");
                    response.setMessage("Data invalid");
                }
            }
        }

        //Lưu lại danh sách sản phẩm vào giở hàng của người dùng
        exitsOrder.setListProduct(productInCart);
        //Lưu lại giỏ hàng vào Database
        response.setData(orderRepository.save(exitsOrder));
        response.setCode("00");
        response.setMessage("Update giỏ hàng thành công");
        return response;
    }

    @RequestMapping(value = "/payment/{id}",
            method = RequestMethod.POST)
    public BaseResponse paymentOrder(
            @PathVariable("id") String id
    ){
        BaseResponse response = new BaseResponse();
        Optional<Order> optOrder = orderRepository.findById(id);
        if(!optOrder.isPresent()){
            response.setCode("99");
            response.setMessage("Data not found");
            return response;
        }
        Order exOrder = optOrder.get();
        exOrder.setStatus(2);
        exOrder.setCreatedAt(new Date());
        response.setMessage("Thanh toán thành công");
        response.setCode("00");
        return response;
    }
}
