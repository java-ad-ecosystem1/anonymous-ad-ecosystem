package com.video_sys.param;

import com.video_sys.common.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频查询参数
 */
public class VideoParam extends PageParam {

    private Long id;
    /**
     * 视频类型
     */
    private String videoType;

    /**
     * 视频名称（模糊查询）
     */
    private String videoName;

    /**
     * 视频发布者（模糊查询）
     */
    private String videoPublisher;

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
}

