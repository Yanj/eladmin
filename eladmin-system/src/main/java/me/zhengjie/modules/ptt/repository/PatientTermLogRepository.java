package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientTermLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 患者套餐日志
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientTermLogRepository extends JpaRepository<PatientTermLog, Long>, JpaSpecificationExecutor<PatientTermLog> {

}
