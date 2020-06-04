package com.example.admin_mvc.repository;

import com.example.admin_mvc.model.ProductModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel,Integer> {

//    @Modifying
//    @Transactional
//    @Query(value = "UPDATE product p SET p.name = :name, p.description = :description, p.price = " +
//            ":price, p.star = :star, p.image = :image WHERE p.id = :id", nativeQuery = true)
//    void updateProduct(@Param("id") int id, @Param("name") String name, @Param("description")
//            String description, @Param("price") Float price, @Param("star") int star, @Param("image") String image);

//    @Modifying
//    @Transactional
    @Query(value = "SELECT * from product p WHERE p.name LIKE %:name%", nativeQuery = true)
    List<ProductModel> searchProduct(@Param("name") String name);

    List<ProductModel> findByNameContaining(String name, Pageable pageable);
}
