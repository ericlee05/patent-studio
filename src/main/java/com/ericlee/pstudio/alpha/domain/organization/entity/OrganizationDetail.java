package com.ericlee.pstudio.alpha.domain.organization.entity;

import com.ericlee.pstudio.alpha.domain.organization.type.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Embeddable
public class OrganizationDetail {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    private String enterpriseRegisterNumber;
}
