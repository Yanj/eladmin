package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.ReserveLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ReserveLogRepository extends JpaRepository<ReserveLog, Long>, JpaSpecificationExecutor<ReserveLog> {

}
