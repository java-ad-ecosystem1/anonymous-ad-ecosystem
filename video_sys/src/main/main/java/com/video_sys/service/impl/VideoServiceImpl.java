package com.video_sys.service.impl;

import com.video_sys.common.PageResult;
import com.video_sys.dao.VideoMapper;
import com.video_sys.entity.Video;
import com.video_sys.model.VideoModel;
import com.video_sys.param.VideoParam;
import com.video_sys.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频服务实现类
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    /**
     * 分页查询视频列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @Override
    public PageResult<VideoModel> getVideoList(VideoParam param) {
        // 查询总数
        Long total = videoMapper.selectCount(param);
        
        // 查询列表
        List<Video> videoList = videoMapper.selectPageList(param);
        
        // 转换为模型
        List<VideoModel> modelList = new ArrayList<>();
        if (videoList != null && !videoList.isEmpty()) {
            for (Video video : videoList) {
                VideoModel model = convertToModel(video);
                modelList.add(model);
            }
        }
        
        // 构建分页结果
        PageResult<VideoModel> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setList(modelList);
        pageResult.setPageNum(param.getPageNum());
        pageResult.setPageSize(param.getPageSize());
        
        return pageResult;
    }

    /**
     * 根据ID查询视频详情
     *
     * @param id 视频ID
     * @return 视频模型
     */
    @Override
    public VideoModel getVideoById(Long id) {
        Video video = videoMapper.selectById(id);
        if (video == null) {
            return null;
        }
        return convertToModel(video);
    }

    /**
     * 将实体转换为模型
     *
     * @param video 视频实体
     * @return 视频模型
     */
    private VideoModel convertToModel(Video video) {
        if (video == null) {
            return null;
        }
        VideoModel model = new VideoModel();
        BeanUtils.copyProperties(video, model);
        return model;
    }
}

