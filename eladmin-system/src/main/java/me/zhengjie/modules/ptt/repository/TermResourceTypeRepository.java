package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.TermResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 套餐资源类型
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface TermResourceTypeRepository extends JpaRepository<TermResourceType, Long>, JpaSpecificationExecutor<TermResourceType> {

    /**
     * 根据套餐和医院查询资源类型
     *
     * @param termId .
     * @param deptId .
     * @return .
     */
    List<TermResourceType> findAllByTermIdAndDeptId(Long termId, Long deptId);

    /**
     * 根据套餐和医院查询资源类型
     *
     * @param termId   .
     * @param deptId   .
     * @param nullable .
     * @return .
     */
    TermResourceType findFirstByTermIdAndDeptIdAndNullable(Long termId, Long deptId, Boolean nullable);

}
