package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientTermReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 患者套餐预约
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientTermReserveRepository extends JpaRepository<PatientTermReserve, Long>, JpaSpecificationExecutor<PatientTermReserve> {

    @Query(
            value = "select pr.* from ptt_patient_term_reserve pr " +
                    "inner join ptt_term_resource_type pt on pr.term_id = pt.term_id " +
                    "where pt.dept_id = :deptId and pt.term_id = :termId and pr.date = :date",
            nativeQuery = true
    )
    List<PatientTermReserve> findByDeptAndTerm(@Param("deptId") Long deptId, @Param("termId") Long termId, @Param("date") String date);

    @Query(
            value = "select pr.* from ptt_patient_term_reserve pr " +
                    "inner join ptt_term_resource_type pt on pr.term_id = pt.term_id " +
                    "where pt.dept_id = :deptId and pt.resource_type_id = :resourceTypeId and pr.date = :date",
            nativeQuery = true
    )
    List<PatientTermReserve> findByDeptAndResourceType(@Param("deptId") Long deptId, @Param("resourceTypeId") Long resourceTypeId, @Param("date") String date);

    /**
     * 查询套餐指定日期的预约记录
     *
     * @param deptId .
     * @param termId .
     * @param date .
     * @return .
     */
    List<PatientTermReserve> findAllByDeptIdAndTermIdAndDateAndStatus(Long deptId, Long termId, String date, String status);

}
