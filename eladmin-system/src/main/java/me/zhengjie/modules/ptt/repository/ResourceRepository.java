package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 资源
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {

    /**
     * 查询资源总数
     *
     * @param resourceTypeId .
     * @param deptId         .
     * @return .
     */
    @Query(
            value = "select sum(r.count) from ptt_resource r where r.resource_type_id = :resourceTypeId and r.dept_id = :deptId",
            nativeQuery = true
    )
    Long findCountByDeptIdAndTermId(@Param("resourceTypeId") Long resourceTypeId, @Param("deptId") Long deptId);

}
