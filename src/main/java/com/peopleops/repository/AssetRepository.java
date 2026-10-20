package com.peopleops.repository;

import com.peopleops.entity.Asset;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    boolean existsByAssetTag(String assetTag);
    Optional<Asset> findByAssetTag(String assetTag);
}
