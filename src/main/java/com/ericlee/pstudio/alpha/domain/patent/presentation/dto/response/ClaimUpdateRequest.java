package com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response;

import com.swcns.reflcrypt.annotation.SecurityField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor @Getter
public class ClaimUpdateRequest {
    private List<String> content;
}
