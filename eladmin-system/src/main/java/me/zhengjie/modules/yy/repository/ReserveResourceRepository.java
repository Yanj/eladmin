package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.ReserveResource;
import me.zhengjie.modules.yy.domain.ReserveResourceGroupCount;
import me.zhengjie.utils.enums.YesNoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ReserveResourceRepository extends JpaRepository<ReserveResource, Long>, JpaSpecificationExecutor<ReserveResource> {

    /**
     * 根据 reserveId 删除
     *
     * @param reserveId .
     */
    void deleteByReserveId(Long reserveId);

    /**
     * 根据 reserveId 更新状态
     *
     * @param reserveId .
     * @param status    .
     * @return .
     */
    @Modifying
    @Query("update ReserveResource rr set rr.status = ?2 where rr.reserve.id = ?1")
    int updateStatusByReserveId(Long reserveId, YesNoEnum status);

    /**
     * 根据公司和日期统计各时段资源占用数量
     *
     * @param comId .
     * @param beginDate .
     * @param endDate .
     * @return .
     */
    @Query(
            value = "select new me.zhengjie.modules.yy.domain.ReserveResourceGroupCount(r.date, rr.workTime.id, rr.resourceGroup.id, count(rr)) " +
                    "from ReserveResource rr " +
                    "inner join Reserve r " +
                    "    on rr.reserve.id = r.id " +
                    "        and r.status = '1' " +
                    "        and r.verifyStatus <> 'canceled' " +
                    "        and r.comId = ?1 " +
                    "        and r.date >= ?2 " +
                    "        and r.date <= ?3 " +
                    "where 1=1 " +
                    "    and rr.status = '1' " +
                    "group by " +
                    "    r.date, rr.workTime.id, rr.resourceGroup.id "
    )
    List<ReserveResourceGroupCount> findResourceGroupCountByComIdAndDate(Long comId, String beginDate, String endDate);

}
