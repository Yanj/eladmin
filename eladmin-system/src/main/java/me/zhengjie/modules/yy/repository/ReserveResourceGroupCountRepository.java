package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.ReserveResourceGroupCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ReserveResourceGroupCountRepository extends JpaRepository<ReserveResourceGroupCount, ReserveResourceGroupCount.PK>, JpaSpecificationExecutor<ReserveResourceGroupCount> {

    /**
     * 根据部门和日期查询
     *
     * @param deptId    .
     * @param beginDate .
     * @param endDate   .
     * @return .
     */
    @Query("from ReserveResourceGroupCount where pk.deptId = ?1 and pk.date >= ?2 and pk.date <= ?3")
    List<ReserveResourceGroupCount> findAllByPkDeptIdAndPkDate(Long deptId, String beginDate, String endDate);

}
