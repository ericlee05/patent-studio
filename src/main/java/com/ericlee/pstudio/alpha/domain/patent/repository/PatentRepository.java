package com.ericlee.pstudio.alpha.domain.patent.repository;

import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatentRepository extends JpaRepository<Patent, Long> {

}
