package com.advertisement_sys.entity;

import java.math.BigInteger;
import java.util.Date;

/**
 * 广告实体类
 */
public class Advertisement {
    /**
     * 广告ID
     */
    private BigInteger id;

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
     * 是否投放(true-是,false-否)
     */
    private Boolean putFlag;

    /**
     * 广澳内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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
}

