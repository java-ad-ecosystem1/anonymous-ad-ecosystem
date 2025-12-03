package com.video_sys.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 视频模型类（用于接口返回）
 */
public class VideoModel implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 视频类型
     */
    private String videoType;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 视频发布者
     */
    private String videoPublisher;

    /**
     * 视频路径
     */
    private String videoUrl;

    /**
     * 视频时长
     */
    private String duration;

    /**
     * 视频描述
     */
    private String videoDescribe;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPublisher() {
        return videoPublisher;
    }

    public void setVideoPublisher(String videoPublisher) {
        this.videoPublisher = videoPublisher;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVideoDescribe() {
        return videoDescribe;
    }

    public void setVideoDescribe(String videoDescribe) {
        this.videoDescribe = videoDescribe;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

