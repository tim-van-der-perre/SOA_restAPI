package com.Create.API.rest.Repo;

import com.Create.API.rest.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
