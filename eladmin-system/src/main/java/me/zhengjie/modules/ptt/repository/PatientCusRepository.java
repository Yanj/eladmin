package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientCus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * 患者自定义
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientCusRepository extends JpaRepository<PatientCus, Long>, JpaSpecificationExecutor<PatientCus> {

    Optional<PatientCus> findByCusIdAndPatientId(Long cusId, Long patientId);

    List<PatientCus> findAllByDeptIdAndPatientId(Long deptId, Long patientId);

    void deleteAllByDeptIdAndPatientId(Long deptId, Long patientId);

}
