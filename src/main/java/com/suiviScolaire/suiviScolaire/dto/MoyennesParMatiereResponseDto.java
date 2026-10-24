package com.suiviScolaire.suiviScolaire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoyennesParMatiereResponseDto implements ResponseDto {
    private Long eleveId;
    private Integer trimestre;
    private List<MoyenneMatiereDto> matieres;
}

