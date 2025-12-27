package com.news_sys.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 新闻模型类（用于返回给前端）
 */
public class NewsModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 新闻类型
     */
    private String newsType;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String describe;

    /**
     * 首页图片
     */
    private String imgUrl;

    /**
     * 标签
     */
    private String label;

    /**
     * 新闻内容
     */
    private String content;

    /**
     * 新闻来源
     */
    private String source;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public NewsModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "id=" + id +
                ", newsType='" + newsType + '\'' +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", label='" + label + '\'' +
                ", content='" + content + '\'' +
                ", source='" + source + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

