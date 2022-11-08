//package com.example.mongojpapractice.controller.test;
//
//import com.example.mongojpalogic.test.products.document.Products;
//import com.example.mongojpalogic.test.products.repository.ProductsRepository;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RequiredArgsConstructor
//@RequestMapping("/api/test")
//@RestController
//public class ProductsController {
//
//    private final ProductsRepository productsRepository;
//
//    @GetMapping("/products")
//    public ResponseEntity<Object> getAllProducts() {
//        List<Products> products = productsRepository.findAll();
//        return ResponseEntity.ok(products);
//    }
//}
