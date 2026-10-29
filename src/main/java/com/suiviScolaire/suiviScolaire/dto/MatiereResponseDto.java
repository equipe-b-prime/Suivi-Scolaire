package com.suiviScolaire.suiviScolaire.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Réponse contenant les données d'une matière.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatiereResponseDto implements ResponseDto {
    private Long id;
    private String nom;

    private BigDecimal coefficient;
}