package com.mbp.eng.framework.common.utils;

import com.mbp.eng.framework.common.Constants;

import java.util.Map;
import java.util.Map.Entry;

public class HelperUtils {
    /**
     * @param operateUser
     * @param operateDesc
     * @param operateParams
     * @return
     */
    public static String formatOperateInfo(String operateUser,
                                           String operateDesc, Map<String, Object> operateParams) {
        StringBuilder params = new StringBuilder();
        params.append("operateUser=" + operateUser).append(Constants.COMMA)
                .append("operateDesc=").append(operateDesc)
                .append(Constants.COMMA);
        for (Entry<String, Object> entry : operateParams.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            params.append(key).append(Constants.EQUAL).append(value)
                    .append(Constants.COMMA);
        }
        return params.toString();
    }
}
