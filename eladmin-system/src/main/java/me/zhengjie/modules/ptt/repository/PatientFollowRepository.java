package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 患者跟进
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientFollowRepository extends JpaRepository<PatientFollow, Long>, JpaSpecificationExecutor<PatientFollow> {

}
