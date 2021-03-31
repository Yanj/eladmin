package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.ResourceCategory;
import org.springframework.data.jpa.repository.*;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ResourceCategoryRepository extends JpaRepository<ResourceCategory, Long>, JpaSpecificationExecutor<ResourceCategory> {

    /**
     * 根据资源分组查询所有下面所有资源分类
     *
     * @param groupId .
     * @return .
     */
    @Query("select rc from ResourceCategory rc left join fetch rc.resourceGroups rg where rg.id = ?1 and rc.status = 1")
    List<ResourceCategory> findAllByGroupId(Long groupId);

    /**
     * 锁行进行更新
     *
     * @param id .
     * @return .
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("from ResourceCategory where id = ?1")
    ResourceCategory getResourceCategoryForUpdate(Long id);

    /**
     * 更新资源分类的可用资源数量
     *
     * @param id .
     * @return .
     */
    @Modifying
    @Query(value = " " +
            "update yy_resource_category rc " +
            "inner join ( " +
            "  select t.resource_category_id, t.status, sum(t.count) as count from ( " +
            "    select " +
            "      resource_category_id, " +
            "      status, " +
            "      ifnull(sum(count),0) as count " +
            "    from yy_resource " +
            "      where resource_category_id = ?1 " +
            "      group by resource_category_id, status " +
            "    union all " +
            "    select ?1 as resource_category_id, '1' as status, 0 as count " +
            "  ) t " +
            "  group by t.resource_category_id, t.status " +
            ") r on rc.id = r.resource_category_id and r.resource_category_id = ?1 and r.status = 1 " +
            "set rc.count = r.count " +
            "where rc.id = ?1 " +
            " ",
            nativeQuery = true)
    int updateResourceCategoryCount(Long id);

}
