package org.example.invertory.repository;

import org.example.invertory.entity.InventoryQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryQuantityRepository extends JpaRepository<InventoryQuantity, Long> {
}
