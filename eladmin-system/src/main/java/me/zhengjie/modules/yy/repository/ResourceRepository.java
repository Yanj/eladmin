package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {

    List<Resource> findAllByDeptIdAndResourceCategoryId(Long deptId, Long resourceCategoryId);

}
