package com.mbp.eng.module.system.service;


import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DictTypePageReqVO;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DictTypeSaveReqVO;
import com.mbp.eng.module.system.domain.SystemDictType;

import java.util.List;

/**
 * 字典类型 Service 接口
 */
public interface DictTypeService {

    /**
     * 创建字典类型
     *
     * @param createReqVO 字典类型信息
     * @return 字典类型编号
     */
    Long createDictType(DictTypeSaveReqVO createReqVO);

    /**
     * 更新字典类型
     *
     * @param updateReqVO 字典类型信息
     */
    void updateDictType(DictTypeSaveReqVO updateReqVO);

    /**
     * 删除字典类型
     *
     * @param id 字典类型编号
     */
    void deleteDictType(Long id);

    /**
     * 批量删除字典类型
     *
     * @param ids 字典类型编号列表
     */
    void deleteDictTypeList(List<Long> ids);

    /**
     * 获得字典类型分页列表
     *
     * @param pageReqVO 分页请求
     * @return 字典类型分页列表
     */
    PageResult<SystemDictType> getDictTypePage(DictTypePageReqVO pageReqVO);

    /**
     * 获得字典类型详情
     *
     * @param id 字典类型编号
     * @return 字典类型
     */
    SystemDictType getDictType(Long id);

    /**
     * 获得字典类型详情
     *
     * @param type 字典类型
     * @return 字典类型详情
     */
    SystemDictType getDictType(String type);

    /**
     * 获得全部字典类型列表
     *
     * @return 字典类型列表
     */
    List<SystemDictType> getDictTypeList();

}
