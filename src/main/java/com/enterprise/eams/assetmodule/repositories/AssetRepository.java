package com.enterprise.eams.assetmodule.repositories;

import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.usermodule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Boolean existsByName(String name);
}
