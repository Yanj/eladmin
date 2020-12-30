package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.PatientTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface PatientTermRepository extends JpaRepository<PatientTerm, Long>, JpaSpecificationExecutor<PatientTerm> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("from PatientTerm where id = ?1")
    PatientTerm getPatientTermForUpdate(Long id);

    PatientTerm findByTermCode(String termCode);

}
