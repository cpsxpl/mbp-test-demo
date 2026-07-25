package com.mbp.eng.framework.common.spark;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class SparkJobParam implements Serializable {
    private String serviceCode;
    private String serviceName;

    private AudienceParam audienceParam = new AudienceParam();
    private ActivityParam activityParam = new ActivityParam();
    private EsParam esParam = new EsParam();
    private JdbcParam jdbcParam = new JdbcParam();
    private JmqParam jmqParam = new JmqParam();
    private JfsParam jfsParam = new JfsParam();

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public AudienceParam getAudienceParam() {
        return audienceParam;
    }

    public ActivityParam getActivityParam() {
        return activityParam;
    }

    public EsParam getEsParam() {
        return esParam;
    }

    public JdbcParam getJdbcParam() {
        return jdbcParam;
    }

    public JmqParam getJmqParam() {
        return jmqParam;
    }

    public JfsParam getJfsParam() {
        return jfsParam;
    }

    @Override
    public String toString() {
        return "SparkJobParam{" +
                "audienceParam=" + audienceParam +
                "activityParam=" + activityParam +
                ", esParam=" + esParam +
                ", jdbcParam=" + jdbcParam +
                ", jmqParam=" + jmqParam +
                ", jfsParam=" + jfsParam +
                '}';
    }

    public class AudienceParam implements Serializable {
        private String sparkSQL;
        private String createdBy;
        private String audienceId;
        private String uuid;
        private String pin;
        private int id;
        private String hdfsPath;
        private Integer audienceLimitType;
        private String audienceSizeCut;

        //private String reachId;
        private String srcJfsUrl;
        private String type;
        private String encrypt;
        private String table2Jfs;
        private String audienceMaxNum;
        private Boolean updateFlag;
        private String jfsObjectKey;

        public String getSparkSQL() {
            return sparkSQL;
        }

        public void setSparkSQL(String sparkSQL) {
            this.sparkSQL = sparkSQL;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getAudienceId() {
            return audienceId;
        }

        public void setAudienceId(String audienceId) {
            this.audienceId = audienceId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHdfsPath() {
            return hdfsPath;
        }

        public void setHdfsPath(String hdfsPath) {
            this.hdfsPath = hdfsPath;
        }

        public Integer getAudienceLimitType() {
            return audienceLimitType;
        }

        public void setAudienceLimitType(Integer audienceLimitType) {
            this.audienceLimitType = audienceLimitType;
        }

        public String getAudienceSizeCut() {
            return audienceSizeCut;
        }

        public void setAudienceSizeCut(String audienceSizeCut) {
            this.audienceSizeCut = audienceSizeCut;
        }

        /*public String getReachId() {
            return reachId;
        }

        public void setReachId(String reachId) {
            this.reachId = reachId;
        }*/

        public String getSrcJfsUrl() {
            return srcJfsUrl;
        }

        public void setSrcJfsUrl(String srcJfsUrl) {
            this.srcJfsUrl = srcJfsUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEncrypt() {
            return encrypt;
        }

        public void setEncrypt(String encrypt) {
            this.encrypt = encrypt;
        }

        public String getTable2Jfs() {
            return table2Jfs;
        }

        public void setTable2Jfs(String table2Jfs) {
            this.table2Jfs = table2Jfs;
        }

        public String getAudienceMaxNum() {
            return audienceMaxNum;
        }

        public void setAudienceMaxNum(String audienceMaxNum) {
            this.audienceMaxNum = audienceMaxNum;
        }

        public Boolean getUpdateFlag() {
            return updateFlag;
        }

        public void setUpdateFlag(Boolean updateFlag) {
            this.updateFlag = updateFlag;
        }

        public String getJfsObjectKey() {
            return jfsObjectKey;
        }

        public void setJfsObjectKey(String jfsObjectKey) {
            this.jfsObjectKey = jfsObjectKey;
        }

        @Override
        public String toString() {
            return "AudienceParam{" +
                    "sparkSQL='" + sparkSQL + '\'' +
                    ", createdBy='" + createdBy + '\'' +
                    ", audienceId='" + audienceId + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", pin='" + pin + '\'' +
                    ", id=" + id +
                    ", hdfsPath='" + hdfsPath + '\'' +
                    ", audienceLimitType=" + audienceLimitType +
                    ", audienceSizeCut='" + audienceSizeCut + '\'' +
                    //", reachId='" + reachId + '\'' +
                    ", srcJfsUrl='" + srcJfsUrl + '\'' +
                    ", type='" + type + '\'' +
                    ", encrypt='" + encrypt + '\'' +
                    ", table2Jfs='" + table2Jfs + '\'' +
                    ", audienceMaxNum='" + audienceMaxNum + '\'' +
                    ", updateFlag=" + updateFlag +
                    ", jfsObjectKey='" + jfsObjectKey + '\'' +
                    '}';
        }
    }

    public class EsParam implements Serializable {
        private String esPort;
        private String esNodes;
        private String esUser;
        private String esPass;
        private String profileIndex;

        public String getEsPort() {
            return esPort;
        }

        public void setEsPort(String esPort) {
            this.esPort = esPort;
        }

        public String getEsNodes() {
            return esNodes;
        }

        public void setEsNodes(String esNodes) {
            this.esNodes = esNodes;
        }

        public String getEsUser() {
            return esUser;
        }

        public void setEsUser(String esUser) {
            this.esUser = esUser;
        }

        public String getEsPass() {
            return esPass;
        }

        public void setEsPass(String esPass) {
            this.esPass = esPass;
        }

        public String getProfileIndex() {
            return profileIndex;
        }

        public void setProfileIndex(String profileIndex) {
            this.profileIndex = profileIndex;
        }

        @Override
        public String toString() {
            return "EsParam{" +
                    "esPort='" + esPort + '\'' +
                    ", esNodes='" + esNodes + '\'' +
                    ", esUser='" + esUser + '\'' +
                    ", esPass='******'" +
                    ", profileIndex='" + profileIndex + '\'' +
                    '}';
        }
    }

    public class JfsParam implements Serializable {
        private String bucket;
        private String uploadJssAccessKey;
        private String uploadJssSecretKey;
        private String uploadJssEndpoint;

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getUploadJssAccessKey() {
            return uploadJssAccessKey;
        }

        public void setUploadJssAccessKey(String uploadJssAccessKey) {
            this.uploadJssAccessKey = uploadJssAccessKey;
        }

        public String getUploadJssSecretKey() {
            return uploadJssSecretKey;
        }

        public void setUploadJssSecretKey(String uploadJssSecretKey) {
            this.uploadJssSecretKey = uploadJssSecretKey;
        }

        public String getUploadJssEndpoint() {
            return uploadJssEndpoint;
        }

        public void setUploadJssEndpoint(String uploadJssEndpoint) {
            this.uploadJssEndpoint = uploadJssEndpoint;
        }

        @Override
        public String toString() {
            return "JfsParam{" +
                    "bucket='" + bucket + '\'' +
                    ", uploadJssAccessKey='******'" +
                    ", uploadJssSecretKey='******'" +
                    ", uploadJssEndpoint='" + uploadJssEndpoint + '\'' +
                    '}';
        }
    }

    public class JdbcParam implements Serializable {
        private String jdbcDriver;
        private String jdbcUrl;
        private String jdbcUser;
        private String jdbcPassword;

        public String getJdbcDriver() {
            return jdbcDriver;
        }

        public void setJdbcDriver(String jdbcDriver) {
            this.jdbcDriver = jdbcDriver;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getJdbcUser() {
            return jdbcUser;
        }

        public void setJdbcUser(String jdbcUser) {
            this.jdbcUser = jdbcUser;
        }

        public String getJdbcPassword() {
            return jdbcPassword;
        }

        public void setJdbcPassword(String jdbcPassword) {
            this.jdbcPassword = jdbcPassword;
        }

        @Override
        public String toString() {
            return "JdbcParam{" +
                    "jdbcDriver='" + jdbcDriver + '\'' +
                    ", jdbcUrl='" + jdbcUrl + '\'' +
                    ", jdbcUser='" + jdbcUser + '\'' +
                    ", jdbcPassword='******'" +
                    '}';
        }
    }

    public class JmqParam implements Serializable {
        private String jmqAddress;
        private String jmqUser;
        private String jmqPassword;
        private String app;
        private String topic;
        private String pullTimeout;
        private String jmqEpoll;

        public String getJmqAddress() {
            return jmqAddress;
        }

        public void setJmqAddress(String jmqAddress) {
            this.jmqAddress = jmqAddress;
        }

        public String getJmqUser() {
            return jmqUser;
        }

        public void setJmqUser(String jmqUser) {
            this.jmqUser = jmqUser;
        }

        public String getJmqPassword() {
            return jmqPassword;
        }

        public void setJmqPassword(String jmqPassword) {
            this.jmqPassword = jmqPassword;
        }

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getPullTimeout() {
            return pullTimeout;
        }

        public void setPullTimeout(String pullTimeout) {
            this.pullTimeout = pullTimeout;
        }

        public String getJmqEpoll() {
            return jmqEpoll;
        }

        public void setJmqEpoll(String jmqEpoll) {
            this.jmqEpoll = jmqEpoll;
        }

        @Override
        public String toString() {
            return "JmqParam{" +
                    "jmqAddress='" + jmqAddress + '\'' +
                    ", jmqUser='" + jmqUser + '\'' +
                    ", jmqPassword='******'" +
                    ", app='" + app + '\'' +
                    ", topic='" + topic + '\'' +
                    ", pullTimeout='" + pullTimeout + '\'' +
                    ", jmqEpoll='" + jmqEpoll + '\'' +
                    '}';
        }
    }

    public class ActivityParam implements Serializable {
        private Long id;

        private List<Long> taskId;

        private String name;

        private String comment;

        private Integer brandCode;

        private String createdBy;

        private Long audienceSize;

        private Timestamp createTime;

        private Integer status;

        private Integer isValid;

        private Timestamp createdTime;

        private Timestamp finishedTime;

        private Timestamp lastUpdateTime;

        private String errMsg;

        private Integer version;

        private Integer reportStartType;

        private Timestamp reportStartTime;

        private String packageUrl;

        private String fileKey;

        private String fileMd5;

        private Timestamp taskFinishedTime;

        private Integer assetStatus;

        private Integer behaviorStatus;

        private String track;

        private String hiveTableName;

        private String activityMaxNum;

        private String esTableName;

        private String modelId;

        private String srcJfsUrl;

        private Long activityId;

        private String startDate;

        private String endDate;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public List<Long> getTaskId() {
            return taskId;
        }

        public void setTaskId(List<Long> taskId) {
            this.taskId = taskId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getBrandCode() {
            return brandCode;
        }

        public void setBrandCode(Integer brandCode) {
            this.brandCode = brandCode;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public Long getAudienceSize() {
            return audienceSize;
        }

        public void setAudienceSize(Long audienceSize) {
            this.audienceSize = audienceSize;
        }

        public Timestamp getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Timestamp createTime) {
            this.createTime = createTime;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getIsValid() {
            return isValid;
        }

        public void setIsValid(Integer isValid) {
            this.isValid = isValid;
        }

        public Timestamp getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Timestamp createdTime) {
            this.createdTime = createdTime;
        }

        public Timestamp getFinishedTime() {
            return finishedTime;
        }

        public void setFinishedTime(Timestamp finishedTime) {
            this.finishedTime = finishedTime;
        }

        public Timestamp getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(Timestamp lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Integer getReportStartType() {
            return reportStartType;
        }

        public void setReportStartType(Integer reportStartType) {
            this.reportStartType = reportStartType;
        }

        public Timestamp getReportStartTime() {
            return reportStartTime;
        }

        public void setReportStartTime(Timestamp reportStartTime) {
            this.reportStartTime = reportStartTime;
        }

        public String getPackageUrl() {
            return packageUrl;
        }

        public void setPackageUrl(String packageUrl) {
            this.packageUrl = packageUrl;
        }

        public String getFileKey() {
            return fileKey;
        }

        public void setFileKey(String fileKey) {
            this.fileKey = fileKey;
        }

        public String getFileMd5() {
            return fileMd5;
        }

        public void setFileMd5(String fileMd5) {
            this.fileMd5 = fileMd5;
        }

        public Timestamp getTaskFinishedTime() {
            return taskFinishedTime;
        }

        public void setTaskFinishedTime(Timestamp taskFinishedTime) {
            this.taskFinishedTime = taskFinishedTime;
        }

        public Integer getAssetStatus() {
            return assetStatus;
        }

        public void setAssetStatus(Integer assetStatus) {
            this.assetStatus = assetStatus;
        }

        public Integer getBehaviorStatus() {
            return behaviorStatus;
        }

        public void setBehaviorStatus(Integer behaviorStatus) {
            this.behaviorStatus = behaviorStatus;
        }

        public String getTrack() {
            return track;
        }

        public void setTrack(String track) {
            this.track = track;
        }

        public String getHiveTableName() {
            return hiveTableName;
        }

        public void setHiveTableName(String hiveTableName) {
            this.hiveTableName = hiveTableName;
        }

        public String getActivityMaxNum() {
            return activityMaxNum;
        }

        public void setActivityMaxNum(String activityMaxNum) {
            this.activityMaxNum = activityMaxNum;
        }

        public String getEsTableName() {
            return esTableName;
        }

        public void setEsTableName(String esTableName) {
            this.esTableName = esTableName;
        }

        public String getModelId() {
            return modelId;
        }

        public void setModelId(String modelId) {
            this.modelId = modelId;
        }

        public String getSrcJfsUrl() {
            return srcJfsUrl;
        }

        public void setSrcJfsUrl(String srcJfsUrl) {
            this.srcJfsUrl = srcJfsUrl;
        }

        public Long getActivityId() {
            return activityId;
        }

        public void setActivityId(Long activityId) {
            this.activityId = activityId;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        @Override
        public String toString() {
            return "ActivityParam{" +
                    "id=" + id +
                    ", taskId=" + taskId +
                    ", name='" + name + '\'' +
                    ", comment='" + comment + '\'' +
                    ", brandCode=" + brandCode +
                    ", createdBy='" + createdBy + '\'' +
                    ", audienceSize=" + audienceSize +
                    ", createTime=" + createTime +
                    ", status=" + status +
                    ", isValid=" + isValid +
                    ", createdTime=" + createdTime +
                    ", finishedTime=" + finishedTime +
                    ", lastUpdateTime=" + lastUpdateTime +
                    ", errMsg='" + errMsg + '\'' +
                    ", version=" + version +
                    ", reportStartType=" + reportStartType +
                    ", reportStartTime=" + reportStartTime +
                    ", packageUrl='" + packageUrl + '\'' +
                    ", fileKey='" + fileKey + '\'' +
                    ", fileMd5='" + fileMd5 + '\'' +
                    ", taskFinishedTime=" + taskFinishedTime +
                    ", assetStatus=" + assetStatus +
                    ", behaviorStatus=" + behaviorStatus +
                    ", track='" + track + '\'' +
                    ", hiveTableName='" + hiveTableName + '\'' +
                    ", activityMaxNum='" + activityMaxNum + '\'' +
                    ", esTableName='" + esTableName + '\'' +
                    ", modelId='" + modelId + '\'' +
                    ", srcJfsUrl='" + srcJfsUrl + '\'' +
                    ", activityId='" + activityId + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    '}';
        }
    }
}
