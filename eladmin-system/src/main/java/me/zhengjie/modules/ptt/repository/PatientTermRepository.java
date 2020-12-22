package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 患者套餐
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientTermRepository extends JpaRepository<PatientTerm, Long>, JpaSpecificationExecutor<PatientTerm> {

    Optional<PatientTerm> findFirstByPatientIdAndTermId(Long patientId, Long termId);

}
