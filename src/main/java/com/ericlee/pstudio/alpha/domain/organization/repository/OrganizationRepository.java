package com.ericlee.pstudio.alpha.domain.organization.repository;

import com.ericlee.pstudio.alpha.domain.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
