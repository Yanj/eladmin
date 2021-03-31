package me.zhengjie.modules.yy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkTimeReserveListDto implements Serializable {

    private WorkTimeSmallDto workTime;

    private List<ReserveDto> reserves;

}
