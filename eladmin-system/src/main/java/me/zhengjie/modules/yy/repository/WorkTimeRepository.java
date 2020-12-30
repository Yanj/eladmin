package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long>, JpaSpecificationExecutor<WorkTime> {

    List<WorkTime> findAllByDeptIdOrderByBeginTime(Long deptId);

}
