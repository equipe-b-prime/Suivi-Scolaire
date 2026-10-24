package com.suiviScolaire.suiviScolaire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoyenneMatiereDto implements ResponseDto {
    private Long matiereId;
    private String matiereNom;
    private BigDecimal moyenne;
    private Integer nombreNotes;
    private boolean nonEvalue;
}

