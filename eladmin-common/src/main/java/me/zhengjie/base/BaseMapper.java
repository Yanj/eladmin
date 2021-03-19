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
package me.zhengjie.base;

import me.zhengjie.utils.enums.YesNoEnum;
import org.mapstruct.ValueMapping;

import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
public interface BaseMapper<D, E> {

    /**
     * DTO转Entity
     * @param dto /
     * @return /
     */
    E toEntity(D dto);

    /**
     * Entity转DTO
     * @param entity /
     * @return /
     */
    D toDto(E entity);

    /**
     * DTO集合转Entity集合
     * @param dtoList /
     * @return /
     */
    List <E> toEntity(List<D> dtoList);

    /**
     * Entity集合转DTO集合
     * @param entityList /
     * @return /
     */
    List <D> toDto(List<E> entityList);

//    @ValueMapping(source = "NO", target = "0")
//    @ValueMapping(source = "YES", target = "1")
    default String map(YesNoEnum value) {
        switch (value) {
            case NO:
                return "0";
            case YES:
                return "1";
        }
        return null;
    }

    default YesNoEnum map(String value) {
        if ("0".equals(value)) {
            return YesNoEnum.NO;
        }
        if ("1".equals(value)) {
            return YesNoEnum.YES;
        }
        return null;
    }

}
