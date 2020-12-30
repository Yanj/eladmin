package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ResourceCategoryRepository extends JpaRepository<ResourceCategory, Long>, JpaSpecificationExecutor<ResourceCategory> {

    List<ResourceCategory> findAllByDeptId(Long deptId);

    @Query("select rc from ResourceCategory rc left join fetch rc.resourceGroups rg where rg.id = ?2 and rc.dept.id = ?1")
    List<ResourceCategory> findAllByDeptIdAndGroupId(Long deptId, Long groupId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("from ResourceCategory where id = ?1")
    ResourceCategory getResourceCategoryForUpdate(Long id);

}
