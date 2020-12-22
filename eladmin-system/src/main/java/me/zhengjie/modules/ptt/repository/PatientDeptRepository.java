package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.Patient;
import me.zhengjie.modules.ptt.domain.PatientDept;
import me.zhengjie.modules.system.domain.Dept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 患者
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientDeptRepository extends JpaRepository<PatientDept, PatientDept.PK>, JpaSpecificationExecutor<PatientDept> {

    List<PatientDept> findByDeptAndPatient(Dept dept, Patient patient);

}
