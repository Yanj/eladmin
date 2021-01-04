package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.ResourceReserveCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ResourceReserveCountRepository extends JpaRepository<ResourceReserveCount, ResourceReserveCount.PK>, JpaSpecificationExecutor<ResourceReserveCount> {

    /**
     * 根据部门和日期查询
     *
     * @param deptId .
     * @param date   .
     * @return .
     */
    List<ResourceReserveCount> findAllByPkDeptIdAndPkDate(Long deptId, String date);

}
