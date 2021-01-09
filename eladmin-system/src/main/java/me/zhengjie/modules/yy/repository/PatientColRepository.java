package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.PatientCol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface PatientColRepository extends JpaRepository<PatientCol, Long>, JpaSpecificationExecutor<PatientCol> {

    /**
     * 根据部门 ID 来插入一条默认数据
     *
     * @param deptId .
     * @return .
     */
    @Modifying
    @Query(
            value = "insert into yy_patient_col(patient_id, dept_id, status) " +
                    "select id, ?1, '1' from yy_patient where id not in (select patient_id from yy_patient_col) ",
            nativeQuery = true
    )
    int insertByDeptId(Long deptId);

    /**
     * 更新状态
     *
     * @param id     .
     * @param status .
     * @return .
     */
    @Modifying
    @Query(value = "update PatientCol set status = ?2 where id = ?1")
    int updateStatus(Long id, String status);

}
