package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface TermRepository extends JpaRepository<Term, Long>, JpaSpecificationExecutor<Term> {

    Optional<Term> findFirstByCode(String code);

    List<Term> findAllByDeptId(Long deptId);

}
