package com.video_sys.controller;

import com.video_sys.common.PageResult;
import com.video_sys.common.Result;
import com.video_sys.model.VideoModel;
import com.video_sys.param.VideoParam;
import com.video_sys.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 视频控制器
 */
@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 分页查询视频列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<VideoModel>> list(VideoParam param) {
        PageResult<VideoModel> pageResult = videoService.getVideoList(param);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询视频详情
     *
     * @param param 请求参数（仅包含视频ID）
     * @return 视频详情（包含视频标题、播放地址、时长等信息）
     */

    @GetMapping("/detail")
    public Result<VideoModel> detail(VideoParam param) {
        VideoModel videoModel = videoService.getVideoById(param.getId());
        if (videoModel == null) {
            return Result.error("视频不存在");
        }
        return Result.success(videoModel);
    }
}

