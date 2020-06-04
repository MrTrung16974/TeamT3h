package com.example.mongodb;

import com.example.mongodb.model.Product;
import com.example.mongodb.repository.ProductRepository;
import com.example.mongodb.services.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongodbApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MongodbApplication.class, args);
    }

    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    ProductRepository productRepository;
    @Override
    public void run(String... args) throws Exception {
//        System.out.println("Go here");
//        for(int i = 0;i < 10;i++){
//            Product p = new Product();
//            p.setName("Name "+i);
//            p.setNumber(i);
//            p.setPrice((double)i*1000);
//            p.setId(""+i);
//            productRepository.save(p);
//            System.out.println("Add user " +i);
//        }
//        System.out.println("End here");
//        String token = tokenAuthenticationService.generateJWT("userId");
//        System.out.println(token);
//        String userName = tokenAuthenticationService.readJWT(token);
//        System.out.println(userName);
    }
}
