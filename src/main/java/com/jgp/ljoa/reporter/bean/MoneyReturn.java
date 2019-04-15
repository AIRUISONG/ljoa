package com.jgp.ljoa.reporter.bean;

import java.time.LocalDate;

/**
 * Created by Administrator on 2018/8/13 0013.
 */
public class MoneyReturn {
    //项目名称
    private String projectName;
    //缴款时间
    private LocalDate time;
    //客户姓名
    private String name;
    //房号
    private String roomNo;
    //面积
    private Float roomArea;
    //总房款
    private Float allHouseMoney;
    //实收金额
    private Float trueMoney;
    //来源
    private String source;
    //应退款金额
    private Float refundsMoney;
    //实际退款金额
    private Float trueRefundsMoney;
    //备注
    private String remarks;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Float getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(Float roomArea) {
        this.roomArea = roomArea;
    }

    public Float getAllHouseMoney() {
        return allHouseMoney;
    }

    public void setAllHouseMoney(Float allHouseMoney) {
        this.allHouseMoney = allHouseMoney;
    }

    public Float getTrueMoney() {
        return trueMoney;
    }

    public void setTrueMoney(Float trueMoney) {
        this.trueMoney = trueMoney;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Float getRefundsMoney() {
        return refundsMoney;
    }

    public void setRefundsMoney(Float refundsMoney) {
        this.refundsMoney = refundsMoney;
    }

    public Float getTrueRefundsMoney() {
        return trueRefundsMoney;
    }

    public void setTrueRefundsMoney(Float trueRefundsMoney) {
        this.trueRefundsMoney = trueRefundsMoney;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
