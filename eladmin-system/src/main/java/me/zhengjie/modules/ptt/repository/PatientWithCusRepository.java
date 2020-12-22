package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientWithCus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 自定义
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientWithCusRepository extends JpaRepository<PatientWithCus, PatientWithCus.PK>, JpaSpecificationExecutor<PatientWithCus> {

    List<PatientWithCus> findByDeptIdAndPatientId(Long deptId, Long patientId);

}
