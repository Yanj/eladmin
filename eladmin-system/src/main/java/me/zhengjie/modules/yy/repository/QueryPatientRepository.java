package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.QueryPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface QueryPatientRepository extends JpaRepository<QueryPatient, Long>, JpaSpecificationExecutor<QueryPatient> {

}
