package com.ericlee.pstudio.alpha.domain.user.entity;

import com.ericlee.pstudio.alpha.domain.organization.entity.Organization;
import com.ericlee.pstudio.alpha.domain.user.type.Permission;
import com.ericlee.pstudio.alpha.domain.user.type.Role;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String loginId;

    @Setter
    @Column(columnDefinition = "VARCHAR(128)", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_code")
    @ElementCollection
    private List<Permission> permissions;

    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;
}
