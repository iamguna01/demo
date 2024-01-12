package com.tao.digital.test.demo.service;


import com.tao.digital.test.demo.dao.ApprovalQueueProductRepository;
import com.tao.digital.test.demo.dao.ApprovalQueueRepository;
import com.tao.digital.test.demo.dao.ProductRepository;
import com.tao.digital.test.demo.entity.ApprovalQueue;
import com.tao.digital.test.demo.entity.ApprovalQueueProduct;
import com.tao.digital.test.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ApprovalQueueProductRepository approvalQueueProductRepository;
    private final ApprovalQueueRepository approvalQueueRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ApprovalQueueProductRepository approvalQueueProductRepository,
                          ApprovalQueueRepository approvalQueueRepository) {
        this.productRepository = productRepository;
        this.approvalQueueProductRepository = approvalQueueProductRepository;
        this.approvalQueueRepository = approvalQueueRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> listActiveProducts() {
        return productRepository.findByStatusTrueOrderByPostedDateDesc();
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String productName, BigDecimal minPrice, BigDecimal maxPrice,
                                        LocalDateTime minPostedDate, LocalDateTime maxPostedDate) {
        return productRepository.searchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
    }

    @Transactional
    public void createProduct(Product product) {
        validateProductPrice(product);
        if(product.getPrice().compareTo(new BigDecimal("5000")) <= 0) {
            productRepository.save(product);
        }
    }

// Update the method in ProductService.java

    @Transactional
    public void updateProduct(Long productId, Product updatedProduct) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            // Check if the updated price is more than 50% of the previous price
            BigDecimal updatedPrice = updatedProduct.getPrice();
            BigDecimal previousPrice = existingProduct.getPrice();

            if (updatedPrice.compareTo(previousPrice.multiply(new BigDecimal("1.5"))) > 0) {
                // If the update exceeds the threshold, create or update ApprovalQueueProduct
                ApprovalQueueProduct approvalQueueProduct = approvalQueueProductRepository.findById(productId)
                        .orElse(new ApprovalQueueProduct());  // Create if not exists

                // Update or set values for ApprovalQueueProduct
                approvalQueueProduct.setId(productId);
                approvalQueueProduct.setName(updatedProduct.getName());
                approvalQueueProduct.setPrice(updatedProduct.getPrice());
                approvalQueueProduct.setStatus(updatedProduct.isStatus());
                approvalQueueProduct.setPostedDate(existingProduct.getPostedDate());

                approvalQueueProductRepository.save(approvalQueueProduct);

                // Create or update ApprovalQueue
                ApprovalQueue approvalQueue = approvalQueueRepository.findByApprovalQueueProduct(approvalQueueProduct)
                        .orElse(new ApprovalQueue());  // Create if not exists

                // Update or set values for ApprovalQueue
                approvalQueue.setApprovalQueueProduct(approvalQueueProduct);
                approvalQueue.setRequestDate(LocalDateTime.now());

                approvalQueueRepository.save(approvalQueue);
            }else {
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setStatus(updatedProduct.isStatus());
                productRepository.save(existingProduct);
            }
        } else {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }
    }


    @Transactional
    public void deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            productRepository.delete(product);
            ApprovalQueueProduct approvalQueueProduct = approvalQueueProductRepository.findById(productId)
                    .orElse(new ApprovalQueueProduct());  // Create if not exists

            // Update or set values for ApprovalQueueProduct
            approvalQueueProduct.setId(productId);
            approvalQueueProduct.setName(product.getName());
            approvalQueueProduct.setPrice(product.getPrice());
            approvalQueueProduct.setStatus(product.isStatus());
            approvalQueueProduct.setPostedDate(product.getPostedDate());

            approvalQueueProductRepository.save(approvalQueueProduct);

            // Create or update ApprovalQueue
            ApprovalQueue approvalQueue = approvalQueueRepository.findByApprovalQueueProduct(approvalQueueProduct)
                    .orElse(new ApprovalQueue());  // Create if not exists

            // Update or set values for ApprovalQueue
            approvalQueue.setApprovalQueueProduct(approvalQueueProduct);
            approvalQueue.setRequestDate(LocalDateTime.now());

            approvalQueueRepository.save(approvalQueue);
        } else {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }
    }

    @Transactional(readOnly = true)
    public List<ApprovalQueue> getApprovalQueue() {
        return approvalQueueRepository.findAllByOrderByRequestDateAsc();
    }

    @Transactional
    public void approveProduct(Long approvalId) {
        Optional<ApprovalQueue> optionalApproval = approvalQueueRepository.findById(approvalId);

        if (optionalApproval.isPresent()) {
            ApprovalQueue approval = optionalApproval.get();
            approvalQueueRepository.delete(approval);
        } else {
            throw new IllegalArgumentException("Approval not found with id: " + approvalId);
        }
    }

    @Transactional
    public void rejectProduct(Long approvalId) {
        Optional<ApprovalQueue> optionalApproval = approvalQueueRepository.findById(approvalId);

        if (optionalApproval.isPresent()) {
            // Optionally, you can implement logic to handle rejection
            ApprovalQueue approval = optionalApproval.get();
            approvalQueueRepository.delete(approval);
        } else {
            throw new IllegalArgumentException("Approval not found with id: " + approvalId);
        }
    }

    private void validateProductPrice(Product product) {
        BigDecimal maxAllowedPrice = new BigDecimal("10000");
        if (product.getPrice().compareTo(maxAllowedPrice) <= 0) {
            if (product.getPrice().compareTo(new BigDecimal("5000")) > 0) {
                // If the price is more than $5000, create or update ApprovalQueueProduct
                ApprovalQueueProduct approvalQueueProduct;
                if(product.getId() != null) {
                 approvalQueueProduct = approvalQueueProductRepository.findById(product.getId())
                        .orElse(new ApprovalQueueProduct());
                }
                else
                    approvalQueueProduct = new ApprovalQueueProduct();

                // Update or set values for ApprovalQueueProduct

                approvalQueueProduct.setName(product.getName());
                approvalQueueProduct.setPrice(product.getPrice());
                approvalQueueProduct.setStatus(product.isStatus());
                approvalQueueProduct.setPostedDate(product.getPostedDate());

                approvalQueueProductRepository.save(approvalQueueProduct);

                // Create or update ApprovalQueue
                ApprovalQueue approvalQueue = approvalQueueRepository.findByApprovalQueueProduct(approvalQueueProduct)
                        .orElse(new ApprovalQueue());  // Create if not exists

                // Update or set values for ApprovalQueue
                approvalQueue.setApprovalQueueProduct(approvalQueueProduct);
                approvalQueue.setRequestDate(LocalDateTime.now());

                approvalQueueRepository.save(approvalQueue);
            }
        }else {
                throw new IllegalArgumentException("Product price must not exceed $10,000.");
        }
    }
}
