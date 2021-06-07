package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.*;
import me.zhengjie.utils.enums.YesNoEnum;
import org.springframework.data.jpa.repository.*;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ReserveRepository extends JpaRepository<Reserve, Long>, JpaSpecificationExecutor<Reserve> {

    /**
     * 查询某医院某时间内每天各套餐预约总数
     * @param comId .
     * @param beginDate .
     * @param endDate .
     * @return .
     */
    @Query(
            value = "select new me.zhengjie.modules.yy.domain.ReserveTermCount(r.term.id,r.date,count(r)) " +
                    "from Reserve r " +
                    "where 1=1 " +
                    "    and r.comId = ?1 " +
                    "    and r.status = '1' " +
                    "    and r.verifyStatus <> 'canceled' " +
                    "    and r.date >= ?2 " +
                    "    and r.date < ?3 " +
                    "group by r.term.id, r.date"
    )
    List<ReserveTermCount> queryTermCountByComId(Long comId, String beginDate, String endDate);

    /**
     * 查询某医院某时间内每天各套餐预约总数
     * @param orgId .
     * @param beginDate .
     * @param endDate .
     * @return .
     */
    @Query(
            value = "select new me.zhengjie.modules.yy.domain.ReserveTermCount(r.term.id,r.date,count(r)) " +
                    "from Reserve r " +
                    "where 1=1 " +
                    "    and r.orgId = ?1 " +
                    "    and r.status = '1' " +
                    "    and r.verifyStatus <> 'canceled' " +
                    "    and r.date >= ?2 " +
                    "    and r.date < ?3 " +
                    "group by r.term.id, r.date"
    )
    List<ReserveTermCount> queryTermCountByOrgId(Long orgId, String beginDate, String endDate);

    /**
     * 查询某日的各预约时段预约总数
     * @param date
     * @return
     */
    @Query(
            value = "" +
                    "select new me.zhengjie.modules.yy.domain.ReserveWorkTimeCount(r.workTime.id, r.verifyStatus, count(r)) " +
                    "from Reserve r " +
                    "where r.comId = ?1 and r.date = ?2 and r.verifyStatus <> 'canceled' and r.status = '1' " +
                    "group by r.workTime.id, r.verifyStatus " +
                    ""
    )
    List<ReserveWorkTimeCount> queryCountGroupByWorkTimeId(Long comId, String date);

    /**
     * 查询日期段内某状态预约的总数
     *
     * @param orgId .
     * @param status .
     * @param beginDate .
     * @param endDate .
     * @return .
     */
    @Query(
            value = "select " +
                    "new me.zhengjie.modules.yy.domain.ReserveDateCount(r.date, r.verifyStatus, count(r)) " +
                    "from Reserve r " +
                    "where 1=1 " +
                    "    and r.orgId = ?1 " +
                    "    and r.verifyStatus = ?2 " +
                    "    and r.date >= ?3 " +
                    "    and r.date < ?4 " +
                    "group by r.date, r.verifyStatus "
    )
    List<ReserveDateCount> queryDateCountByOrgId(Long orgId, ReserveVerifyStatus status, String beginDate, String endDate);

    /**
     * 查询日期段内某状态预约的总数
     *
     * @param comId .
     * @param status .
     * @param beginDate .
     * @param endDate .
     * @return .
     */
    @Query(
            value = "select " +
                    "new me.zhengjie.modules.yy.domain.ReserveDateCount(r.date, r.verifyStatus, count(r)) " +
                    "from Reserve r " +
                    "where 1=1 " +
                    "    and r.comId = ?1 " +
                    "    and r.verifyStatus = ?2 " +
                    "    and r.date >= ?3 " +
                    "    and r.date < ?4 " +
                    "group by r.date, r.verifyStatus "
    )
    List<ReserveDateCount> queryDateCountByComId(Long comId, ReserveVerifyStatus status, String beginDate, String endDate);

    /**
     * 根据状态和日期查询
     *
     * @param status
     * @param date
     * @return
     */
    @Query(value = "from Reserve where verifyStatus = ?1 and date < ?2")
    List<Reserve> findByVerifyStatusAndDate(ReserveVerifyStatus status, String date);

    /**
     * 锁行更新
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("from Reserve where id = ?1")
    Reserve getReserveForUpdate(Long id);

    /**
     * 更新状态
     *
     * @param id .
     * @param status .
     * @return .
     */
    @Modifying
    @Query("update Reserve r set r.status = ?2 where r.id = ?1")
    int updateStatus(Long id, YesNoEnum status);

    /**
     * 查询同一个套餐同一时段的预约数量
     * @param patientId .
     * @param patientTermId .
     * @param date .
     * @param workTimeId .
     * @return .
     */
    @Query(value = "" +
            "select count(r) from Reserve r " +
            "where 1=1 " +
            "and r.patient.id = ?1 " +
            "and r.patientTerm.id = ?2 " +
            "and r.date = ?3 " +
            "and r.workTime.id = ?4 " +
            "and r.status = '1' " +
            "and r.verifyStatus <> 'canceled'")
    Long countByPatientTerm(Long patientId, Long patientTermId, String date, Long workTimeId);

    /**
     * 查询分类资源已占用数量(未核销的)
     *
     * @param comId .
     * @param date .
     * @return .
     */
    @Query(value = "" +
            "select new me.zhengjie.modules.yy.domain.ReserveResourceCategoryCount(" +
            "    rr.workTime.id, rr.resourceCategory.id, count(rr.resourceCategory.id)" +
            ") " +
            "from ReserveResource rr " +
            "left outer join Reserve r " +
            "    on rr.reserve.id = r.id " +
            "where 1=1 " +
            "    and rr.resource.id is null " +
            "    and rr.status = '1' " +
            "    and r.comId = ?1 " +
            "    and r.date = ?2 " +
            "    and r.status = '1' " +
            "    and r.verifyStatus <> 'canceled' " +
            "    and r.verifyStatus <> 'verified' " +
            "group by rr.workTime.id, rr.resourceCategory.id " +
            ""
    )
    List<ReserveResourceCategoryCount> queryReserveResourceCount(Long comId, String date);

    /**
     * 根据资源分组进行统计已预约数量
     *
     * @param comId .
     * @return .
     */
    @Query(
            value = "select new me.zhengjie.modules.yy.domain.ReserveCount(r.comId, r.date, r.workTime.id, r.term.id, count(r)) " +
                    "from Reserve r " +
                    "where 1=1 " +
                    "    and r.comId = ?1 " +
                    "    and r.status = '1' " +
                    "    and r.verifyStatus <> 'canceled' " +
                    "group by r.comId, r.date, r.workTime.id, r.term.id "
    )
    List<ReserveCount> queryReserveCount(Long comId);

    /**
     * 根据资源分组进行统计已预约数量
     *
     * @param comId .
     * @param date .
     * @return .
     */
    @Query(
            value = "select new me.zhengjie.modules.yy.domain.ReserveCount(r.comId, r.date, r.workTime.id, r.term.id, count(r)) " +
                    "from Reserve r " +
                    "where 1=1 " +
                    "    and r.comId = ?1 " +
                    "    and r.date = ?2 " +
                    "    and r.status = '1' " +
                    "    and r.verifyStatus <> 'canceled' " +
                    "group by r.comId, r.date, r.workTime.id, r.term.id "
    )
    List<ReserveCount> queryReserveCount(Long comId, String date);

    /**
     * 根据资源分组进行统计已预约数量
     *
     * @param comId .
     * @param date .
     * @param termId .
     * @return .
     */
    @Query(
            value = "select new me.zhengjie.modules.yy.domain.ReserveCount(r.comId, r.date, r.workTime.id, r.term.id, count(r)) " +
                    "from Reserve r " +
                    "where 1=1 " +
                    "    and r.comId = ?1 " +
                    "    and r.date = ?2 " +
                    "    and r.term.id = ?3 " +
                    "    and r.status = '1' " +
                    "    and r.verifyStatus <> 'canceled' " +
                    "group by r.comId, r.date, r.workTime.id, r.term.id "
    )
    List<ReserveCount> queryReserveCount(Long comId, String date, Long termId);

    /**
     * 根据资源分组进行统计已预约数量
     *
     * @param comId .
     * @param date .
     * @param termId .
     * @param resourceGroupId .
     * @return .
     */
    @Query(
            value = "select new me.zhengjie.modules.yy.domain.ReserveCount(r.comId, r.date, r.workTime.id, r.term.id, count(r)) " +
                    "from Reserve r " +
                    "where 1=1 " +
                    "    and r.comId = ?1 " +
                    "    and r.date = ?2 " +
                    "    and r.term.id = ?3 " +
                    "    and r.resourceGroup.id = ?4 " +
                    "    and r.status = '1' " +
                    "    and r.verifyStatus <> 'canceled' " +
                    "group by r.comId, r.date, r.workTime.id, r.term.id "
    )
    List<ReserveCount> queryReserveCount(Long comId, String date, Long termId, Long resourceGroupId);

    /**
     * 查询用户工作量
     *
     * @param comId .
     * @param beginDate .
     * @param endDate .
     * @return .
     */
    @Query(
            value = "" +
                    "select new me.zhengjie.modules.yy.domain.UserReserveCount(ro.id, ro.nickName, r.term.id, r.term.name, r.date, count(r)) " +
                    "from Reserve r " +
                    "inner join r.operators ro " +
                    "where r.comId = ?1 and r.date >= ?2 and r.date <= ?3 and r.status = '1' and r.verifyStatus = 'verified' " +
                    "group by ro.id, r.term.id, r.date "
    )
    List<UserReserveCount> queryUserReserveCount(Long comId, String beginDate, String endDate);

    /**
     * 查询用户工作量
     *
     * @param comId .
     * @param beginDate .
     * @param endDate .
     * @return .
     */
    @Query(
            value = "" +
                    "select new me.zhengjie.modules.yy.domain.UserReserveCountGroup(ro.id, ro.nickName, r.resourceGroup.id, r.resourceGroup.name, r.date, count(r)) " +
                    "from Reserve r " +
                    "inner join r.operators ro " +
                    "where r.comId = ?1 and r.date >= ?2 and r.date <= ?3 and r.status = '1' and r.verifyStatus = 'verified' " +
                    "group by ro.id, r.resourceGroup.id, r.date "
    )
    List<UserReserveCountGroup> queryUserReserveCountGroup(Long comId, String beginDate, String endDate);
}
