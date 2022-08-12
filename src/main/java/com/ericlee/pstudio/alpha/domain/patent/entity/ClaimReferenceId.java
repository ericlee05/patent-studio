package com.ericlee.pstudio.alpha.domain.patent.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class ClaimReferenceId implements Serializable {
    @ManyToOne
    @JoinColumn
    private Claim parentClaim;

    @ManyToOne
    @JoinColumn
    private Claim childClaim;
}
