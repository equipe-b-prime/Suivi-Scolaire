package com.suiviScolaire.suiviScolaire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoyenneGeneraleResponseDto implements ResponseDto {
    private Long eleveId;
    private Integer trimestre;
    private BigDecimal moyenneGenerale;
    private List<DetailCoefficientDto> details;
}

