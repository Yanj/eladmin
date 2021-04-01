package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
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
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.enums.YesNoEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
