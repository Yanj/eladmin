package me.zhengjie.modules.ptt.mapper;

import me.zhengjie.modules.ptt.domain.Patient;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-08 23:47
 */
@Component
@Mapper
public interface PatientMapper {

    List<Patient> selectWithCols(Long id);

}
