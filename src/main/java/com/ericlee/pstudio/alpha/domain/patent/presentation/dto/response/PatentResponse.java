package com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor @Builder
@Getter
public class PatentResponse {
    private Long patentId;

    private String nameWithKorean;
    private String nameWithEnglish;

    private String lastModified;
    private String lastModifierName;
}
