package com.ravemaster.inventory.domain.entity;

import com.ravemaster.inventory.domain.enums.MovementType;
import com.ravemaster.inventory.domain.enums.ReferenceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "stock_movements")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MovementType movementType;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ReferenceType referenceType;

    private UUID referenceId;

    @Column(nullable = false)
    private String reason;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_movement_id", nullable = false)
    private User performedBy;

    @OneToMany(mappedBy = "movement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovementLine> movementLines = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return Objects.equals(id, movement.id) && movementType == movement.movementType && referenceType == movement.referenceType && Objects.equals(referenceId, movement.referenceId) && Objects.equals(reason, movement.reason) && Objects.equals(createdAt, movement.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movementType, referenceType, referenceId, reason, createdAt);
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
