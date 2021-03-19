package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    /**
     * 根据外部系统 ID 查询患者
     *
     * @param code .
     * @return .
     */
    Patient findByCode(String code);

    /**
     * 根据档案编号查询患者
     *
     * @param mrg .
     * @return .
     */
    @Query(value = "from Patient where mrn = ?1 and status = 'YES'")
    List<Patient> findAllByMrn(String mrg);

    /**
     * 根据电话查询患者
     *
     * @param phone .
     * @return .
     */
    @Query(value = "from Patient where phone = ?1 and status = 'YES'")
    List<Patient> findAllByPhone(String phone);

    /**
     * 根据姓名查询患者
     *
     * @param name .
     * @return .
     */
    @Query(value = "from Patient where name = ?1 and status = 'YES'")
    List<Patient> findAllByName(String name);

}
