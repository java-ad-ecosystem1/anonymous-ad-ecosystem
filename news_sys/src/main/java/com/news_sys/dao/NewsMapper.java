package com.news_sys.dao;

import com.news_sys.entity.News;
import com.news_sys.param.NewsQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 新闻Mapper接口
 */
@Mapper
public interface NewsMapper {

    /**
     * 分页查询新闻列表
     *
     * @param param 查询参数
     * @return 新闻列表
     */
    List<News> selectNewsList(NewsQueryParam param);

    /**
     * 统计新闻总数
     *
     * @param param 查询参数
     * @return 总数
     */
    Long countNews(NewsQueryParam param);

    /**
     * 根据id查询新闻详情
     * @param param 查询参数
     * @return 新闻详情
     */
    News selectNewsById(NewsQueryParam param);
}

