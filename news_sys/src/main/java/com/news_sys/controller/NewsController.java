package com.news_sys.controller;

import com.news_sys.common.PageResult;
import com.news_sys.common.Result;
import com.news_sys.common.ResultCode;
import com.news_sys.model.NewsModel;
import com.news_sys.param.NewsQueryParam;
import com.news_sys.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新闻控制器
 */
@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 分页查询新闻列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<NewsModel>> getNewsList(NewsQueryParam param) {
        PageResult<NewsModel> pageResult = newsService.getNewsList(param);
        return Result.success(pageResult);
    }

    /**
     *
     * @param param 查询参数
     * @return 详情结果
     */
    @GetMapping("/detail")
    public Result<NewsModel> getNewsInfo(NewsQueryParam param) {
        if (param.getId() == null) {
            return Result.error(ResultCode.PARAM_ERROR.getCode(), "新闻ID不能为空");
        }
        return Result.success(newsService.getNewsInfo(param));
    }

}

