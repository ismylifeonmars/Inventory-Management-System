package com.ravemaster.inventory.repository;

import com.ravemaster.inventory.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p.id FROM Product p")
    Page<UUID> findAllProductIds(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category c LEFT JOIN FETCH p.transactionLines WHERE p.id IN :ids")
    List<Product> findByIds(@Param("ids") List<UUID> ids, Sort sort);

    @Query("""
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.category c
            LEFT JOIN FETCH p.transactionLines
            WHERE p.id IN :ids
            AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))""")
    List<Product> findProductsByNameContaining(@Param("ids") List<UUID> ids, @Param("name") String name, Sort sort);

    @Query("""
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.category c
            LEFT JOIN FETCH p.transactionLines
            WHERE p.id IN :ids
            AND c.name = :categoryName""")
    List<Product> findByCategoryName(@Param("ids") List<UUID> ids, @Param("categoryName") String categoryName, Sort sort);

    @Query("SELECT COALESCE(SUM(p.stockQuantity * p.unitPrice),0) FROM Product p")
    BigDecimal calculateTotalStockValue();
}
