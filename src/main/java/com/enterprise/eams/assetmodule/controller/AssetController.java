package com.enterprise.eams.assetmodule.controller;

import com.enterprise.eams.assetmodule.dtos.RegisterAssetRequestDTO;
import com.enterprise.eams.assetmodule.dtos.AssetResponseDTO;
import com.enterprise.eams.assetmodule.dtos.UpdateAssetRequestDTO;
import com.enterprise.eams.assetmodule.services.AssetServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetServices assetService;

    @PostMapping
    public ResponseEntity<AssetResponseDTO> createAsset(@Valid @RequestBody RegisterAssetRequestDTO registerAssetRequestDTO) {
        return new ResponseEntity<>(assetService.registerAsset(registerAssetRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AssetResponseDTO>> getAssets() {
        return new ResponseEntity<>(assetService.getAssets(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> getAssetById(@PathVariable Long id) {
        return new ResponseEntity<>(assetService.getAssetById(id),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> updateAsset(@PathVariable Long id, @Valid @RequestBody UpdateAssetRequestDTO updateAssetRequestDTO) {
        return new ResponseEntity<>(assetService.updateAsset(id, updateAssetRequestDTO),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> deleteAsset(@PathVariable Long id) {
        return new ResponseEntity<>(assetService.deleteAsset(id),HttpStatus.OK);
    }


}
