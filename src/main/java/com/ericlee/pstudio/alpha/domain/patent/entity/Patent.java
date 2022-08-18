package com.ericlee.pstudio.alpha.domain.patent.entity;

import com.ericlee.pstudio.alpha.domain.organization.entity.Organization;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentComponentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.type.MultiComponentType;
import com.ericlee.pstudio.alpha.domain.patent.type.SingleComponentType;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public SingleComponent getSingleComponentByType(SingleComponentType type) {
        return singleComponents.stream().filter(it -> it.getId().getSingleComponentType() == type).findFirst()
                .orElseThrow(PatentComponentNotFoundException::new);
    }

    @OneToMany(mappedBy = "id.patent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MultiComponent> multiComponents;
    public void updateMultiComponents(User modifier, List<MultiComponent> components) {
        getMultiComponents().addAll(components);

        getDetail().setLastModified(LocalDateTime.now());
        getDetail().setLastModifier(modifier);
    }
    public List<MultiComponent> getMultiComponentsByType(MultiComponentType type) {
        return multiComponents.stream()
                .filter(it -> it.getId().getMultiComponentType() == type)
                .sorted(Comparator.comparingInt(MultiComponent::getOrder))
                .collect(Collectors.toList());
    }
    public MultiComponent getMultiComponentByTypeAndName(MultiComponentType type, String identifier) {
        return getMultiComponentsByType(type).stream().filter(it -> it.getId().getComponentIdentifier().equals(identifier)).findFirst()
                .orElseThrow(PatentComponentNotFoundException::new);
    }
}
