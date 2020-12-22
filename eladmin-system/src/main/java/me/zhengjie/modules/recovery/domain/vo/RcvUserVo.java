package me.zhengjie.modules.recovery.domain.vo;

import lombok.Data;
import me.zhengjie.modules.recovery.service.dto.RcvItemDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserDto;
import me.zhengjie.service.dto.HisCkItemDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-11-17 16:02
 */
@Data
public class RcvUserVo implements Serializable {

    private Long patientId;

    private RcvUserDto user;

    private List<RcvItemDto> itemList;

    private List<HisCkItemDto> ckItemList;

    public void addItem(RcvItemDto item) {
        if (null == itemList) {
            itemList = new ArrayList<>();
        }
        itemList.add(item);
    }

    public void addCkItem(HisCkItemDto ckItem) {
        if (null == ckItemList) {
            ckItemList = new ArrayList<>();
        }
        ckItemList.add(ckItem);
    }

}
