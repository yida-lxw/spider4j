package com.yida.spider4j.crawler.wangyi.bean;

import com.yida.spider4j.crawler.utils.jdbc.Column;
import com.yida.spider4j.crawler.utils.jdbc.ID;
import com.yida.spider4j.crawler.utils.jdbc.Table;

import java.util.Date;
import java.util.Objects;

@Table("news")
public class WangyiDetailPage {
    @ID
    private Long id;
    private String title;
    private String content;
    private String media;
    private String channel;
    @Column("publishDate")
    private Date publishDate;
    @Column("commentCount")
    private Integer commentCount;
    @Column("pageUrl")
    private String pageUrl;
    @Column("pageId")
    private String pageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WangyiDetailPage that = (WangyiDetailPage) o;
        return Objects.equals(id, that.id) || Objects.equals(pageId, that.pageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pageId);
    }
}
