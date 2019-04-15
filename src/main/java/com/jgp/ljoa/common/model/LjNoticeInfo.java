package com.jgp.ljoa.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by Administrator on 2018/9/11.
 */
/*
作者  SSF
时间   2018/9/11
*/
@UI
@Entity
@Table(name = "LJ_NOTICE_INFO")//新闻公告
public class LjNoticeInfo extends UUIDModel {
    //标题
    @Column(name = "TITLE",length = 50)
    private String title;
    //信息类别
    @Column(name = "NOTICE_TYPE",length = 1)
    private String noticeType;
    //发布日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "PUSH_TIME")
    private LocalDate pushTime;
    //有效开始日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "START_DATE")
    private LocalDate startDate;
    //有效结束日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "END_DATE")
    private LocalDate endDate;
    //信息状态
    @Column(name = "NOTICE_STATUS",length = 1)
    private String boticeStatus;
    //内容
    @Column(name = "CONTENT",length = 2000)
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public LocalDate getPushTime() {
        return pushTime;
    }

    public void setPushTime(LocalDate pushTime) {
        this.pushTime = pushTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getBoticeStatus() {
        return boticeStatus;
    }

    public void setBoticeStatus(String boticeStatus) {
        this.boticeStatus = boticeStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
