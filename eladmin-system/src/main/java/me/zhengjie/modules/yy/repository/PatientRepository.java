package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

}
