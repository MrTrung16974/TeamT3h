package com.example.mongodb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongodbApplication {
//        implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MongodbApplication.class, args);
    }

//    @Autowired
//    ProductRepository productRepository;

//    @Override
//    public void  run(String...args) throws Exception {
//        for (int i = 0; i < 10; i++) {
//            Product p = new Product();
//            p.setName("Name " + i);
//            p.setPrice((double)i*100);
//            p.setNumber(i);
//            p.setId(""+i);
//            productRepository.save(p);
//        }
//    }
}
