package me.zhengjie.modules.yy.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.modules.yy.domain.ReserveVerifyStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkTimeReserveListCriteria extends BaseCriteria implements Serializable {

    private String date;

    private ReserveVerifyStatus verifyStatus;

}
