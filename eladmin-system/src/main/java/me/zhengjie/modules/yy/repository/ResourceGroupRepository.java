package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.ResourceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ResourceGroupRepository extends JpaRepository<ResourceGroup, Long>, JpaSpecificationExecutor<ResourceGroup> {

    List<ResourceGroup> findAllByDeptId(Long deptId);

    @Query("select rg from ResourceGroup rg left join fetch rg.terms tm where rg.dept.id = :deptId and tm.id = :termId")
    List<ResourceGroup> findAllByDeptIdAndTermId(@Param("deptId") Long deptId, @Param("termId") Long termId);

}
