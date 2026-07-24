package com.mbp.eng.module.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.annotations.VisibleForTesting;
import com.mbp.eng.framework.common.enums.CommonStatusEnum;
import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.common.util.collection.CollectionUtils;
import com.mbp.eng.framework.common.util.object.BeanUtils;
import com.mbp.eng.module.system.service.DictDataService;
import com.mbp.eng.module.system.service.DictTypeService;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DictDataSaveReqVO;
import com.mbp.eng.module.system.domain.SystemDictData;
import com.mbp.eng.module.system.domain.SystemDictType;
import com.mbp.eng.module.system.mapper.DictDataMapper;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DictDataPageReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_DATA_NOT_ENABLE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_DATA_NOT_EXISTS;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_DATA_VALUE_DUPLICATE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_TYPE_NOT_ENABLE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.DICT_TYPE_NOT_EXISTS;


/**
 * 字典数据 Service 实现类
 */
@Service("dictDataService")
@Slf4j
public class DictDataServiceImpl implements DictDataService {

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<SystemDictData> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(SystemDictData::getDictType)
            .thenComparingInt(SystemDictData::getSort);

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private DictDataMapper dictDataMapper;

    @Override
    public List<SystemDictData> getDictDataList(Integer status, String dictType) {
        List<SystemDictData> list = dictDataMapper.selectListByStatusAndDictType(status, dictType);
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    @Override
    public PageResult<SystemDictData> getDictDataPage(DictDataPageReqVO pageReqVO) {
        return dictDataMapper.selectPage(pageReqVO);
    }

    @Override
    public SystemDictData getDictData(Long id) {
        return dictDataMapper.selectById(id);
    }

    @Override
    public Long createDictData(DictDataSaveReqVO createReqVO) {
        // 校验字典类型有效
        validateDictTypeExists(createReqVO.getDictType());
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(null, createReqVO.getDictType(), createReqVO.getValue());

        // 插入字典类型
        SystemDictData dictData = BeanUtils.toBean(createReqVO, SystemDictData.class);
        dictDataMapper.insert(dictData);
        return dictData.getId();
    }

    @Override
    public void updateDictData(DictDataSaveReqVO updateReqVO) {
        // 校验自己存在
        validateDictDataExists(updateReqVO.getId());
        // 校验字典类型有效
        validateDictTypeExists(updateReqVO.getDictType());
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(updateReqVO.getId(), updateReqVO.getDictType(), updateReqVO.getValue());

        // 更新字典类型
        SystemDictData updateObj = BeanUtils.toBean(updateReqVO, SystemDictData.class);
        dictDataMapper.updateById(updateObj);
    }

    @Override
    public void deleteDictData(Long id) {
        // 校验是否存在
        validateDictDataExists(id);

        // 删除字典数据
        dictDataMapper.deleteById(id);
    }

    @Override
    public void deleteDictDataList(List<Long> ids) {
        dictDataMapper.deleteByIds(ids);
    }

    @Override
    public long getDictDataCountByDictType(String dictType) {
        return dictDataMapper.selectCountByDictType(dictType);
    }

    @VisibleForTesting
    public void validateDictDataValueUnique(Long id, String dictType, String value) {
        SystemDictData dictData = dictDataMapper.selectByDictTypeAndValue(dictType, value);
        if (dictData == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
        if (!dictData.getId().equals(id)) {
            throw exception(DICT_DATA_VALUE_DUPLICATE);
        }
    }

    @VisibleForTesting
    public void validateDictDataExists(Long id) {
        if (id == null) {
            return;
        }
        SystemDictData dictData = dictDataMapper.selectById(id);
        if (dictData == null) {
            throw exception(DICT_DATA_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    public void validateDictTypeExists(String type) {
        SystemDictType dictType = dictTypeService.getDictType(type);
        if (dictType == null) {
            throw exception(DICT_TYPE_NOT_EXISTS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw exception(DICT_TYPE_NOT_ENABLE);
        }
    }

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        if (CollUtil.isEmpty(values)) {
            return;
        }
        Map<String, SystemDictData> dictDataMap = CollectionUtils.convertMap(
                dictDataMapper.selectByDictTypeAndValues(dictType, values), SystemDictData::getValue);
        // 校验
        values.forEach(value -> {
            SystemDictData dictData = dictDataMap.get(value);
            if (dictData == null) {
                throw exception(DICT_DATA_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dictData.getStatus())) {
                throw exception(DICT_DATA_NOT_ENABLE, dictData.getLabel());
            }
        });
    }

    @Override
    public SystemDictData getDictData(String dictType, String value) {
        return dictDataMapper.selectByDictTypeAndValue(dictType, value);
    }

    @Override
    public SystemDictData parseDictData(String dictType, String label) {
        return dictDataMapper.selectByDictTypeAndLabel(dictType, label);
    }

    @Override
    public List<SystemDictData> getDictDataListByDictType(String dictType) {
        List<SystemDictData> list = dictDataMapper.selectList(SystemDictData::getDictType, dictType);
        list.sort(Comparator.comparing(SystemDictData::getSort));
        return list;
    }

}
