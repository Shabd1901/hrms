package com.peopleops.service;

import com.peopleops.dto.AssetRequestDTO;
import com.peopleops.dto.AssetResponseDTO;
import com.peopleops.entity.Asset;
import com.peopleops.enums.AssetStatus;
import com.peopleops.exception.BusinessRuleException;
import com.peopleops.exception.DuplicateResourceException;
import com.peopleops.exception.ResourceNotFoundException;
import com.peopleops.repository.AssetRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    private static final Logger log = LoggerFactory.getLogger(AssetService.class);

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public AssetResponseDTO createAsset(AssetRequestDTO request) {
        if (assetRepository.existsByAssetTag(request.getAssetTag())) {
            log.warn("Create asset rejected: asset tag {} already exists", request.getAssetTag());
            throw new DuplicateResourceException("Asset already exists with tag: " + request.getAssetTag());
        }

        Asset asset = toEntity(request);
        asset.setStatus(AssetStatus.AVAILABLE);
        Asset saved = assetRepository.save(asset);
        log.info("Created asset '{}' with id {} and tag {}", saved.getName(), saved.getId(), saved.getAssetTag());
        return toResponseDTO(saved);
    }

    public List<AssetResponseDTO> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        List<AssetResponseDTO> responses = new ArrayList<>();
        for (Asset asset : assets) {
            responses.add(toResponseDTO(asset));
        }
        return responses;
    }

    public AssetResponseDTO getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));
        return toResponseDTO(asset);
    }

    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO request) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        if (!asset.getAssetTag().equals(request.getAssetTag()) && assetRepository.existsByAssetTag(request.getAssetTag())) {
            log.warn("Update asset rejected: asset tag {} already exists for another asset", request.getAssetTag());
            throw new DuplicateResourceException("Asset already exists with tag: " + request.getAssetTag());
        }

        asset.setAssetTag(request.getAssetTag());
        asset.setName(request.getName());
        asset.setCategory(request.getCategory());
        asset.setPurchaseDate(request.getPurchaseDate());

        Asset updated = assetRepository.save(asset);
        log.info("Updated asset id {} (tag {})", updated.getId(), updated.getAssetTag());
        return toResponseDTO(updated);
    }

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            log.warn("Delete asset rejected: asset id {} is currently assigned", id);
            throw new BusinessRuleException("Cannot delete an asset that is currently assigned");
        }

        assetRepository.delete(asset);
        log.info("Deleted asset id {}", id);
    }

    public AssetResponseDTO retireAsset(Long id) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + id));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            log.warn("Retire asset rejected: asset id {} status is {}", id, asset.getStatus());
            throw new BusinessRuleException("Only AVAILABLE assets can be retired. Current status: " + asset.getStatus());
        }

        asset.setStatus(AssetStatus.RETIRED);
        Asset updated = assetRepository.save(asset);
        log.info("Retired asset id {}", id);
        return toResponseDTO(updated);
    }

    private Asset toEntity(AssetRequestDTO dto) {
        Asset asset = new Asset();
        asset.setAssetTag(dto.getAssetTag());
        asset.setName(dto.getName());
        asset.setCategory(dto.getCategory());
        asset.setPurchaseDate(dto.getPurchaseDate());
        return asset;
    }

    private AssetResponseDTO toResponseDTO(Asset entity) {
        return new AssetResponseDTO(
            entity.getId(),
            entity.getAssetTag(),
            entity.getName(),
            entity.getCategory(),
            entity.getPurchaseDate(),
            entity.getStatus()
        );
    }
}
