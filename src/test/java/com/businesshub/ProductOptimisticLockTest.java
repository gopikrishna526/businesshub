package com.businesshub;

import java.math.BigDecimal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.businesshub.entity.ProductEntity;
import com.businesshub.repository.ProductRepository;

@SpringBootTest
class ProductOptimisticLockTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void optimisticLockingTest() {

        // Transaction 1 - User A reads product
        ProductEntity productA = transactionTemplate.execute(status -> {

            ProductEntity product = productRepository.findById(1L)
                    .orElseThrow();

            System.out.println("User A version: " + product.getVersion());

            return product;
        });

        BigDecimal originalPrice = productA.getPrice();

        // Transaction 2 - User B updates and commits
        transactionTemplate.executeWithoutResult(status -> {

            ProductEntity productB = productRepository.findById(1L)
                    .orElseThrow();

            System.out.println("User B version: " + productB.getVersion());

            productB.setPrice(
                    productB.getPrice().add(BigDecimal.ONE)
            );

            entityManager.flush();

            System.out.println("User B update completed.");
            System.out.println("User B new version: " + productB.getVersion());
        });

        // Transaction 3 - User A tries to update stale data
        try {

            transactionTemplate.executeWithoutResult(status -> {

                productA.setPrice(
                        productA.getPrice().add(new BigDecimal("2"))
                );

                productRepository.save(productA);

                entityManager.flush();

                System.out.println("User A update unexpectedly succeeded.");
            });

        } catch (Exception e) {

            System.out.println(
                    "Optimistic locking conflict detected: "
                            + e.getClass().getName()
            );
        }

        // Restore original price
        transactionTemplate.executeWithoutResult(status -> {

            ProductEntity currentProduct = productRepository.findById(1L)
                    .orElseThrow();

            currentProduct.setPrice(originalPrice);

            entityManager.flush();

            System.out.println("Original price restored.");
        });
    }
}