package com.jgp.ljoa.com.model;/**
 * Created by Administrator on 2018/7/5.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@UI
@Entity
@Table(name = "LJ_APPROVAL")
public class Approval extends UUIDModel {
    //审批业务
    @Column(name = "CHECK_CONTENT",length = 1)
    private String checkContent;
    //审批类型
    @Column(name = "CHECK_TYPE",length = 1)
    private String checkType;
    //对应主键
    @Column(name = "BUSI_UUID",length = 32)
    private String busiUuid;
    //审批结果
    @Column(name = "CHECK_RESULT",length = 1)
    private String checkResult;
    //审批意见
    @Column(name = "CHECK_OPTION",length = 200)
    private String checkOption;
    //审批人
    @Column(name = "CHECK_MAN",length = 32)
    private String checkMan;
    //审批时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CHECK_TIME")
    private LocalDate checkTime;

    public String getCheckContent() {
        return checkContent;
    }

    public void setCheckContent(String checkContent) {
        this.checkContent = checkContent;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getBusiUuid() {
        return busiUuid;
    }

    public void setBusiUuid(String busiUuid) {
        this.busiUuid = busiUuid;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckOption() {
        return checkOption;
    }

    public void setCheckOption(String checkOption) {
        this.checkOption = checkOption;
    }

    public String getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan;
    }

    public LocalDate getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(LocalDate checkTime) {
        this.checkTime = checkTime;
    }
}
