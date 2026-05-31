package com.acharya.dikshanta.EcomMed.repository;

import com.acharya.dikshanta.EcomMed.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {


    boolean existsByName(String name);


}
