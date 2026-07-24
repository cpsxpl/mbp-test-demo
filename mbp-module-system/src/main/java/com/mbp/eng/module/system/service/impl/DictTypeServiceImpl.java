package com.mbp.eng.module.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.annotations.VisibleForTesting;
import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.common.util.date.LocalDateTimeUtils;
import com.mbp.eng.framework.common.util.object.BeanUtils;
import com.mbp.eng.module.system.service.DictDataService;
import com.mbp.eng.module.system.domain.SystemDictType;
import com.mbp.eng.module.system.mapper.DictTypeMapper;
import com.mbp.eng.module.system.service.DictTypeService;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DictTypePageReqVO;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DictTypeSaveReqVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_TYPE_HAS_CHILDREN;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_TYPE_NAME_DUPLICATE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_TYPE_TYPE_DUPLICATE;


/**
 * 字典类型 Service 实现类
 */
@Service("dictTypeService")
public class DictTypeServiceImpl implements DictTypeService {

    @Resource
    private DictDataService dictDataService;

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Override
    public PageResult<SystemDictType> getDictTypePage(DictTypePageReqVO pageReqVO) {
        return dictTypeMapper.selectPage(pageReqVO);
    }

    @Override
    public SystemDictType getDictType(Long id) {
        return dictTypeMapper.selectById(id);
    }

    @Override
    public SystemDictType getDictType(String type) {
        return dictTypeMapper.selectByType(type);
    }

    @Override
    public Long createDictType(DictTypeSaveReqVO createReqVO) {
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(null, createReqVO.getName());
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(null, createReqVO.getType());

        // 插入字典类型
        SystemDictType dictType = BeanUtils.toBean(createReqVO, SystemDictType.class);
        dictType.setDeletedTime(LocalDateTimeUtils.EMPTY); // 唯一索引，避免 null 值
        dictTypeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void updateDictType(DictTypeSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDictTypeExists(updateReqVO.getId());
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(updateReqVO.getId(), updateReqVO.getName());
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(updateReqVO.getId(), updateReqVO.getType());

        // 更新字典类型
        SystemDictType updateObj = BeanUtils.toBean(updateReqVO, SystemDictType.class);
        dictTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictType(Long id) {
        // 校验是否存在
        SystemDictType dictType = validateDictTypeExists(id);
        // 校验是否有字典数据
        if (dictDataService.getDictDataCountByDictType(dictType.getType()) > 0) {
            throw exception(DICT_TYPE_HAS_CHILDREN);
        }
        // 删除字典类型
        dictTypeMapper.updateToDelete(id, LocalDateTime.now());
    }

    @Override
    public void deleteDictTypeList(List<Long> ids) {
        // 1. 校验是否有字典数据
        List<SystemDictType> dictTypes = dictTypeMapper.selectByIds(ids);
        dictTypes.forEach(dictType -> {
            if (dictDataService.getDictDataCountByDictType(dictType.getType()) > 0) {
                throw exception(DICT_TYPE_HAS_CHILDREN);
            }
        });

        // 2. 批量删除字典类型
        LocalDateTime now = LocalDateTime.now();
        ids.forEach(id -> dictTypeMapper.updateToDelete(id, now));
    }

    @Override
    public List<SystemDictType> getDictTypeList() {
        return dictTypeMapper.selectList();
    }

    @VisibleForTesting
    void validateDictTypeNameUnique(Long id, String name) {
        SystemDictType dictType = dictTypeMapper.selectByName(name);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_NAME_DUPLICATE);
        }
    }

    @VisibleForTesting
    void validateDictTypeUnique(Long id, String type) {
        if (StrUtil.isEmpty(type)) {
            return;
        }
        SystemDictType dictType = dictTypeMapper.selectByType(type);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw exception(DICT_TYPE_TYPE_DUPLICATE);
        }
    }

    @VisibleForTesting
    SystemDictType validateDictTypeExists(Long id) {
        if (id == null) {
            return null;
        }
        SystemDictType dictType = dictTypeMapper.selectById(id);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        return dictType;
    }

}
