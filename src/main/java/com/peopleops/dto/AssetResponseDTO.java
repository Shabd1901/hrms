package com.peopleops.dto;

import com.peopleops.enums.AssetStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponseDTO {
    private Long id;
    private String assetTag;
    private String name;
    private String category;
    private LocalDate purchaseDate;
    private AssetStatus status;
}
