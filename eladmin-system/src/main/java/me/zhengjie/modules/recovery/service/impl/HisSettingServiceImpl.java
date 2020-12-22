/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-10-22 17:32
 */
package me.zhengjie.modules.recovery.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.exception.EntityNotFoundException;
import me.zhengjie.modules.recovery.domain.HisSetting;
import me.zhengjie.modules.recovery.repository.HisSettingRepository;
import me.zhengjie.modules.recovery.service.HisSettingService;
import me.zhengjie.modules.recovery.service.dto.HisSettingCriteria;
import me.zhengjie.modules.recovery.service.dto.HisSettingDto;
import me.zhengjie.modules.recovery.service.mapstruct.HisSettingMapper;
import me.zhengjie.utils.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author yanjun
 * @date 2020-10-22 17:32
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "hisSetting")
public class HisSettingServiceImpl implements HisSettingService {

    private final HisSettingMapper hisSettingMapper;
    private final HisSettingRepository hisSettingRepository;

    private final RedisUtils redisUtils;

    @Override
    @Cacheable(key = "'id:' + #p0")
    public HisSettingDto findById(long id) {
        HisSetting hisSetting = hisSettingRepository.findById(id).orElseGet(HisSetting::new);
        ValidationUtil.isNull(hisSetting.getId(), "HisSetting", "id", id);
        return hisSettingMapper.toDto(hisSetting);
    }

    @Override
    @Cacheable(key = "'name:' + #p0")
    public HisSettingDto findByName(String name) {
        HisSetting hisSetting = hisSettingRepository.findByName(name);
        if (hisSetting == null) {
            throw new EntityNotFoundException(HisSetting.class, "name", name);
        } else {
            return hisSettingMapper.toDto(hisSetting);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(HisSetting resources) {
        if (hisSettingRepository.findByName(resources.getName()) != null) {
            throw new EntityExistException(HisSetting.class, "name", resources.getName());
        }
        hisSettingRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(HisSetting resources) {
        HisSetting hisSetting = hisSettingRepository.findById(resources.getId()).orElseGet(HisSetting::new);
        ValidationUtil.isNull(hisSetting.getId(), "HisSetting", "id", resources.getId());
        resources.setId(hisSetting.getId());
        if (resources.getEnabled() != null && resources.getEnabled()) { // 更新已设置过值
            HisSetting old = hisSettingRepository.findByEnabled(true);
            if (old != null) {
                old.setEnabled(false);

                // 保存
                hisSettingRepository.save(old);
                // 清除缓存
                delCaches(old.getId(), old.getName());
            }
        }
        // 保存
        hisSettingRepository.save(resources);
        // 清除缓存
        delCaches(hisSetting.getId(), hisSetting.getName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            HisSettingDto hisSettingDto = findById(id);
            // 清除缓存
            delCaches(hisSettingDto.getId(), hisSettingDto.getName());
        }
        hisSettingRepository.deleteAllByIdIn(ids);
    }

    @Override
    public Object queryAll(HisSettingCriteria criteria, Pageable pageable) {
        Page<HisSetting> page =
                hisSettingRepository.findAll(
                        (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),
                        pageable
                );
        return PageUtil.toPage(page.map(hisSettingMapper::toDto));
    }

    @Override
    public List<HisSettingDto> queryAll(HisSettingCriteria criteria) {
        List<HisSetting> list = hisSettingRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)
        );
        return hisSettingMapper.toDto(list);
    }

    @Override
    public void download(List<HisSettingDto> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HisSettingDto dto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", dto.getName());
            map.put("地址", dto.getAddress());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    public void delCaches(Long id, String name) {
        redisUtils.del("hisSetting::id:" + id);
        redisUtils.del("hisSetting::name:" + name);
    }

}
