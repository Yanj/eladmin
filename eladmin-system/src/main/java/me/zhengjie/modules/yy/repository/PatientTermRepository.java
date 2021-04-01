package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.PatientTerm;
import me.zhengjie.modules.yy.domain.PatientTermTimesCount;
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
public interface PatientTermRepository extends JpaRepository<PatientTerm, Long>, JpaSpecificationExecutor<PatientTerm> {

    /**
     * 锁行查询患者套餐
     *
     * @param id .
     * @return .
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("from PatientTerm where id = ?1")
    PatientTerm getPatientTermForUpdate(Long id);

    /**
     * 查询套餐使用情况
     *
     * @param comId .
     * @return .
     */
    @Query(
            value = "" +
                    "select new me.zhengjie.modules.yy.domain.PatientTermTimesCount(" +
                    "pt.termId, pt.termName, pt.termPrice, COALESCE(sum(pt.totalTimes), 0), COALESCE(sum(pt.times), 0)" +
                    ") " +
                    "from PatientTerm pt " +
                    "where pt.comId = ?1 and pt.status = 1 and pt.termId is not null " +
                    "group by pt.termId "
    )
    List<PatientTermTimesCount> queryPatientTermTimesCount(Long comId);

}
