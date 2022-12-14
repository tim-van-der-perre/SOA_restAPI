package com.Create.API.rest.Controller;


import com.Create.API.rest.Models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class ApiController {

    @Autowired
    public com.Create.API.rest.Repo.ProductRepo productRepo;

    @GetMapping("/")
    public String getProduct() {
        return "Welcome," + "\n" +
                "Dit is de webAPI van Tim Van der Perre." + "\n" + "\n"
                + "hoe te gebruiken:" + "\n"
                + "/products (haalt alle producten op)\n" +
                "  -/product/{id} (haalt product op met {id})\n" +
                "  -/add (voegt product toe met automatisch gegenereerde id)\n" +
                "  -/delete/{id} (verwijdert product op met {id})\n" +
                "  -/update/{id} (update product op met {id})";
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable long id) {
        return productRepo.findById(id).get();
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody Product product) {
         productRepo.save(product);
         return "saved ...";
    }
    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable long id, @RequestBody Product product) {
        Product updatedProduct = productRepo.findById(id).get();
        if (updatedProduct == null) {
            System.out.println("apicontroller vind niet.");
            return "niks gevonden";}
        updatedProduct.setDescription(product.getDescription());
        //updatedProduct.setId(id);
        updatedProduct.setTitle(product.getTitle());
        updatedProduct.setType(product.getType());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setPicture_url(product.getPicture_url());
        productRepo.save(updatedProduct);
        return "updated  ...";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable long id) {
        Product deleteProduct = productRepo.findById(id).get();
        if (deleteProduct == null) {return "niks gevonden";}
        productRepo.delete(deleteProduct);
        return "deleted ...";
    }

}
