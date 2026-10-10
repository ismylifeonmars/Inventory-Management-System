package com.ravemaster.inventory.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "movement_lines")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer previousQuantity;

    @Column(nullable = false)
    private Integer resultingQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_movement_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movement_id", nullable = false)
    private Movement movement;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovementLine that = (MovementLine) o;
        return Objects.equals(id, that.id) && Objects.equals(quantity, that.quantity) && Objects.equals(previousQuantity, that.previousQuantity) && Objects.equals(resultingQuantity, that.resultingQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity, previousQuantity, resultingQuantity);
    }
}
