package com.video_sys.dao;

import com.video_sys.entity.Video;
import com.video_sys.param.VideoParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频Mapper接口
 */
@Mapper
public interface VideoMapper {

    /**
     * 根据ID查询视频详情
     *
     * @param id 视频ID
     * @return 视频实体
     */
    Video selectById(@Param("id") Long id);

    /**
     * 分页查询视频列表
     *
     * @param param 查询参数
     * @return 视频列表
     */
    List<Video> selectPageList(VideoParam param);

    /**
     * 查询总数
     *
     * @param param 查询参数
     * @return 总数
     */
    Long selectCount(VideoParam param);

    /**
     * 插入视频
     *
     * @param video 视频实体
     * @return 影响行数
     */
    int insert(Video video);

    /**
     * 更新视频
     *
     * @param video 视频实体
     * @return 影响行数
     */
    int update(Video video);

    /**
     * 删除视频
     *
     * @param id 视频ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}

