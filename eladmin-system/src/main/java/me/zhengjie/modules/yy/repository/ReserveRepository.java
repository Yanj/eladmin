package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ReserveRepository extends JpaRepository<Reserve, Long>, JpaSpecificationExecutor<Reserve> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("from Reserve where id = ?1")
    Reserve getReserveForUpdate(Long id);

    /**
     * 查询分类资源总数
     */
    /*
select rgc.resource_category_id, count(r.`count`) as count from yy_resource_group_category rgc
left join yy_resource r on rgc.resource_category_id = r.resource_category_id group by rgc.resource_category_id
     */
    @Query(value = "" +
            "select rgc.resource_category_id, COALESCE(sum(r.`count`),0) as count " +
            "from yy_resource_group_category rgc " +
            "left join yy_resource r on rgc.resource_category_id = r.resource_category_id " +
            "group by rgc.resource_category_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryCategoryCount();

    /**
     * 查询资源分类在某个时段的使用次数
     *
     * @param deptId     .
     * @param date       .
     * @param workTimeId .
     * @return .
     */
    /*
select rgc.resource_category_id as resource_category_id, count(re.id) as count
from yy_reserve re
right join yy_resource_group_category rgc on re.resource_group_id = rgc.resource_group_id
where re.dept_id = 0 and re.date = '' and re.work_time_id = 0
group by rgc.resource_category_id;
    */
    @Query(value = "" +
            "select rgc.resource_category_id as resource_category_id, count(re.id) as count " +
            "from yy_reserve re " +
            "right join yy_resource_group_category rgc on re.resource_group_id = rgc.resource_group_id " +
            "where re.dept_id = ?1 and re.date = ?2 and re.work_time_id = ?3 " +
            "group by rgc.resource_category_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryCategoryCount(Long deptId, String date, Long workTimeId);

    @Query(value = "select " +
            "dept_id as dept_id, `date` as `date`, work_time_id as work_time_id, term_id as term_id, count(1) as count " +
            "from yy_reserve where dept_id = ?1 " +
            "group by dept_id, `date`, work_time_id, term_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveCount(Long deptId);

    @Query(value = "select " +
            "dept_id as dept_id, `date` as `date`, work_time_id as work_time_id, term_id as term_id, count(1) as count " +
            "from yy_reserve where dept_id = ?1 and `date` = ?2 " +
            "group by dept_id, `date`, work_time_id, term_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveCountByDate(Long deptId, String date);

    @Query(value = "select " +
            "dept_id as dept_id, `date` as `date`, work_time_id as work_time_id, term_id as term_id, count(1) as count " +
            "from yy_reserve where dept_id = ?1 and `date` = ?2 and term_id = ?3 " +
            "group by dept_id, `date`, work_time_id, term_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveCountByDateAndTermId(Long deptId, String date, Long termId);

    @Query(value = "select " +
            "dept_id as dept_id, `date` as `date`, work_time_id as work_time_id, term_id as term_id, count(1) as count " +
            "from yy_reserve where dept_id = ?1 and `date` = ?2 and term_id = ?3 and resource_group_id = ?4 " +
            "group by dept_id, `date`, work_time_id, term_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveCountByDateAndTermIdAndResourceGroupId(Long deptId, String date, Long termId, Long resourceGroupId);

}
