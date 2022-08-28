package com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response;

import com.swcns.reflcrypt.annotation.SecurityField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor @Builder @Getter
public class ClaimResponse {
    private Long id;
    @SecurityField
    private String content;
}
