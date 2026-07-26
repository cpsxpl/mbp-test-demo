package com.mbp.eng.framework.common.biz.system.dict;

import com.mbp.eng.framework.common.biz.system.dict.dto.DictDataRespDTO;
import com.mbp.eng.framework.common.util.object.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典数据 API 实现类
 */
@Service
public class DictDataCommonApiImpl implements DictDataCommonApi {

    @Override
    public List<DictDataRespDTO> getDictDataList(String dictType) {
        List<DictDataRespDTO> list = new ArrayList<>();
        return BeanUtils.toBean(list, DictDataRespDTO.class);
    }

}
