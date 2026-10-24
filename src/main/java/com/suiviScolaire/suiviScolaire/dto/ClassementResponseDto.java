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
public class ClassementResponseDto implements ResponseDto {
    private Long classeId;
    private String nomClasse;
    private Integer trimestre;
    private BigDecimal moyenneClasse;
    private BigDecimal meilleureMoyenne;
    private BigDecimal plusFaibleMoyenne;
    private List<ClassementEleveDto> eleves;
}

