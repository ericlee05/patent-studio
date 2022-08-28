package com.ericlee.pstudio.alpha.domain.patent.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Claim {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long claimId;

    @ManyToOne
    @JoinColumn
    private Patent patent;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    @Lob
    private String content;

    @OneToMany(mappedBy = "id.childClaim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimReference> parentClaims;

    @OneToMany(mappedBy = "id.parentClaim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimReference> childClaims;

    @Override
    public String toString() {
        return number.toString();
    }
}
