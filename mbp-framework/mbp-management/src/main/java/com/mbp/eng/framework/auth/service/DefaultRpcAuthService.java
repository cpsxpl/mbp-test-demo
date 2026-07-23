package com.mbp.eng.framework.auth.service;

import com.mbp.eng.framework.common.auth.AuthResponse;
import com.mbp.eng.framework.common.exception.auth.AuthException;
import com.mbp.eng.framework.common.exception.ErrorStatus;
import com.mbp.eng.framework.common.exception.MbpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.PermissionDeniedDataAccessException;

import java.util.HashMap;
import java.util.Map;

public class DefaultRpcAuthService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRpcAuthService.class);

    public DefaultRpcAuthService() {
    }

    public static void setRpcAttachments() {
        Map<String, Object> map = new HashMap();
        if (MDC.getCopyOfContextMap() != null) {
            map.putAll(MDC.getCopyOfContextMap());
            //TODO
        }

        //TODO
    }

    public static void setRpcAttachments(Map<String, String> map) {
        Map<String, Object> mdcMap = new HashMap();
        if (map != null) {
            mdcMap.putAll(map);
            //TODO
        }

        //TODO
    }

    public static void setRpcAttachment(String name, String value) {
        //TODO
    }

    public abstract static class RpcAuthServiceTemplate<T> {
        public RpcAuthServiceTemplate() {
        }

        public abstract AuthResponse<T> callback() throws Exception;

        public AuthResponse<T> execute() {
            DefaultRpcAuthService.setRpcAttachments();

            try {
                return this.callback();
            } catch (MbpException mbpException) {
                throw mbpException;
            } catch (Exception exception) {
                DefaultRpcAuthService.logger.error("Failed to call Auth API.", exception);
                String message = exception.getMessage();
                int errorCode = ErrorStatus.SERVER_INTERNAL_ERROR;
                if (message != null) {
                    if (exception instanceof AuthException) {
                        errorCode = Integer.parseInt(message.substring(message.indexOf("(") + 1, message.indexOf(")")));
                    } else if (exception instanceof PermissionDeniedDataAccessException) {
                        //屏蔽
                        //errorCode = ErrorStatus.AUTH_DB_READONLY;
                    } else {
                        if (message.indexOf("MbpException: ") > -1) {
                            message = message.substring(message.indexOf("DatamillException: ") + 19);
                        } else if (message.indexOf("AuthException: ") > -1) {
                            message = message.substring(message.indexOf("AuthException: ") + 15);
                            errorCode = Integer.parseInt(message.substring(message.indexOf("(") + 1, message.indexOf(")")));
                        } else if (message.indexOf("PermissionDeniedDataAccessException: ") > -1) {
                            message = message.substring(message.indexOf("PermissionDeniedDataAccessException: ") + 37);
                            //屏蔽
                            //errorCode = ErrorStatus.AUTH_DB_READONLY;
                        }
                        if (message.indexOf("\n") > -1) {
                            message = message.substring(0, message.indexOf("\n"));
                        }
                    }
                }
                throw new MbpException(errorCode, message, new Object[0]);
            }
        }
    }
}
