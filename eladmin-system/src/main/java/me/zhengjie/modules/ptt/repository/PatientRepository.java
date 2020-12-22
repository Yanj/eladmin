package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.Patient;
import me.zhengjie.modules.ptt.domain.PatientWithCols;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 患者
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    @Query(value = "select * from ptt_patient where id in (select patient_id from ptt_patient_dept where dept_id = ?1)", nativeQuery = true)
    List<Patient> findByDeptId(Long deptId);

    Optional<Patient> findFirstByPatientId(Long patientId);

    @Query(
            value = "select " +
                    "  p.id patient_id, p.patient_id patient_patient_id, p.`name` patient_name, p.mrn patient_mrn, p.phone patient_phone, p.status patient_status, d.dept_id dept_dept_id, d.pid dept_pid, d.name dept_name " +
                    "from ptt_patient p " +
                    "left join ptt_patient_dept pd on p.id = pd.patient_id " +
                    "left join sys_dept d on pd.dept_id = d.dept_id",
            countQuery = "select " +
                    " count(1) " +
                    "from ptt_patient p " +
                    "left join ptt_patient_dept pd on p.id = pd.patient_id " +
                    "left join sys_dept d on pd.dept_id = d.dept_id",
            nativeQuery = true
    )
    Page<List<Map<String, Object>>> findWithDept(Pageable pageable);

    @Query(
            value = "select " +
                    "  p.id patient_id, p.patient_id patient_patient_id, p.`name` patient_name, p.mrn patient_mrn, p.phone patient_phone, p.status patient_status, d.dept_id dept_dept_id, d.pid dept_pid, d.name dept_name " +
                    "from ptt_patient p " +
                    "left join ptt_patient_dept pd on p.id = pd.patient_id " +
                    "left join sys_dept d on pd.dept_id = d.dept_id " +
                    "where p.name like %:name%",
            countQuery = "select " +
                    " count(1) " +
                    "from ptt_patient p " +
                    "left join ptt_patient_dept pd on p.id = pd.patient_id " +
                    "left join sys_dept d on pd.dept_id = d.dept_id " +
                    "where p.name like %:name%",
            nativeQuery = true
    )
    Page<List<Map<String, Object>>> findWithDeptByName(@Param("name") String name, Pageable pageable);

    @Query(
            value = "select" +
                    "  c.id cus_id, c.dept_id dept_id, c.type cus_type, c.dict_id cus_dict_id, c.title cus_title, c.cus_sort cus_sort, " +
                    "  pc.id patient_cus_id, pc.patient_id patient_patient_id, pc.value patient_cus_value " +
                    "from ptt_cus c " +
                    "left join (select * from ptt_patient_cus where patient_id=:patientId and dept_id=:deptId) pc on c.id = pc.cus_id",
            countQuery = "select" +
                    "  count(1) " +
                    "from ptt_cus c " +
                    "left join (select * from ptt_patient_cus where patient_id=:patientId and dept_id=:deptId) pc on c.id = pc.cus_id",
            nativeQuery = true
    )
    Page<List<Map<String, Object>>> findCusByPatientId(@Param("deptId") Long deptId, @Param("patientId") Long patientId, Pageable pageable);


    @Query(
            value = "select  p.id, dd.dept_id from ptt_patient p " +
                    "left join ptt_patient_dept d on p.id = d.patient_id " +
                    "left join sys_dept dd on d.dept_id = dd.dept_id ",
            countQuery = "select  count(1) from ptt_patient p " +
                    "left join ptt_patient_dept d on p.id = d.patient_id " +
                    "left join sys_dept dd on d.dept_id = dd.dept_id ",
            nativeQuery = true
    )
    Page<List<PatientWithCols>> findPatientWithCus(Pageable pageable);

}
