package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.organization.entity.Organization;
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
public class Patent {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String koreanName;

    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String englishName;

    @Embedded
    private PatentDetail detail;

    @ManyToOne
    @JoinColumn
    private Organization organization;

    @OneToMany(mappedBy = "id.patent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatentDrawing> drawings;

    @OneToMany(mappedBy = "patent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;

    @OneToMany(mappedBy = "id.patent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SingleComponent> singleComponents;

    @OneToMany(mappedBy = "id.patent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MultiComponent> multiComponents;
}
