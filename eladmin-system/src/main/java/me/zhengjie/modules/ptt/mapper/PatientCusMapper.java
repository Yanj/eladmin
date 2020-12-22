package me.zhengjie.modules.ptt.mapper;

import me.zhengjie.modules.ptt.domain.PatientCus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-09 22:53
 */
@Component
@Mapper
public interface PatientCusMapper {

    List<PatientCus> selectFull();

}
