package com.mbp.eng.framework.auth.service.open;

import com.mbp.eng.framework.common.auth.AuthResponse;

public interface AccountAuthorizeService {
    AuthResponse<String> encryptVirtualPin(String clearVpin);

    AuthResponse<String> decryptVirtualPin(String cipherVpin);
}
