package com.mbp.eng.framework.auth.service.impl;

import com.mbp.eng.framework.auth.service.open.AccountAuthorizeService;
import com.mbp.eng.framework.common.auth.AuthResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * accountAuthorizeService 实现类
 */
@Service("accountAuthorizeService")
public class AccountAuthorizeServiceImpl implements AccountAuthorizeService {
    @Override
    public AuthResponse<String> encryptVirtualPin(String clearVpin) {
        AuthResponse authResponse = new AuthResponse();
        List list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("pin", "admin");
        list.add(map);
        authResponse.setResult(list);
        return authResponse;
    }

    @Override
    public AuthResponse<String> decryptVirtualPin(String cipherVpin) {
        AuthResponse authResponse = new AuthResponse();
        Map<String, Object> map = new HashMap<>();
        map.put("pin", "admin");
        authResponse.setResult(map);
        return authResponse;
    }
}
