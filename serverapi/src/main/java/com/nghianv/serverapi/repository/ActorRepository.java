package com.nghianv.serverapi.repository;

import com.nghianv.serverapi.model.ActorModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<ActorModel, Integer> {
    // select * from product where name =?
//    List<ActorModel> findByName(String name);
    // select * from product where name LIKE %?%
    Page<ActorModel> findByNameContaining(String name, Pageable pageable);

}
