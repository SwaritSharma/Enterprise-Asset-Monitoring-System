package com.enterprise.eams.assetmodule.repositories;

import com.enterprise.eams.assetmodule.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Boolean existsByName(String name);
}
