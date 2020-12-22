package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.HisLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * HIS查询日志
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface HisLogRepository extends JpaRepository<HisLog, Long>, JpaSpecificationExecutor<HisLog> {

}
