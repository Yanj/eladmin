package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientTermReserveLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 患者套餐预约日志
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientTermReserveLogRepository extends JpaRepository<PatientTermReserveLog, Long>, JpaSpecificationExecutor<PatientTermReserveLog> {

    @Query(
            value = " select trl.* from ptt_patient_term_reserve_log trl" +
                    " left join ptt_patient_term_reserve tr on trl.patient_term_reserve_id = tr.id " +
                    " left join ptt_patient_term t on tr.patient_term_id = t.id " +
                    " left join ptt_patient p on t.patient_id = p.id " +
                    " where p.id = :patientId ",
            countQuery = " select count(1) from ptt_patient_term_reserve_log trl" +
                    " left join ptt_patient_term_reserve tr on trl.patient_term_reserve_id = tr.id " +
                    " left join ptt_patient_term t on tr.patient_term_id = t.id " +
                    " left join ptt_patient p on t.patient_id = p.id " +
                    " where p.id = :patientId ",
            nativeQuery = true
    )
    Page<PatientTermReserveLog> findAllByPatientId(@Param("patientId") Long patientId, Pageable pageable);

    @Query(
            value = " select trl.* from ptt_patient_term_reserve_log trl" +
                    " left join ptt_patient_term_reserve tr on trl.patient_term_reserve_id = tr.id " +
                    " left join ptt_patient_term t on tr.patient_term_id = t.id " +
                    " left join ptt_patient p on t.patient_id = p.id " +
                    " where p.name like %:name% ",
            countQuery = " select count(1) from ptt_patient_term_reserve_log trl" +
                    " left join ptt_patient_term_reserve tr on trl.patient_term_reserve_id = tr.id " +
                    " left join ptt_patient_term t on tr.patient_term_id = t.id " +
                    " left join ptt_patient p on t.patient_id = p.id " +
                    " where p.name like %:name% ",
            nativeQuery = true
    )
    Page<PatientTermReserveLog> findAllByPatientName(@Param("name") String patientName, Pageable pageable);

}
