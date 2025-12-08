package com.advertisement_sys.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告返回模型类
 */
public class AdvertisementModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 广告ID
     */
    private String id;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 广告类型(text-文本,video-视频,picture-图片)
     */
    private String advertisementType;

    /**
     * 投放类型(mobile-手机,clothes-服装,photography-摄影,computer-电脑)
     */
    private String putType;

    /**
     * 投放标志(true-是,false-否)
     */
    private Boolean putFlag;

    /**
     * 广告内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    public AdvertisementModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdvertisementType() {
        return advertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        this.advertisementType = advertisementType;
    }

    public String getPutType() {
        return putType;
    }

    public void setPutType(String putType) {
        this.putType = putType;
    }

    public Boolean getPutFlag() {
        return putFlag;
    }

    public void setPutFlag(Boolean putFlag) {
        this.putFlag = putFlag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AdvertisementModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", advertisementType='" + advertisementType + '\'' +
                ", putType='" + putType + '\'' +
                ", putFlag=" + putFlag +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

