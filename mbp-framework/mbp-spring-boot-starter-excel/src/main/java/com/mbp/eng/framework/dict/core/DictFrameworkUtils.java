package com.mbp.eng.framework.dict.core;

import cn.hutool.core.collection.CollUtil;
import com.mbp.eng.framework.common.biz.system.dict.DictDataCommonApi;
import com.mbp.eng.framework.common.util.cache.CacheUtils;
import com.mbp.eng.framework.common.biz.system.dict.dto.DictDataRespDTO;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mbp.eng.framework.common.util.date.DateUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static com.mbp.eng.framework.common.util.collection.CollectionUtils.convertList;

/**
 * 字典工具类
 */
@Slf4j
public class DictFrameworkUtils {
    private static Logger logger = LoggerFactory.getLogger(DictFrameworkUtils.class);
    private static DictDataCommonApi dictDataCommonApi;

    /**
     * 针对 dictType 的字段数据缓存
     */
    private static final LoadingCache<String, List<DictDataRespDTO>> GET_DICT_DATA_CACHE = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // 过期时间 1 分钟
            new CacheLoader<String, List<DictDataRespDTO>>() {

                @Override
                public List<DictDataRespDTO> load(String dictType) {
                    return dictDataCommonApi.getDictDataList(dictType);
                }

            });

    public static void init(DictDataCommonApi dictDataCommonApi) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("类=====:{} 方法=====:{} time: is {}", DictFrameworkUtils.class.getSimpleName(), methodName, DateUtil.getFormatTime(time));

        DictFrameworkUtils.dictDataCommonApi = dictDataCommonApi;
        log.info("[init][初始化 DictFrameworkUtils 成功]");
    }

    public static void clearCache() {
        GET_DICT_DATA_CACHE.invalidateAll();
    }

    @SneakyThrows
    public static String parseDictDataLabel(String dictType, Integer value) {
        if (value == null) {
            return null;
        }
        return parseDictDataLabel(dictType, String.valueOf(value));
    }

    @SneakyThrows
    public static String parseDictDataLabel(String dictType, String value) {
        List<DictDataRespDTO> dictDatas = GET_DICT_DATA_CACHE.get(dictType);
        DictDataRespDTO dictData = CollUtil.findOne(dictDatas, data -> Objects.equals(data.getValue(), value));
        return dictData != null ? dictData.getLabel(): null;
    }

    @SneakyThrows
    public static List<String> getDictDataLabelList(String dictType) {
        List<DictDataRespDTO> dictDatas = GET_DICT_DATA_CACHE.get(dictType);
        return convertList(dictDatas, DictDataRespDTO::getLabel);
    }

    @SneakyThrows
    public static String parseDictDataValue(String dictType, String label) {
        List<DictDataRespDTO> dictDatas = GET_DICT_DATA_CACHE.get(dictType);
        DictDataRespDTO dictData = CollUtil.findOne(dictDatas, data -> Objects.equals(data.getLabel(), label));
        return dictData!= null ? dictData.getValue(): null;
    }

    @SneakyThrows
    public static List<String> getDictDataValueList(String dictType) {
        List<DictDataRespDTO> dictDatas = GET_DICT_DATA_CACHE.get(dictType);
        return convertList(dictDatas, DictDataRespDTO::getValue);
    }
}
