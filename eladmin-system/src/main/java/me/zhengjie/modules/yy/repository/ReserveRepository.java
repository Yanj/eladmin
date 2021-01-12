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

    /**
     * 查询某日各时段资源占用数量
     *
     * @param date .
     * @return .
     */
    /*
select r.work_time_id as work_time_id, r.resource_group_id as resource_group_id, count(1) as count from yy_reserve r
where r.dept_id = ?1 and r.date = ?2 r.status != 'canceled'
group by r.work_time_id, r.resource_group_id
     */
    @Query(
            value = "" +
                    "select r.work_time_id as work_time_id, r.resource_group_id as resource_group_id, count(1) as count from yy_reserve r " +
                    "where r.dept_id = ?1 and r.date = ?2 and r.status != 'canceled' " +
                    "group by r.work_time_id, r.resource_group_id " +
                    "",
            nativeQuery = true
    )
    List<Map<String, Object>> queryCountGroupByWorkTimeAndResourceGroup(Long deptId, String date);

    /**
     * 查询某日的时段预约总数
     * @param date
     * @return
     */
    /*
select r.work_time_id as work_time_id, count(1) as count from yy_reserve r
where r.dept_id = ?1 and r.date = ?2 and r.status != 'canceled'
group by r.work_time_id
     */
    @Query(
            value = "" +
                    "select r.work_time_id as work_time_id, count(1) as count from yy_reserve r " +
                    "where r.dept_id = ?1 and r.date = ?2 and r.status != 'canceled' " +
                    "group by r.work_time_id " +
                    "",
            nativeQuery = true
    )
    List<Map<String, Object>> queryCountGroupByWorkTimeId(Long deptId, String date);

    /**
     * 日期范围内根据状态统计
     *
     * @param beginDate .
     * @param endDate .
     * @param status .
     * @return .
     */
    /*
select r.date as date,count(1) as count from yy_reserve r
where r.dept_id = ?1 and r.date >= ?2 and r.date < ?3 and r.status = ?4
group by r.date
     */
    @Query(
            value = "" +
                    "select r.date as date, count(1) as count from yy_reserve r " +
                    "where r.dept_id = ?1 and r.date >= ?2 and r.date < ?3 and r.status = ?4 " +
                    "group by r.date ",
            nativeQuery = true
    )
    List<Map<String, Object>> queryRangeCountByStatusEquals(Long deptId, String beginDate, String endDate, String status);

    /**
     * 日期范围内根据状态统计
     *
     * @param beginDate .
     * @param endDate .
     * @param status .
     * @return .
     */
    /*
select r.date as date,count(1) as count from yy_reserve r
where r.dept_id = ?1 and r.date >= ?2 and r.date < ?3 and r.status != 'canceled'
group by r.date
     */
    @Query(
            value = "" +
                    "select r.date as date, count(1) as count from yy_reserve r " +
                    "where r.dept_id = ?1 and r.date >= ?2 and r.date < ?3 and r.status != ?4 " +
                    "group by r.date ",
            nativeQuery = true
    )
    List<Map<String, Object>> queryRangeCountByStatusNotEquals(Long deptId, String beginDate, String endDate, String status);

    /**
     * 统计今日预约数据
     *
     * @param date .
     * @return .
     */
    /*
select 'all' as status, count(1) as count from yy_reserve r where r.date =  ?1 and r.status != ''
union
select 'init' as status, count(1) as count from yy_reserve r where r.date = ?1 and r.status = 'init'
union
select 'check_in' as status, count(1) as count from yy_reserve r where r.date = ?1 and r.status = 'check_in'
union
select 'verified' as status, count(1) as count from yy_reserve r where r.date = ?1 and r.status = 'verified'
     */
    @Query(
            value = "" +
                    "select 'all' as status, count(1) as count from yy_reserve r where r.dept_id = ?1 and r.date =  ?2 and r.status != 'canceled' " +
                    "union " +
                    "select 'init' as status, count(1) as count from yy_reserve r where r.dept_id = ?1 and r.date = ?2 and r.status = 'init' " +
                    "union " +
                    "select 'check_in' as status, count(1) as count from yy_reserve r where r.dept_id = ?1 and r.date = ?2 and r.status = 'check_in' " +
                    "union " +
                    "select 'verified' as status, count(1) as count from yy_reserve r where r.dept_id = ?1 and r.date = ?2 and r.status = 'verified'" +
            "",
            nativeQuery = true
    )
    List<Map<String, Object>> queryTodayCount(Long deptId, String date);

    /**
     * 根据状态和日期查询
     *
     * @param status
     * @param date
     * @return
     */
    List<Reserve> findByStatusAndDateLessThan(String status, String date);

    /**
     * 锁行更新
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("from Reserve where id = ?1")
    Reserve getReserveForUpdate(Long id);

    /**
     * 查询同一个套餐同一时段的预约数量
     * @param deptId .
     * @param patientId .
     * @param patientTermId .
     * @param date .
     * @param workTimeId .
     * @return .
     */
    @Query(value = "" +
            "select count(r) from Reserve r " +
            "where 1=1 " +
            "and r.dept.id = ?1 " +
            "and r.patient.id = ?2 " +
            "and r.patientTerm.id = ?3 " +
            "and r.date = ?4 " +
            "and r.workTime.id = ?5 " +
            "and r.status <> 'canceled'")
    Long countByPatientTerm(Long deptId, Long patientId, Long patientTermId, String date, Long workTimeId);

    /**
     * 查询分类资源已预约数量
     */
    /*
select rr.resource_category_id, count(rr.resource_category_id) as count
from yy_reserve re
right join yy_reserve_resource rr on re.id = rr.reserve_id
where re.dept_id = 0 and re.date = '' and re.work_time_id = 0
group by rr.resource_category_id
     */
    @Query(value = "" +
            "select rr.resource_category_id as resource_category_id, count(rr.resource_category_id) as count " +
            "from yy_reserve re " +
            "right join yy_reserve_resource rr on re.id = rr.reserve_id " +
            "where re.dept_id = ?1 and re.date = ?2 and re.work_time_id = ?3 and rr.resource_id is null " +
            "group by rr.resource_category_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveResourceCount(Long deptId, String date, Long workTimeId);

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
            "from yy_reserve where status <> 'canceled' and dept_id = ?1 " +
            "group by dept_id, `date`, work_time_id, term_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveCount(Long deptId);

    @Query(value = "select " +
            "dept_id as dept_id, `date` as `date`, work_time_id as work_time_id, term_id as term_id, count(1) as count " +
            "from yy_reserve where status <> 'canceled' and dept_id = ?1 and `date` = ?2 " +
            "group by dept_id, `date`, work_time_id, term_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveCountByDate(Long deptId, String date);

    @Query(value = "select " +
            "dept_id as dept_id, `date` as `date`, work_time_id as work_time_id, term_id as term_id, count(1) as count " +
            "from yy_reserve where status <> 'canceled' and dept_id = ?1 and `date` = ?2 and term_id = ?3 " +
            "group by dept_id, `date`, work_time_id, term_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveCountByDateAndTermId(Long deptId, String date, Long termId);

    @Query(value = "select " +
            "dept_id as dept_id, `date` as `date`, work_time_id as work_time_id, term_id as term_id, count(1) as count " +
            "from yy_reserve where status <> 'canceled' and dept_id = ?1 and `date` = ?2 and term_id = ?3 and resource_group_id = ?4 " +
            "group by dept_id, `date`, work_time_id, term_id",
            nativeQuery = true
    )
    List<Map<String, Object>> queryReserveCountByDateAndTermIdAndResourceGroupId(Long deptId, String date, Long termId, Long resourceGroupId);

}
