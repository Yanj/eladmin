package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.Cus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 自定义
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface CusRepository extends JpaRepository<Cus, Long>, JpaSpecificationExecutor<Cus> {

    @Query(value = "from Cus where dept.id = :deptId", countQuery = "select count(1) from Cus where dept.id = :deptId")
    Page<Cus> findByDeptId(@Param("deptId") Long deptId, Pageable pageable);

}
