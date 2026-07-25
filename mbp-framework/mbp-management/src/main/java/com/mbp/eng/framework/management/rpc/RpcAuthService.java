package com.mbp.eng.framework.management.rpc;

import com.mbp.eng.framework.auth.service.DefaultRpcAuthService;
import com.mbp.eng.framework.auth.service.open.AccountAuthorizeService;
import com.mbp.eng.framework.common.auth.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RpcAuthService {
    private static final Logger logger = LoggerFactory.getLogger(RpcAuthService.class);
    @Autowired
    protected AccountAuthorizeService accountAuthorizeService;

    public AuthResponse<String> encryptVirtualPin(String pin) {
        return new DefaultRpcAuthService.RpcAuthServiceTemplate<String>() {
            @Override
            public AuthResponse<String> callback() throws Exception {
                try {
                    return accountAuthorizeService.encryptVirtualPin(pin);
                } catch (Exception e) {
                    logger.error("Failed to call encryptVirtualPin, pin: " + pin, e);
                    throw e;
                }
            }
        }.execute();
    }

    public AuthResponse<String> decryptVirtualPin(String pin) {
        return new DefaultRpcAuthService.RpcAuthServiceTemplate<String>() {
            @Override
            public AuthResponse<String> callback() throws Exception {
                try {
                    return accountAuthorizeService.decryptVirtualPin(pin);
                } catch (Exception e) {
                    logger.error("Failed to call decryptVirtualPin, pin: " + pin, e);
                    throw e;
                }
            }
        }.execute();
    }
}
