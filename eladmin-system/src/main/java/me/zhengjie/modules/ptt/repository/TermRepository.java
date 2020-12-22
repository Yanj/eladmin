package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 套餐
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface TermRepository extends JpaRepository<Term, Long>, JpaSpecificationExecutor<Term> {

    Optional<Term> findFirstByCode(String code);

}
