package com.ericlee.pstudio.alpha.domain.patent.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class ClaimReference {
    @EmbeddedId
    private ClaimReferenceId id;
}
