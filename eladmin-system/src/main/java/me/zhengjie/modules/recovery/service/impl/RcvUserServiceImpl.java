/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.recovery.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.modules.recovery.domain.RcvUser;
import me.zhengjie.modules.recovery.domain.vo.RcvUserVo;
import me.zhengjie.modules.recovery.repository.RcvUserRepository;
import me.zhengjie.modules.recovery.service.*;
import me.zhengjie.modules.recovery.service.dto.*;
import me.zhengjie.modules.recovery.service.mapstruct.RcvUserMapper;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.service.HisService;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-10-25
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class RcvUserServiceImpl implements RcvUserService {

    private final RcvUserRepository rcvUserRepository;
    private final RcvUserMapper rcvUserMapper;

    private final DeptRepository deptRepository;

    private final HisService hisService;

    private final RcvHisLogService rcvHisLogService;
    private final RcvItemService rcvItemService;
    private final RcvItemResourceTypeService rcvItemResourceTypeService;
    private final RcvItemTimeSettingService rcvItemTimeSettingService;
    private final RcvUserItemService rcvUserItemService;

    @Transactional
    @Override
    public Map<String, Object> syncData(JwtUserDto currentUser, HisCkItemVo vo) throws Exception {
        log.debug("syncData: in: " + vo);

        List<HisCkItemDto> ckItemList = hisService.asyncQueryCkItemList(vo);
        log.debug("syncData: his 查询结果: " + ckItemList);
        if (ckItemList == null || ckItemList.isEmpty()) {
            throw new RuntimeException("查询 HIS 无数据");
        }

        // 当前用户部门
        Long userDeptId = currentUser.getUser().getDept().getId();
        log.info("当前用户部门: " + userDeptId);
        Dept userDept = deptRepository.findById(userDeptId).orElseGet(Dept::new);
        if (null == userDept.getId()) {
            throw new RuntimeException("当前用户部门为空");
        }

        // 保存查询结果
        List<RcvHisLogDto> logList = rcvHisLogService.createOrUpdate(ckItemList);
        log.debug("syncData: 保存查询结果: " + logList);

        // 保存或更新预约项目信息
        List<RcvItemDto> itemList = rcvItemService.createOrUpdate(userDept, ckItemList);
        log.debug("syncData: 保存项目信息结果: " + itemList);
        if (null == itemList || itemList.isEmpty()) {
            throw new RuntimeException("保存预约项目信息失败");
        }

        // 保存或更新患者信息
        List<RcvUserDto> userList = createOrUpdate(userDept, ckItemList);
        log.debug("syncData: 保存患者信息结果: " + userList);
        if (null == userList) {
            throw new RuntimeException("保存用户信息失败");
        }

        // 处理患者信息
        Map<Long, RcvUserVo> userMap = new HashMap<>();
        for (RcvUserDto user : userList) {
            if (null == user || null == user.getPatientId()) continue;

            Long patientId = user.getPatientId();

            RcvUserVo userVo = new RcvUserVo();
            userVo.setPatientId(patientId);
            userVo.setUser(user);

            userMap.put(patientId, userVo);
        }

        // 处理患者项目信息
        for (RcvItemDto item : itemList) {
            if (null == item || null == item.getCkItem()) continue;

            HisCkItemDto ckItem = item.getCkItem();
            RcvUserVo userVo = userMap.get(ckItem.getPatientId());
            if (null == userVo) continue;

            userVo.addItem(item);
            userVo.addCkItem(ckItem);
        }

        // 保存患者项目信息
        for (RcvUserDto user : userList) {
            if (null == user || null == user.getPatientId()) continue;

            RcvUserVo userVo = userMap.get(user.getPatientId());
            if (null == userVo || null == userVo.getItemList() || null == userVo.getCkItemList()) continue;

            List<RcvUserItemDto> userItemList = rcvUserItemService.createOrUpdate(user, userVo.getItemList(), userVo.getCkItemList());
            if (null == userItemList) {
                throw new RuntimeException("新增患者项目失败");
            }
        }

        Map<String, Object> res = PageUtil.toPage(userList, userList.size());
        log.debug("syncData: out: " + res);
        return res;
    }

    @Transactional
    @Override
    public List<RcvUserDto> createOrUpdate(Dept userDept, List<HisCkItemDto> ckItemList) {
        if (null == ckItemList || ckItemList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<Long, RcvUser> userMap = new HashMap<>();
        for (HisCkItemDto ckItem : ckItemList) {
            if (null == ckItem || null == ckItem.getPatientId()) continue;

            // 忽略刚刚添加过的数据
            if (userMap.containsKey(ckItem.getPatientId())) {
                continue;
            }

            RcvUser user = rcvUserRepository.findFirstByPatientId(ckItem.getPatientId()).orElseGet(RcvUser::new);
            if (user.getId() == null) {
                Snowflake snowflake = IdUtil.createSnowflake(1, 1);
                user.setId(snowflake.nextId());
                user.setPatientId(ckItem.getPatientId());
                user.setName(ckItem.getName());
                user.setPhone(ckItem.getMobilePhone());

                user = rcvUserRepository.save(user);
            }

            // 关联部门
            if (null != userDept) {
                Set<Dept> depts = user.getDepts();
                if (null == depts) {
                    depts = new LinkedHashSet<>();
                }
                depts.add(userDept);
                user.setDepts(depts);
                user = rcvUserRepository.save(user);
            }

            userMap.put(user.getPatientId(), user);
        }

        return rcvUserMapper.toDto(new ArrayList<>(userMap.values()));
    }

    @Override
    public Map<String, Object> queryAll(RcvUserQueryCriteria criteria, Pageable pageable) {
        Page<RcvUser> page = rcvUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvUserMapper::toDto));
    }

    @Override
    public List<RcvUserDto> queryAll(RcvUserQueryCriteria criteria) {
        return rcvUserMapper.toDto(rcvUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvUserDto findById(Long id) {
        RcvUser rcvUser = rcvUserRepository.findById(id).orElseGet(RcvUser::new);
        ValidationUtil.isNull(rcvUser.getId(), "RcvUser", "id", id);
        return rcvUserMapper.toDto(rcvUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvUserDto create(RcvUser resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvUserMapper.toDto(rcvUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvUser resources) {
        RcvUser rcvUser = rcvUserRepository.findById(resources.getId()).orElseGet(RcvUser::new);
        ValidationUtil.isNull(rcvUser.getId(), "RcvUser", "id", resources.getId());
        rcvUser.copy(resources);
        rcvUserRepository.save(rcvUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvUserDto rcvUser : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("姓名", rcvUser.getName());
            map.put("身份证", rcvUser.getCertNo());
            map.put("电话", rcvUser.getPhone());
            map.put("生日", rcvUser.getBirthday());
            map.put("职业", rcvUser.getProfession());
            map.put("地址", rcvUser.getAddress());
            map.put("联系人", rcvUser.getContactName());
            map.put("联系电话", rcvUser.getContactPhone());
            map.put("联系人关系", rcvUser.getContactRelation());
            map.put("状态", rcvUser.getStatus());
            map.put("创建人", rcvUser.getCreateBy());
            map.put("修改人", rcvUser.getUpdatedBy());
            map.put("创建时间", rcvUser.getCreateTime());
            map.put("修改时间", rcvUser.getUpdateTime());
            map.put("备注", rcvUser.getRemark());
            map.put("col1", rcvUser.getCol1());
            map.put("col2", rcvUser.getCol2());
            map.put("col3", rcvUser.getCol3());
            map.put("col4", rcvUser.getCol4());
            map.put("col5", rcvUser.getCol5());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}