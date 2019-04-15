package com.jgp.ljoa.reporter.bean;

import java.time.LocalDate;

/**
 * Created by Administrator on 2018/8/14.
 */
/*
作者  SSF
时间   2018/8/14
*/
//第三方结款申请子报表转换实体
public class ThirdPartyChannelCommission {
    private String projectName;//项目名
    private LocalDate subTime;//认购时间
    private LocalDate netSignTime;//网签时间
    private String customerName;//客户姓名
    private String roomNumber;//房号
    private Float acreage;//面积
    private Float totalHouseMoney;//总房款
    private String payMethod;//付款方式
    private Float actualMoney;//实收金额
    private String source;//来源
    private Float copeWith;//应付佣金金额
    private Float realPayment;//实付佣金金额
    private String remarks;//备注
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getSubTime() {
        return subTime;
    }

    public void setSubTime(LocalDate subTime) {
        this.subTime = subTime;
    }

    public LocalDate getNetSignTime() {
        return netSignTime;
    }

    public void setNetSignTime(LocalDate netSignTime) {
        this.netSignTime = netSignTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Float getAcreage() {
        return acreage;
    }

    public void setAcreage(Float acreage) {
        this.acreage = acreage;
    }

    public Float getTotalHouseMoney() {
        return totalHouseMoney;
    }

    public void setTotalHouseMoney(Float totalHouseMoney) {
        this.totalHouseMoney = totalHouseMoney;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Float getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(Float actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Float getCopeWith() {
        return copeWith;
    }

    public void setCopeWith(Float copeWith) {
        this.copeWith = copeWith;
    }

    public Float getRealPayment() {
        return realPayment;
    }

    public void setRealPayment(Float realPayment) {
        this.realPayment = realPayment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }





}
