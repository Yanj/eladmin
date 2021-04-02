package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DictDetailService;
import me.zhengjie.modules.system.service.DictService;
import me.zhengjie.modules.system.service.dto.DictDetailDto;
import me.zhengjie.modules.yy.domain.Reserve;
import me.zhengjie.modules.yy.domain.ResourceGroup;
import me.zhengjie.modules.yy.domain.WorkTime;
import me.zhengjie.modules.yy.repository.ReserveRepository;
import me.zhengjie.modules.yy.repository.ResourceGroupRepository;
import me.zhengjie.modules.yy.repository.WorkTimeRepository;
import me.zhengjie.modules.yy.service.WorkTimeReserveListService;
import me.zhengjie.modules.yy.service.dto.*;
import me.zhengjie.modules.yy.service.mapstruct.ReserveMapper;
import me.zhengjie.modules.yy.service.mapstruct.ResourceGroupSmallMapper;
import me.zhengjie.modules.yy.service.mapstruct.WorkTimeSmallMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.enums.YesNoEnum;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author yanjun
 * @date 2021-04-01 10:33
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTimeReserveListServiceImpl implements WorkTimeReserveListService {

    private final ReserveRepository reserveRepository;
    private final ReserveMapper reserveMapper;

    private final WorkTimeRepository workTimeRepository;
    private final WorkTimeSmallMapper workTimeSmallMapper;

    private final ResourceGroupRepository resourceGroupRepository;
    private final ResourceGroupSmallMapper resourceGroupSmallMapper;

    private final DictService dictService;
    private final DictDetailService dictDetailService;

    @Override
    public void download(List<WorkTimeSmallDto> workTimeList, List<ResourceGroupWorkTimeReserveListDto> all, boolean showStatus, HttpServletResponse response) throws IOException {
        List<DictDetailDto> dictDetailList = dictDetailService.getDictByName("reserve_verify_status");
        Map<String, String> dictLabel = new HashMap<>();
        for (DictDetailDto dictDetail : dictDetailList) {
            dictLabel.put(dictDetail.getValue(), dictDetail.getLabel());
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResourceGroupWorkTimeReserveListDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("资源组", item.getResourceGroup().getName());
            WorkTimeSmallDto workTime;
            for (int i = 0; i < workTimeList.size(); i++) {
                workTime = workTimeList.get(i);
                StringBuilder str = new StringBuilder();
                for (ReserveDto reserve : item.getList().get(i).getReserves()) {
                    str.append(reserve.getPatient().getName());
                    if (showStatus) {
                        str.append("(")
                                .append(dictLabel.get(reserve.getVerifyStatus()))
                                .append(")");
                    }
                    str.append("\r\n");
                }
                map.put(workTime.getBeginTime(), str);
            }
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<WorkTimeSmallDto> queryWorkTimeList(WorkTimeReserveListCriteria criteria) {
        Long comId = criteria.getComId();
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (!user.isAdmin()) {
            comId = user.getComId();
        }
        if (null == comId) {
            throw new BadRequestException("comId不能为空");
        }
        List<WorkTime> workTimeList = workTimeRepository.findByComId(comId);
        return workTimeSmallMapper.toDto(workTimeList);
    }

    @Override
    public List<ResourceGroupWorkTimeReserveListDto> queryWorkTimeReserveList(WorkTimeReserveListCriteria criteria) {
        Long comId = criteria.getComId();
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (!user.isAdmin()) {
            comId = user.getComId();
        }
        if (null == comId) {
            throw new BadRequestException("comId不能为空");
        }

        List<WorkTime> workTimeList = workTimeRepository.findByComId(comId);
        List<ResourceGroup> resourceGroupList = resourceGroupRepository.findByComId(comId);

        ReserveCriteria reserveCriteria = new ReserveCriteria();
        reserveCriteria.setComId(comId);
        reserveCriteria.setStatus(YesNoEnum.YES);
        reserveCriteria.setDate(criteria.getDate());
        reserveCriteria.setVerifyStatus(criteria.getVerifyStatus());
        List<Reserve> reserveList = reserveRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, reserveCriteria, criteriaBuilder));

        List<ResourceGroupWorkTimeReserveListDto> res = new ArrayList<>();
        for (ResourceGroup resourceGroup : resourceGroupList) {
            ResourceGroupWorkTimeReserveListDto resourceGroupWorkTimeReserveList = new ResourceGroupWorkTimeReserveListDto();
            resourceGroupWorkTimeReserveList.setResourceGroup(resourceGroupSmallMapper.toDto(resourceGroup));
            List<WorkTimeReserveListDto> list = new ArrayList<>();
            for (WorkTime workTime : workTimeList) {
                WorkTimeReserveListDto workTimeReserveList = new WorkTimeReserveListDto();
                workTimeReserveList.setWorkTime(workTimeSmallMapper.toDto(workTime));
                List<ReserveDto> reserves = new ArrayList<>();
                for (Reserve reserve : reserveList) {
                    if (null == reserve.getResourceGroup() || !resourceGroup.getId().equals(reserve.getResourceGroup().getId())) {
                        continue;
                    }
                    if (null == reserve.getWorkTime() || !workTime.getId().equals(reserve.getWorkTime().getId())) {
                        continue;
                    }
                    reserves.add(reserveMapper.toDto(reserve));
                }
                workTimeReserveList.setReserves(reserves);
                list.add(workTimeReserveList);
            }
            resourceGroupWorkTimeReserveList.setList(list);
            res.add(resourceGroupWorkTimeReserveList);
        }

        return res;
    }

}
