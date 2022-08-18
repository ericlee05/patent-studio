package com.ericlee.pstudio.alpha.domain.patent.presentation.dto.common;

import com.swcns.reflcrypt.annotation.SecurityField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor @Getter
public class MultiComponentDto {
    private String identifier;

    @SecurityField
    private String content;
}
