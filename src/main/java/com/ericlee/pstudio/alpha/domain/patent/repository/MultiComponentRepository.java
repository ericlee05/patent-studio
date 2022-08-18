package com.ericlee.pstudio.alpha.domain.patent.repository;

import com.ericlee.pstudio.alpha.domain.patent.entity.MultiComponent;
import com.ericlee.pstudio.alpha.domain.patent.entity.MultiComponentId;
import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import com.ericlee.pstudio.alpha.domain.patent.type.MultiComponentType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiComponentRepository extends CrudRepository<MultiComponent, MultiComponentId> {
    @Modifying
    @Query("DELETE FROM MultiComponent m WHERE m.id.patent = :patent AND m.id.multiComponentType = :type")
    void deleteByPatentAndType(@Param("patent") Patent patent, @Param("type") MultiComponentType type);
}
