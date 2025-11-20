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

    @Query("SELECT DISTINCT p FROM Product p WHERE p.stockQuantity BETWEEN :min AND :max")
    Page<UUID> findLowStockProductsIds(@Param("min") Integer min, @Param("max") Integer max, Pageable pageable);

    @Query("SELECT p.id FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<UUID> findAllProductIdsByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT p.id FROM Product p JOIN p.category c WHERE c.name = :categoryName")
    Page<UUID> findAllProductIdsByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("SELECT p.id FROM Product p LEFT JOIN p.transactionLines tl GROUP BY p.id ORDER BY COALESCE(SUM(tl.quantity),0) DESC")
    Page<UUID> findBestSellingProductIds(Pageable pageable);

    @Query("SELECT p.id FROM Product p LEFT JOIN p.transactionLines tl GROUP BY p.id ORDER BY COALESCE(SUM(tl.quantity),0) ASC")
    Page<UUID> findWorstSellingProductIds(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category c LEFT JOIN FETCH p.transactionLines WHERE p.id IN :ids")
    List<Product> findByIds(@Param("ids") List<UUID> ids, Sort sort);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category c LEFT JOIN FETCH p.transactionLines WHERE p.id IN :ids")
    List<Product> findByIdsSecond(@Param("ids") List<UUID> ids);

    @Query("SELECT COALESCE(SUM(p.stockQuantity * p.unitPrice),0) FROM Product p")
    BigDecimal calculateTotalStockValue();
}
