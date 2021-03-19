package me.zhengjie.modules.yy.repository;

import me.zhengjie.modules.yy.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-24 14:27
 */
public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {

    /**
     * 根据 资源分组ID 查询所有资源
     *
     * @param resourceCategoryId .
     * @return .
     */
    @Query("from Resource where resourceCategory.id = ?1 and status = 1")
    List<Resource> findAllByResourceCategoryId(Long resourceCategoryId);

}
