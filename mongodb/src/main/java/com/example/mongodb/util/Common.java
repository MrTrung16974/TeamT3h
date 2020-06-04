//package com.example.mongodb.util;
//
//import com.example.mongodb.model.Users;
//import com.example.mongodb.repository.UserRepository;
//import com.example.mongodb.services.TokenAuthenticationService;
//import com.sun.el.stream.Optional;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class Common {
//
//    @Autowired
//    TokenAuthenticationService tokenAuthenticationService;
//    @Autowired
//    private UserRepository userRepository;
//    public static boolean validateToKen(String token) {
//        try {
//            String userId = tokenAuthenticationService.readJWT(token);
//            Optional<Users> optionalUser = userRepository.findAll(userId);
//            if(!optionalUser.isPresent()) {
//
//            }
//            return true;
//        }catch (Exception e) {
//            return false;
//        }
//    }
//}
