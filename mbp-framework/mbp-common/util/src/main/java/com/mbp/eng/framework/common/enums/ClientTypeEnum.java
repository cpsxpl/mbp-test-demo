package com.mbp.eng.framework.common.enums;

import com.google.common.base.Enums;

public enum ClientTypeEnum {
    inke("0", "logs", "inke_app"),
    gaia("1", "logs", "gaia_app"),
    queen("2", "bujiu", "bj_app"),
    cj("3", "logs", "cj_app"),
    ms("4", "logs", "ms_app"),
    sm("5", "sm", "sm_app"),
    lingxi("6", "logs", "yingtao_app"),
    qianmei("7", "logs", "qianmei_app"),
    ss("8", "logs", "ss_app"),
    gmu("9", "logs", "jimu_app"),
    haokan("10", "logs", "haokan_app"),
    vins("11", "logs", "vins_app"),
    chatright("12", "logs", "chatright_app"),
    eos("13", "logs", "eos_app"),
    buylive("14", "logs", "buylive_app"),
    softvoice("15", "logs", "softvoice_app"),
    lovemeet("16", "logs", "lovemeet_app"),
    socialgame("17", "logs", "socialgame_app"),
    localmeet("18", "logs", "localmeet_app"),
    deep("19", "logs", "deep_app"),
    someone("20", "logs", "someone_app"),
    shayla("21", "logs", "shayla_app"),
    yuyu("22", "logs", "yuyu_app"),
    honey("23", "logs", "honey_app"),
    venus("24", "logs", "venus_app"),
    findlove("25", "logs", "findlove_app"),
    seeklove("26", "logs", "seeklove_app"),
    roman("27", "logs", "roman_app"),
    dialogue("28", "logs", "dialogue_app"),
    athena("29", "logs", "athena_app"),
    lubanaaa("30", "logs", "lubanaaa_app"),
    qianxi("31", "logs", "qianxi_app"),
    facetalk("32", "logs", "facetalk_app"),
    zaiyiqi("33", "logs", "zaiyiqi_app"),
    lovepig("34", "logs", "lovepig_app"),
    yochat("35", "logs", "yochat_app"),
    mido("36", "logs", "mido_app"),
    vivahuo("37", "logs", "vivahuo_app"),
    camp("38", "logs", "camp_app"),
    xingyuan("39", "logs", "xingyuan_app"),
    webuy("40", "logs", "webuy_app"),
    wsds("41", "logs", "wsds_app"),
    nopstor("42", "logs", "nopstor_app"),
    zhixing("43", "logs", "zhixing_app"),
    yeet("44", "logs", "yeet_app"),
    xwifi("45", "logs", "xwifi_app"),
    wfgj("46", "logs", "wfgj_app"),
    blackcat("47", "logs", "blackcat_app"),
    tianmi("48", "logs", "tianmi_app"),
    dlabroad("49", "logs", "dlabroad_app"),
    silian("50", " logs", "silian_app"),
    archapp("51", " logs", "archapp_app"),
    spacememorial("52", " logs", "spacememorial_app"),
    pokershark("53", " logs", "pokershark_app"),
    ssplanet("54", " logs", "ssplanet_app"),
    xiaojinbu("55", " logs", "xiaojinbu_app"),
    yap("56", " logs", "yap_app"),
    phxyuyin("57", " logs", "phxyuyin_app"),
    multimedia("58", " logs", "multimedia_app"),
    yesok("59", " logs", "yesok_app"),
    dock("60", " logs", "dock_app"),
    eryiyi("61", " logs", "eryiyi_app"),
    owonovel("62", " logs", "owonovel_app"),
    wifiaid("63", " logs", "wifiaid_app"),
    chatya("64", " logs", "chatya_app"),
    blindbox("65", " logs", "blindbox_app"),
    liaoyue("66", " logs", "liaoyue_app"),
    kisses("67", " logs", "kisses_app"),
    pandora("68", " logs", "pandora_app"),
    sweetheart("69", " logs", "sweetheart_app"),
    muaah("70", " logs", "muaah_app"),
    abbox("71", " logs", "abbox_app"),
    funbox("72", " logs", "funbox_app"),
    quweimanghe("73", " logs", "quweimanghe_app"),
    fubox("74", " logs", "fubox_app"),
    luckybox("75", " logs", "luckybox_app"),
    accompany("76", " logs", "accompany_app"),
    hashbox("77", " logs", "hashbox_app"),
    dataflow("78", " logs", "dataflow_app"),
    texaslivekey("79", " logs", "texaslivekey_app"),
    readom("80", " logs", "readom_app"),
    scg("81", " logs", "scg_app");

    private String code;
    private String dbName;
    private String tableName;

    ClientTypeEnum(String code, String dbName, String tableName) {
        this.code = code;
        this.dbName = dbName;
        this.tableName = tableName;
    }

    /**
     * 按照serviceCode获得枚举值
     */
    public static ClientTypeEnum valueOf(Integer serviceCode) {
        if (String.valueOf(serviceCode) != null) {
            for (ClientTypeEnum clientTypeEnum : ClientTypeEnum.values()) {
                if (clientTypeEnum.getCode() == String.valueOf(serviceCode)) {
                    return clientTypeEnum;
                }
            }
        }
        return null;
    }

    /**
     * 判断枚举类是否存在传入的枚举值
     */
    public static ClientTypeEnum getIfPresent(String name) {
        return Enums.getIfPresent(ClientTypeEnum.class, name).orNull();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
