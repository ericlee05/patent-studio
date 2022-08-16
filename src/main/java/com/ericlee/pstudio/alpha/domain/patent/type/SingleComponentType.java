package com.ericlee.pstudio.alpha.domain.patent.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum SingleComponentType {
    TECHNOLOGY_CATEGORY("기술 분야"),
    PROBLEM_TO_SOLVE("해결하고자 하는 과제"),
    EFFECT_OF_INVENTION("발명의 효과"),
    SUMMARY("요약");

    private final String description;

    public static SingleComponentType getByName(String name) {
        for (SingleComponentType type : SingleComponentType.values()) {
            if(type.name().equals(name)) return type;
        }
        return null;
    }
}
