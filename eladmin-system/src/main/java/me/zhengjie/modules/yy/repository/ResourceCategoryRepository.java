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

    /**
     * 根据资源分组查询所有下面所有资源分类
     *
     * @param groupId .
     * @return .
     */
    @Query("select rc from ResourceCategory rc left join fetch rc.resourceGroups rg where rg.id = ?1 and rc.status = 1")
    List<ResourceCategory> findAllByGroupId(Long groupId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("from ResourceCategory where id = ?1")
    ResourceCategory getResourceCategoryForUpdate(Long id);

}
