package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientWithDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 自定义
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientWithDeptRepository extends JpaRepository<PatientWithDept, PatientWithDept.PK>, JpaSpecificationExecutor<PatientWithDept> {

}
