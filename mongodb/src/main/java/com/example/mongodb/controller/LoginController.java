package com.example.mongodb.controller;

import com.example.mongodb.dto.BaseResponse;
import com.example.mongodb.model.Users;
import com.example.mongodb.repository.UserRepository;
import com.example.mongodb.services.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenAuthenticationService tokenAuthenticationService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public BaseResponse login(@RequestParam(value = "username" )String username,
                              @RequestParam(value = "password" )String password) {
        BaseResponse response = new BaseResponse();
        try {
            if (!username.isEmpty() && !password.isEmpty()) {
                Optional<Users> optUser = userRepository.findById(username);
                if (!optUser.isPresent()) {
                    throw new Exception("username or password invalid");
                }
                Users users = optUser.get();
                if(!passwordEncoder.matches(password, users.getPassword())) {
                    throw new Exception("Password invalid");
                }
                response.setData("00");
                response.setMessage("Login Success");
                response.setData(tokenAuthenticationService.generateJWT(users.getId()));
            } else {
                response.setData("00");
                response.setMessage("Error");
                response.setData(null);
            }
        }catch (Exception e) {
            response.setData("99");
            response.setMessage("Error");
            response.setData(e.getMessage());
        }
        return response;
    }

    @GetMapping("/getInfoUser")
    public BaseResponse getInfo(@RequestHeader("Authen") String token) {
        BaseResponse response = new BaseResponse();
        try {
            if(!tokenAuthenticationService.validateToKen(token)) {
                throw new Exception("Token invalid");
            }
            response.setData("00");
            response.setMessage("get data thanh công");
            response.setData("User info");
        }catch (Exception e) {
            response.setData("99");
            response.setMessage("Error" );
            response.setData(e.getMessage());
        }
        return response;
    }



}
