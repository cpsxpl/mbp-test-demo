package com.mbp.eng.framework.common.util.file;

import java.io.Serializable;

public class FileInfo implements Serializable {
    private static final long serialVersionUID = -7201523302165673900L;

    /**
     * @naturalId 业务主键, 上传文件时可由具体业务自行指定, 如不指定则服务默认自动生成
     * @fileName 文件名称, 上传文件时该字段必须由用户指定
     * @fileSize 文件大小
     * @fileType 文件名称, 上传文件时该字段必须由用户指定
     * @fileUrl 文件路径, 上传文件后可直接获得
     * @fileMd5 文件MD5
     * @fileUsage 文件的用途, 建议填写与业务相关的固定的字符串
     * @fileDir 文件存放路径
     * @owner 文件所有者
     * @fileVersion 文件版本
     */

    private Long naturalId;

    private String fileName;

    private Long fileSize;

    private String fileType;

    private String fileUrl;

    private String fileMd5;

    private String fileUsage;

    private String fileDir;

    private String owner;

    private Integer fileVersion;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getNaturalId() {
        return naturalId;
    }

    public void setNaturalId(Long naturalId) {
        this.naturalId = naturalId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getFileUsage() {
        return fileUsage;
    }

    public void setFileUsage(String fileUsage) {
        this.fileUsage = fileUsage;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(Integer fileVersion) {
        this.fileVersion = fileVersion;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "naturalId=" + naturalId +
                ", fileName='" + fileName + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileMd5='" + fileMd5 + '\'' +
                ", fileUsage='" + fileUsage + '\'' +
                ", fileDir='" + fileDir + '\'' +
                ", owner='" + owner + '\'' +
                ", fileVersion='" + fileVersion + '\'' +
                '}';
    }
}
