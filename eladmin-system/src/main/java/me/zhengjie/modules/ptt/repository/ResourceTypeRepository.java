package me.zhengjie.modules.ptt.repository;

import me.zhengjie.modules.ptt.domain.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 资源类型
 *
 * @author yanjun
 * @date 2020-11-28 15:24
 */
public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long>, JpaSpecificationExecutor<ResourceType> {

}
