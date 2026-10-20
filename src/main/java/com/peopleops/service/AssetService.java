package com.peopleops.service;

import com.peopleops.dto.AssetRequestDTO;
import com.peopleops.dto.AssetResponseDTO;
import com.peopleops.entity.Asset;
import com.peopleops.enums.AssetStatus;
import com.peopleops.repository.AssetRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public AssetResponseDTO createAsset(AssetRequestDTO request) {
        if (assetRepository.existsByAssetTag(request.getAssetTag())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Asset already exists with tag: " + request.getAssetTag());
        }

        Asset asset = toEntity(request);
        asset.setStatus(AssetStatus.AVAILABLE);
        Asset saved = assetRepository.save(asset);
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found with id: " + id));
        return toResponseDTO(asset);
    }

    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO request) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found with id: " + id));

        if (!asset.getAssetTag().equals(request.getAssetTag()) && assetRepository.existsByAssetTag(request.getAssetTag())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Asset already exists with tag: " + request.getAssetTag());
        }

        asset.setAssetTag(request.getAssetTag());
        asset.setName(request.getName());
        asset.setCategory(request.getCategory());
        asset.setPurchaseDate(request.getPurchaseDate());

        Asset updated = assetRepository.save(asset);
        return toResponseDTO(updated);
    }

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found with id: " + id));

        if (asset.getStatus() == AssetStatus.ASSIGNED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete an asset that is currently assigned");
        }

        assetRepository.delete(asset);
    }

    public AssetResponseDTO retireAsset(Long id) {
        Asset asset = assetRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found with id: " + id));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only AVAILABLE assets can be retired. Current status: " + asset.getStatus());
        }

        asset.setStatus(AssetStatus.RETIRED);
        Asset updated = assetRepository.save(asset);
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
