package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface TermRepository extends JpaRepository<Term, Long>, JpaSpecificationExecutor<Term> {

    /**
     * 根据外部系统ID查询
     *
     * @param code .
     * @return .
     */
    @Query(value = "from Term where code = ?2 and comId = ?1")
    Optional<Term> findByCode(Long comId, String code);

    /**
     * 根据 机构ID 查询套餐
     * @param orgId .
     * @return .
     */
    @Query(value = "from Term where orgId = ?1 and status = 1")
    List<Term> findAllByOrgId(Long orgId);

    /**
     * 根据 公司ID 查询套餐
     * @param comId .
     * @return .
     */
    @Query(value = "from Term where comId = ?1 and status = 1")
    List<Term> findAllByComId(Long comId);

    /**
     * 根据 部门ID 查询套餐
     * @param deptId .
     * @return .
     */
    @Query(value = "from Term where deptId = ?1 and status = 1")
    List<Term> findAllByDeptId(Long deptId);

}
