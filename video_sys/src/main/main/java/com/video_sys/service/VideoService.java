package com.video_sys.service;

import com.video_sys.common.PageResult;
import com.video_sys.model.VideoModel;
import com.video_sys.param.VideoParam;

/**
 * 视频服务接口
 */
public interface VideoService {

    /**
     * 分页查询视频列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<VideoModel> getVideoList(VideoParam param);

    /**
     * 根据ID查询视频详情
     *
     * @param id 视频ID
     * @return 视频模型
     */
    VideoModel getVideoById(Long id);
}

