package com.mbp.eng.framework.jdbc.sql.exception;

/**
 * {系统（1位）} + {应用(2位)} + {status(3位)}
 * 数据安全系统： 1
 * 入驻系统： 2
 * 通用： 00
 * 业务： 01
 * 服务： 02
 */
public class ErrorStatus {
    private ErrorStatus() {
    }

    /**
     * 常用异常,0~100
     * 权限相关,101~200
     * 201~300: 预留该段错误码是为了给前端的提示信息统一从后端返回的error message中获取
     */
    public static final Integer SUCCESS = 0;
    public static final Integer SERVER_INTERNAL_ERROR = 100001;
    public static final Integer USER_NOT_EXIST = 100002;
    public static final Integer BAD_REQUEST = 100003;
    public static final Integer UNAUTHORIZED = 100004;
    public static final Integer TOKEN_EXPIRED = 100005;
    public static final Integer FILE_TYPE_ERROR = 100006;
    public static final Integer FILE_EXCEED_LIMIT = 100007;
    public static final Integer EMPTY_FILE = 100008;
    public static final Integer FILE_ENCODING_ERROR = 100009;
    public static final Integer FILE_ROW_SIZE_EXCEED_LIMIT = 100010;
    public static final Integer AUTH_ERP_PIN_NOT_AUTH = 100101;
    public static final Integer AUTH_ERP_NOT_EXISTS = 100102;
    public static final Integer AUTH_PIN_NOT_EXISTS = 100103;
    //201~300: 预留该段错误码是为了给前端的提示信息统一从后端返回的error message中获取
    public static final Integer DEMOR_ROLE_NOT_SUPPORT = 100201;

    /**
     * dataService 操作异常,100~200
     * 200~300: pipeline-service相关异常错误码定义
     */
    public static final Integer TRANS_DATA_ERROR = 102101;
    public static final Integer AUDIENCE_GEN_SQL_INVALID = 102102;
    public static final Integer AUDIENCE_GEN_DB_ERROR = 102103;
    // pipeline-service 相关异常码
    public static final Integer APP_PIPELINE_SCHEDULER_INTERNAL_ERROR = 102201;
    public static final Integer APP_PIPELINE_SCHEDULER_ARGUMENT_ERROR = 102202;

    /**
     * 数据安全业务应用异常：001~999, 其中：
     * 001～100是schema相关异常
     * 101～200是aaa相关异常
     * 201～300是bbb相关异常
     * 301～400是ccc的相关异常
     * 401～500是ddd的相关异常
     */
    public static final Integer APP_SCHEMA_NOT_FOUND = 101001;
    public static final Integer APP_SCHEMA_INVALID = 101002;
    public static final Integer APP_SCHEMA_UNSUPPORT_DATASOURCE = 101003;
    public static final Integer APP_POST_DS_NOT_SUPPORT = 101004;
    public static final Integer APP_POST_UNKNOWN_TABLE = 101005;
    public static final Integer APP_POST_COLUMN_NOT_FOUND = 101006;
    public static final Integer APP_POST_UNEXPECT_COLUMN_TYPE = 101007;
    public static final Integer APP_POST_COLUMN_SIZE_NOT_SAME = 101008;
    public static final Integer APP_TRANSFORM_READ_VALUE_ERROR = 101009;
    public static final Integer APP_DATASOURCE_EMPTY = 101010;
    public static final Integer APP_DATE_OUT_OF_BOUNDS = 101011;
    public static final Integer APP_SCHEMA_DATA_INVALID = 101012;
    public static final Integer APP_POST_ZERO_DENOMINATOR = 101013;

    public static final Integer APP_AUDIENCE_NOT_FOUND = 101101;
    public static final Integer APP_AUDIENCE_ALREADY_EXIST = 101102;
    public static final Integer APP_AUDIENCE_NO_AUTHORIZATION = 101103;
    public static final Integer APP_AUDIENCE_UNSUPPORT_DEF_TYPE = 101104;
    public static final Integer APP_AUDIENCE_DEF_BUILD_FAILED = 101105;
    public static final Integer APP_AUDIENCE_UNKNOWN_AUDIENCE_TYPE = 101106;
    public static final Integer APP_AUDIENCE_REACH_NOT_FOUND = 101107;
    public static final Integer APP_AUDIENCE_REACH_DMP_FAILURE = 101108;
    public static final Integer APP_AUDIENCE_IS_REACHING = 101109;
    public static final Integer APP_AUDIENCE_UNKNOWN_DEFINITION = 101110;
    public static final Integer APP_AUDIENCE_DEF_VIEW_SERIALIZE_FAILURE = 101111;
    public static final Integer APP_AUDIENCE_DEF_VIEW_DESERIALIZE_FAILURE = 101112;
    public static final Integer APP_AUDIENCE_DEF_INVALID = 101113;
    public static final Integer APP_AUDIENCE_DEF_CACHE_NOT_FOUND = 101114;
    public static final Integer APP_AUDIENCE_GEN_EMPTY = 101115;
    public static final Integer APP_AUDIENCE_GEN_FAILED = 101116;
    public static final Integer APP_AUDIENCE_NOT_SUCCESS = 101117;
    public static final Integer APP_AUDIENCE_IS_GENERATING = 101118;
    public static final Integer APP_AUDIENCE_DEF_UNION_EMPTY = 101119;
    public static final Integer APP_AUDIENCE_DEF_INTERSECTION_ERROR = 101120;
    public static final Integer APP_AUDIENCE_DEF_DIFF_ERROR = 101121;
    public static final Integer APP_MA_AUDIENCE_GENERATION_EXCEPTION = 101122;
    public static final Integer APP_MA_AUDIENCE_STOP_TRACKING_EXCEPTION = 101123;
    public static final Integer APP_MA_AUDIENCE_REMOVE_EXCEPTION = 101124;
    public static final Integer APP_AUDIENCE_UPLOAD_EXCEPTION = 101125;
    public static final Integer APP_AUDIENCE_REACH_BAD_REQUEST = 101126;
    public static final Integer APP_AUDIENCE_REACH_JT_UNAUTHORIZED = 101127;
    public static final Integer APP_AUDIENCE_REACH_JT_INSUFFICIENT_QUOTA = 101128;
    public static final Integer APP_AUDIENCE_REACH_DUPLICATE_AUDIENCE = 101129;
    public static final Integer APP_AUDIENCE_REACH_DUPLICATE_REQUEST = 101130;
    public static final Integer APP_AUDIENCE_IMPORT_FAILED = 101131;
    public static final Integer APP_AUDIENCE_REACH_AUDIENCE_GEN_FAILED = 101132;
    public static final Integer APP_AUDIENCE_DEF_NOT_FOUND = 101133;
    public static final Integer APP_AUDIENCE_UPDATE_QUOTA_EXCEED_LIMIT = 101134;
    public static final Integer APP_AUDIENCE_MODIFY_NOT_SUPPORT = 101135;
    public static final Integer APP_AUDIENCE_DEF_SUB_AUDIENCE_DELETE = 101136;
    public static final Integer APP_AUDIENCE_REACH_INPROCCESS = 101137;
    public static final Integer APP_AUDIENCE_REACH_JZT_UNAUTHORIZED = 101138;
    public static final Integer APP_AUDIENCE_REACH_JZT_ALREADY_USED = 101139;
    public static final Integer APP_AUDIENCE_RFM_FAILED = 101140;
    public static final Integer APP_CREATE_RFM_AUDIENCE_DETAIL_DATA_PATH_NOT_FOUND = 101141;

    // 201～300 bbb 相关异常错误码
    public static final Integer APP_ACTIVITY_NOT_FOUND = 101201;
    public static final Integer APP_ACTIVITY_ALREADY_EXIST = 101202;
    public static final Integer APP_ACTIVITY_CREATE_FAILED = 101203;
    public static final Integer APP_ACTIVITY_AUDIENCE_CREATE_FAILED = 101204;
    public static final Integer APP_ACTIVITY_STOP_TRACKING_FAILED = 101205;
    public static final Integer APP_ACTIVITY_DELETE_FAILED = 101206;
    public static final Integer APP_ACTIVITY_UNKNOWN_SRCTYPE = 101207;
    public static final Integer APP_GET_FASTCAR_UNIT_FAILED = 101208;
    public static final Integer APP_GET_FASTCAR_CAMPAIGN_FAILED = 101209;
    public static final Integer APP_GET_BRANDEFFECT_UNIT_FAILED = 101210;
    public static final Integer APP_GET_BRANDEFFECT_CAMPAIGN_FAILED = 101211;
    //301～400 ccc 相关异常错误码
    public static final Integer APP_GET_SKUINFO_EMPTY = 101301;
    // 401～500 ddd 相关异常错误码
    public static final Integer APP_ANALYSE_NOT_FOUND = 101401;
    public static final Integer APP_ANALYSE_AUDIENCE_SIZE_BEYOND = 101402;
    public static final Integer APP_ANALYSE_COUNT_SIZE_BEYOND = 101403;
    public static final Integer APP_ANALYSE_AUDIENCE_TYPE_ERROE = 101404;
    public static final Integer APP_ANALYSE_AUDIENCE_NOT_FOUND = 101405;
    public static final Integer APP_ANALYSE_LAYOUT_FEATURE_NOT_FOUND = 101406;
    public static final Integer APP_ANALYSE_CHAIN_FEATURE_NOT_FOUND = 101407;
    public static final Integer APP_ANALYSE_PURCHASE_FEATURE_NOT_FOUND = 101408;
    public static final Integer APP_ANALYSE_AUDIENCE_STATUS_ERROE = 101409;
    public static final Integer APP_ANALYSE_SEARCH_FEATURE_NOT_FOUND = 101410;

    /**
     * {系统（1位）} + {应用(2位)} + {status(3位)}
     * 数据安全系统： 1
     * 入驻系统： 2
     * 通用： 00
     * 业务： 01
     * 服务： 02
     */
    //系统内部错误
    public static final Integer REG_VENDOR_NOT_FOUND = 200001;
    public static final Integer REG_BRAND_INC_NOT_FOUND = 200002;
    public static final Integer REG_POP_NOT_FOUND = 200003;
    public static final Integer REG_APPLY_NOT_FOUND = 200004;
    public static final Integer REG_SKU_NOT_FOUND = 200005;
    public static final Integer REG_WRONG_CHECK_OPERATE = 200006;
    public static final Integer REG_BRAND_INFO_NOT_FOUND = 200007;
    public static final Integer REG_DB_DELETE_FAILED = 200008;

    //定义用户请求错误
    public static final Integer REG_INVALID_REQUEST = 201001;
    public static final Integer REG_APPLY_BRAND_ADD_REPEAT = 201002;
    public static final Integer REG_APPLY_BRAND_POP_EMPTY = 201003;
    public static final Integer REG_APPLY_DELETE_CHECKED_POP_APPLY = 201004;
    public static final Integer REG_REQUEST_PARAM_INVALID = 201005;
    public static final Integer REG_APPLY_VENDOR_ADD_REPEAT = 201006;
    public static final Integer REG_APPLY_POP_ADD_REPEAT = 201007;
    public static final Integer REG_APPLY_VENDOR_SUBMIT_ILLEGAL_ROLE = 201008;
    public static final Integer REG_APPLY_VCSUPPIER_CHOOSE_DATAMILL = 201009;
    public static final Integer REG_APPLY_USER_UNLOGIN = 201010;
    public static final Integer REG_APPLY_USER_NO_UNSUBMIT_STATUS = 201011;
    public static final Integer REG_APPLY_STATUS_ADD_REPEAT = 201012;
    public static final Integer REG_APPLY_NOT_UPDATE_VENDOR_INFO_ = 201013;

    //对接精准通的返回错误  错误码定义范围 202+{0}+{2位}
    public static final Integer REG_PIN_NOT_REGIST = 202001;
    public static final Integer REG_UPLOAD_FILE_FAILED = 202002;

    //对接纵横返回错误  错误码定义范围 202+{1}+{2位}
    public static final Integer REG_BRAND_NOT_BRANDCODE = 202101;
    public static final Integer REG_NOT_USER_BY_PIN = 202102;
    public static final Integer REG_NOT_FOUND_USER_BASIC_INFO = 202103;

    //入驻系统  错误码定义范围 202+{2}+{2位}
    public static final Integer REG_IDENTIFY_ROLE_FAILURE = 202201;
    public static final Integer REG_VC_USER_SERVICE_FAILURE = 202202;
    public static final Integer REG_POP_USER_SERVICE_FAILURE = 202203;
    public static final Integer REG_BRAND_USER_SERVICE_FAILURE = 202204;
}
