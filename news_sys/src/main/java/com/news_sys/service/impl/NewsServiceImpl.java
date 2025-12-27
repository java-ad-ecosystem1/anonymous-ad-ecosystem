package com.news_sys.service.impl;

import com.news_sys.common.PageResult;
import com.news_sys.dao.NewsMapper;
import com.news_sys.entity.News;
import com.news_sys.model.NewsModel;
import com.news_sys.param.NewsQueryParam;
import com.news_sys.service.NewsService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻服务实现类
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public PageResult<NewsModel> getNewsList(NewsQueryParam param) {
        // 查询总数
        Long total = newsMapper.countNews(param);
        
        // 查询列表
        List<News> newsList = newsMapper.selectNewsList(param);
        
        // 转换为Model
        List<NewsModel> modelList = convertToModelList(newsList);
        
        // 构建分页结果
        return new PageResult<>(
                modelList,
                total,
                param.getPageNum(),
                param.getPageSize()
        );
    }

    @Override
    public NewsModel getNewsInfo(NewsQueryParam param) {
        News news = newsMapper.selectNewsById(param);
        return convertToModel(news);
    }


    /**
     * 将Entity列表转换为Model列表
     *
     * @param newsList Entity列表
     * @return Model列表
     */
    private List<NewsModel> convertToModelList(List<News> newsList) {
        List<NewsModel> modelList = new ArrayList<>();
        if (newsList != null && !newsList.isEmpty()) {
            for (News news : newsList) {
                NewsModel model = convertToModel(news);
                modelList.add(model);
            }
        }
        return modelList;
    }  

    /**
     * 将Entity转换为Model
     *
     * @param news Entity对象
     * @return Model对象
     */
    private NewsModel convertToModel(News news) {
        if (news == null) {
            return null;
        }
        NewsModel model = new NewsModel();
        BeanUtils.copyProperties(news, model);
        return model;
    }
}

