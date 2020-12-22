package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.PatientTermReserveResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 患者套餐预约资源
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface PatientTermReserveResourceRepository extends JpaRepository<PatientTermReserveResource, Long>, JpaSpecificationExecutor<PatientTermReserveResource> {

}
