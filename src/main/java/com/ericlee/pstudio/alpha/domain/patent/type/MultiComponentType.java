package com.ericlee.pstudio.alpha.domain.patent.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum MultiComponentType {
    BASEMENT_OF_INVENTION("발명의 배경이 되는 기술"),
    PATENT_REFERENCE("특허문헌"),
    NON_PATENT_REFERENCE("비특허문헌"),
    SOLUTION_FOR_PROBLEM("과제의 해결 수단"),
    WAY_FOR_INVENTION("발명을 실시하기 위한 구체적인 내용"),
    DESCRIPTION_FOR_SYMBOL("부호의 설명");

    private final String description;
}
