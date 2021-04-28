package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.ResourceGroup;
import me.zhengjie.modules.yy.domain.ResourceGroupCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ResourceGroupRepository extends JpaRepository<ResourceGroup, Long>, JpaSpecificationExecutor<ResourceGroup> {

    /**
     * 查询资源数量
     *
     * @param comId .
     * @return .
     */
    @Query(
            value = "" +
                    "select new me.zhengjie.modules.yy.domain.ResourceGroupCount(rg.id, rg.name, min(rc.count), max(rc.count), count(rc.count)) " +
                    "from ResourceGroup rg " +
                    "left join rg.resourceCategories rc on rc.status = '1' " +
                    "where rg.comId = ?1 and rg.status = '1' " +
                    "group by rg.id " +
                    "order by rg.sort asc " +
                    ""
    )
    List<ResourceGroupCount> findCountByComId(Long comId);

    @Query("from ResourceGroup rg where rg.comId = ?1 and rg.status = '1' order by rg.sort asc")
    List<ResourceGroup> findByComId(Long comId);

}
