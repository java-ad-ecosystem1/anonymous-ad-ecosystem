package com.news_sys.service;

import com.news_sys.common.PageResult;
import com.news_sys.model.NewsModel;
import com.news_sys.param.NewsQueryParam;

/**
 * 新闻服务接口
 */
public interface NewsService {

    /**
     * 分页查询新闻列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<NewsModel> getNewsList(NewsQueryParam param);

    /**
     * 查询新闻详情
     * @param param 查询参数
     * @return 详情结果
     */
    NewsModel getNewsInfo(NewsQueryParam param);
}

