package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    Patient findByCode(String code);

    List<Patient> findAllByMrn(String mrg);

    List<Patient> findAllByPhone(String phone);

    List<Patient> findAllByName(String name);

}
