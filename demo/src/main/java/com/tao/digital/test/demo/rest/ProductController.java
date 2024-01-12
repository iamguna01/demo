package com.tao.digital.test.demo.rest;

import com.tao.digital.test.demo.entity.ApprovalQueue;
import com.tao.digital.test.demo.entity.Product;
import com.tao.digital.test.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listActiveProducts() {
        List<Product> activeProducts = productService.listActiveProducts();
        return new ResponseEntity<>(activeProducts, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) LocalDateTime minPostedDate,
            @RequestParam(required = false) LocalDateTime maxPostedDate) {
        List<Product> searchedProducts = productService.searchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
        return new ResponseEntity<>(searchedProducts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            productService.createProduct(product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        try {
            productService.updateProduct(productId, product);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/approval-queue")
    public ResponseEntity<List<ApprovalQueue>> getApprovalQueue() {
        List<ApprovalQueue> approvalQueue = productService.getApprovalQueue();
        return new ResponseEntity<>(approvalQueue, HttpStatus.OK);
    }

    @PutMapping("/approval-queue/{approvalId}/approve")
    public ResponseEntity<?> approveProduct(@PathVariable Long approvalId) {
        try {
            productService.approveProduct(approvalId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/approval-queue/{approvalId}/reject")
    public ResponseEntity<?> rejectProduct(@PathVariable Long approvalId) {
        try {
            productService.rejectProduct(approvalId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
