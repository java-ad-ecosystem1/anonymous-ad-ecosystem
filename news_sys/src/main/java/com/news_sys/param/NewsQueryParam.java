package com.news_sys.param;

import com.news_sys.common.PageParam;

import java.math.BigInteger;

/**
 * 新闻查询参数类
 */
public class NewsQueryParam extends PageParam {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private BigInteger id;

    /**
     * 新闻类型
     */
    private String newsType;

    /**
     * 标题（模糊查询）
     */
    private String title;

    /**
     * 标签
     */
    private String label;

    /**
     * 新闻来源
     */
    private String source;

    public NewsQueryParam() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}

