package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yanjun
 * @date 2020-12-24 21:19
 */
public interface HospitalRepository extends JpaRepository<Hospital, Long>, JpaSpecificationExecutor<Hospital> {

}
