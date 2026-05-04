package com.enterprise.eams.assetmodule.services;

import com.enterprise.eams.assetmodule.dtos.RegisterAssetRequestDTO;
import com.enterprise.eams.assetmodule.dtos.AssetResponseDTO;
import com.enterprise.eams.assetmodule.dtos.UpdateAssetRequestDTO;
import com.enterprise.eams.assetmodule.entity.Asset;
import com.enterprise.eams.assetmodule.exception.AssetNotFoundException;
import com.enterprise.eams.assetmodule.exception.DuplicateAssetException;
import com.enterprise.eams.assetmodule.exception.InvalidAssetAssignmentException;
import com.enterprise.eams.assetmodule.mapper.AssetMapper;
import com.enterprise.eams.assetmodule.repositories.AssetRepository;
import com.enterprise.eams.common.services.EmailServices;
import com.enterprise.eams.usermodule.entity.User;
import com.enterprise.eams.usermodule.enums.UserRole;
import com.enterprise.eams.usermodule.exception.UserNotFoundException;
import com.enterprise.eams.usermodule.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AssetServices {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final AssetMapper assetMapper;
    private final EmailServices emailService;


    @Transactional
    public AssetResponseDTO registerAsset(RegisterAssetRequestDTO registerAssetRequestDTO) {
        Asset asset = assetMapper.toEntity(registerAssetRequestDTO);
        User assignedUser=null;
        if(assetRepository.existsByName(registerAssetRequestDTO.getName())){
            throw new DuplicateAssetException("Asset with this name "+registerAssetRequestDTO.getName()+" already exists");
        }
        if(registerAssetRequestDTO.getAssignedUserId()!=null)
        {
            assignedUser=userRepository.findById(registerAssetRequestDTO.getAssignedUserId()).orElseThrow(()
            -> new UserNotFoundException("User ID Not Found"));
            validateAssignableUser(assignedUser);
            asset.setAssignedTo(assignedUser);
        }
        Asset assetToSave = assetRepository.save(asset);
        if(assignedUser!=null) {
            emailService.sendEmail(
                    assignedUser.getEmail(),
                    "New Asset Assigned to You",
                    "Hello " + assignedUser.getUsername() + ",\n\n" +
                            "You have been assigned a new asset in the Enterprise Asset Monitoring System (EAMS).\n\n" +
                            "Asset Details:\n" +
                            "- Asset ID: " + assetToSave.getId() + "\n" +
                            "- Type: " + assetToSave.getType() + "\n" +
                            "- Location: " + assetToSave.getLocation() + "\n\n" +
                            "Please ensure regular monitoring and take necessary actions if required.\n\n" +
                            "Best Regards,\nEAMS Team"
            );
        }
        return assetMapper.toAssetResponseDTO(assetToSave);
    }

    public List<AssetResponseDTO> getAssets() {
        List<Asset> assets = assetRepository.findAll();
        return assetMapper.toAssetResponseDTOList(assets);
    }

    public AssetResponseDTO getAssetById(Long id) {
        Asset asset=assetRepository.findById(id).orElseThrow(()->new AssetNotFoundException("Asset does not exist with id : "+id));
        return assetMapper.toAssetResponseDTO(asset);
    }

    @Transactional
    public AssetResponseDTO updateAsset(Long assetId,@Valid UpdateAssetRequestDTO updateAssetRequestDTO) {
        Asset asset = assetRepository.findById(assetId).orElseThrow(() -> new AssetNotFoundException("Asset does not exist with id : " + assetId));

        if (updateAssetRequestDTO.getName() != null) {
            String newName = updateAssetRequestDTO.getName().trim();

            if (!newName.equals(asset.getName()) &&
                    assetRepository.existsByName(newName)) {

                throw new DuplicateAssetException(
                        "Asset with name " + newName + " already exists"
                );
            }

            asset.setName(newName);
        }

        if (updateAssetRequestDTO.getType() != null) {
            asset.setType(updateAssetRequestDTO.getType());
        }

        if (updateAssetRequestDTO.getLocation() != null) {
            asset.setLocation(updateAssetRequestDTO.getLocation());
        }

        if (updateAssetRequestDTO.getThresholdTemp() != null) {
            asset.setThresholdTemp(updateAssetRequestDTO.getThresholdTemp());
        }

        if (updateAssetRequestDTO.getThresholdPressure() != null) {
            asset.setThresholdPressure(updateAssetRequestDTO.getThresholdPressure());
        }

        boolean changed = false;
        User assignedUser = asset.getAssignedTo();
        User updatedUser = null;

        if(updateAssetRequestDTO.getAssignedUserId()!=null){
            updatedUser=userRepository.findById(updateAssetRequestDTO.getAssignedUserId()).orElseThrow(() -> new UserNotFoundException("User ID Not Found Asset cannot be assigned to "+updateAssetRequestDTO.getAssignedUserId()));
            validateAssignableUser(updatedUser);
            if(assignedUser==null || !updatedUser.getId().equals(assignedUser.getId())) {
                asset.setAssignedTo(updatedUser);
                changed=true;
            }
        }
        else{
            if(assignedUser!=null) {
                asset.setAssignedTo(null);
                changed=true;
            }
        }

        Asset assetToSave = assetRepository.save(asset);
        if(changed){
            if(assignedUser!=null && (updatedUser==null || !updatedUser.getId().equals(assignedUser.getId()))) {
                emailService.sendEmail(
                        assignedUser.getEmail(),
                        "Asset Assignment Update – Action No Longer Required",
                        "Hello " + assignedUser.getUsername() + ",\n\n" +
                                "This is to inform you that the asset previously assigned to you has been unassigned or reassigned.\n\n" +
                                "Asset Details:\n" +
                                "- Asset ID: " + assetToSave.getId() + "\n" +
                                "- Type: " + assetToSave.getType() + "\n" +
                                "- Location: " + assetToSave.getLocation() + "\n\n" +
                                "You are no longer responsible for monitoring or managing this asset.\n\n" +
                                "If you have any questions or believe this update was made in error, please contact your administrator.\n\n" +
                                "Best Regards,\nEAMS Team"
                );
            }
            if(updatedUser!=null && (assignedUser==null || !updatedUser.getId().equals(assignedUser.getId()))) {
                emailService.sendEmail(
                        updatedUser.getEmail(),
                        "New Asset Assigned to You",
                        "Hello " + updatedUser.getUsername() + ",\n\n" +
                                "You have been assigned a new asset in the Enterprise Asset Monitoring System (EAMS).\n\n" +
                                "Asset Details:\n" +
                                "- Asset ID: " + assetToSave.getId() + "\n" +
                                "- Type: " + assetToSave.getType() + "\n" +
                                "- Location: " + assetToSave.getLocation() + "\n\n" +
                                "Please ensure regular monitoring and take necessary actions if required.\n\n" +
                                "Best Regards,\nEAMS Team"
                );
            }
        }
        return assetMapper.toAssetResponseDTO(assetToSave);
    }

    @Transactional
    public AssetResponseDTO deleteAsset(Long id) {
        Asset asset=assetRepository.findById(id).orElseThrow(() -> new AssetNotFoundException("Asset does not exist with id : " + id));
        AssetResponseDTO assetResponseDTO=assetMapper.toAssetResponseDTO(asset);
        assetRepository.delete(asset);
        return assetResponseDTO;
    }

    private void validateAssignableUser(User user) {
        if (!user.getRole().equals(UserRole.OPERATOR)) {
            throw new InvalidAssetAssignmentException(
                    "Asset can only be assigned to OPERATOR"
            );
        }
    }
}

