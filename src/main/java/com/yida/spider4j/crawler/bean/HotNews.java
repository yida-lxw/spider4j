package com.yida.spider4j.crawler.bean;

import com.yida.spider4j.crawler.utils.jdbc.Column;
import com.yida.spider4j.crawler.utils.jdbc.ID;
import com.yida.spider4j.crawler.utils.jdbc.Table;

@Table("news_all_stat")
public class HotNews {
    @ID
    private Long id;
    private String media;
    private String title;
    @Column("pageUrl")
    private String pageUrl;
    @Column("pageId")
    private String pageId;
    @Column("commentCount")
    private Integer commentCount;
    private String channel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
