package com.ericlee.pstudio.alpha.domain.organization.entity;

import com.ericlee.pstudio.alpha.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
@Entity
public class Organization {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(columnDefinition = "VARCHAR(64)", nullable = false)
    private String name;

    @Embedded
    private OrganizationDetail detail;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;
}
