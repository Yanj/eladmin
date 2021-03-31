package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long>, JpaSpecificationExecutor<WorkTime> {

    /**
     * 根据时间段查询所有的工作时段
     *
     * @param comId 公司 ID
     * @param beginTime 开始时间(含)
     * @param endTime 结束时间(含)
     * @return .
     */
    @Query(value = "from WorkTime where comId = ?1 and beginTime >= ?2 and beginTime < ?3 and status = 1 order by beginTime asc")
    List<WorkTime> findByComIdAndTime(Long comId, String beginTime, String endTime);

    /**
     * 查询所有时段
     *
     * @param comId .
     * @return .
     */
    @Query(value = "from WorkTime wt where wt.comId = ?1 and wt.status = 1 order by wt.beginTime asc ")
    List<WorkTime> findByComId(Long comId);

}
