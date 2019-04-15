package com.jgp.ljoa.marketing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by Administrator on 2018/7/4 0004.
 */
@UI
@Entity
@Table(name = "LJ_CUSTOMER_INFO")//客户信息
public class CustomerInfo extends UUIDModel {
    //房源主键
    @Column(name = "HOUSE_UUID",length = 32)
    private String houseUuid;
    //姓名
    @Column(name = "CUSTOMER_NAME",length =20 )
    private String customerName;
    //电话
    @Column(name = "CUSTOMER_TEL",length =11 )
    private String customerTel;
    //身份证号
    @Column(name = "CUSTOMER_IDENTITY",length =18 )
    private String customerIdentify;
    //其他联系人姓名
    @Column(name = "OTHER_NAME",length =20 )
    private String otherName;
    //其他联系人电话
    @Column(name = "OTHER_TEL",length =11 )
    private String otherTel;
    //其他联系人身份证号
    @Column(name = "OTHER_IDENTITY",length =18 )
    private String otherIdentify;
    //成交价格
    @Column(name = "TRANSACTION_PRICE",length = 10,precision = 2)
    private Float transactionPrice;
    //优惠金额
    @Column(name = "DISCOUNT_MONEY",length =10,precision = 2)
    private Float discountMoney;
    //缴纳定金
    @Column(name = "EARNEST",length =10,precision = 2)
    private Float earnest;
    //缴纳定金时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "EARNEST_DATE")
    private LocalDate earnestDate;
    //定金状态
    @Column(name = "EARNEST_STATUS",length =1 )
    private String earnestStatus;
    //定金退回原因
    @Column(name = "RETURN_EARNEST_REASON",length =100 )
    private String returnEarnestReason;
    //定金备注
    @Column(name = "EARNEST_REMARK",length =200 )
    private String earnestRemark;
    //定金去向
    @Column(name = "EARNESTDIRECTION",length = 1)
    private String earnestDirection;
    //客户来访区域
    @Column(name = "CUSTOMER_AREA",length =50 )
    private String customerArea;
    //成交日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "TRADING_DATE")
    private LocalDate tradingDate;
    //网签约定日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "APPOINT_DATE")
    private LocalDate appointDate;
    //网签日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "SIGN_DATE")
    private LocalDate signDate;
    //网签超时备注
    @Column(name = "SIGN_OVERTIME_REMARK",length =200 )
    private String signOvertimeRemark;
    //网签延期期限
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "POSTPONE_LIMIT")
    private LocalDate postponeLimit;
    //客户首付款
    @Column(name = "DOWN_PAYMENT",length =10,precision = 2)
    private Float downPayment;
    //付款方式
    @Column(name = "PATY_TYPE",length =1 )
    private String patyType;
    //商贷额度
    @Column(name = "COMMERCIAL_MONEY",length =10,precision = 2)
    private Float commercialMoney;
    //公积金贷款额度
    @Column(name = "FUND_MONEY",length =10,precision = 2)
    private Float fundMoney;
    //垫资额度
    @Column(name = "ADVANCE_MONEY",length =10,precision = 2)
    private Float advanceMoney;
    //垫资日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "ADVANCE_DATE")
    private LocalDate advanceDate;
    //垫资还款日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "REFUND_DATE")
    private LocalDate refundDate;
    //网签状态
    @Column(name = "SIGN_STATUS",length =1 )
    private String signStatus;
    //贷款回款状态
    @Column(name = "RETURN_MONEY",length =1 )
    private String returnMoney;
    //团购费
    @Column(name = "group_buying_money",columnDefinition = "float(10,3) default '0.000'")
    private Float groupBuyingMoney;
    //团购费去向
    @Column(name = "group_buying_money_destination",length =2)
    private String groupBuyingMoneyDestination;
    //团购费缴纳时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "group_buying_money_date")
    private LocalDate groupBuyingMoneyDate;

    public Float getGroupBuyingMoney() {
        return groupBuyingMoney;
    }

    public void setGroupBuyingMoney(Float groupBuyingMoney) {
        this.groupBuyingMoney = groupBuyingMoney;
    }

    public String getGroupBuyingMoneyDestination() {
        return groupBuyingMoneyDestination;
    }

    public void setGroupBuyingMoneyDestination(String groupBuyingMoneyDestination) {
        this.groupBuyingMoneyDestination = groupBuyingMoneyDestination;
    }

    public LocalDate getGroupBuyingMoneyDate() {
        return groupBuyingMoneyDate;
    }

    public void setGroupBuyingMoneyDate(LocalDate groupBuyingMoneyDate) {
        this.groupBuyingMoneyDate = groupBuyingMoneyDate;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getEarnestRemark() {
        return earnestRemark;
    }

    public void setEarnestRemark(String earnestRemark) {
        this.earnestRemark = earnestRemark;
    }

    public String getHouseUuid() {
        return houseUuid;
    }

    public void setHouseUuid(String houseUuid) {
        this.houseUuid = houseUuid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getCustomerIdentify() {
        return customerIdentify;
    }

    public void setCustomerIdentify(String customerIdentify) {
        this.customerIdentify = customerIdentify;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getOtherTel() {
        return otherTel;
    }

    public void setOtherTel(String otherTel) {
        this.otherTel = otherTel;
    }

    public String getOtherIdentify() {
        return otherIdentify;
    }

    public void setOtherIdentify(String otherIdentify) {
        this.otherIdentify = otherIdentify;
    }

    public Float getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(Float transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public Float getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Float discountMoney) {
        this.discountMoney = discountMoney;
    }

    public Float getEarnest() {
        return earnest;
    }

    public void setEarnest(Float earnest) {
        this.earnest = earnest;
    }

    public String getEarnestStatus() {
        return earnestStatus;
    }

    public void setEarnestStatus(String earnestStatus) {
        this.earnestStatus = earnestStatus;
    }

    public String getReturnEarnestReason() {
        return returnEarnestReason;
    }

    public void setReturnEarnestReason(String returnEarnestReason) {
        this.returnEarnestReason = returnEarnestReason;
    }

    public String getEarnestDirection() {
        return earnestDirection;
    }

    public void setEarnestDirection(String earnestDirection) {
        this.earnestDirection = earnestDirection;
    }

    public String getCustomerArea() {
        return customerArea;
    }

    public void setCustomerArea(String customerArea) {
        this.customerArea = customerArea;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }

    public LocalDate getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(LocalDate appointDate) {
        this.appointDate = appointDate;
    }

    public LocalDate getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }

    public String getSignOvertimeRemark() {
        return signOvertimeRemark;
    }

    public void setSignOvertimeRemark(String signOvertimeRemark) {
        this.signOvertimeRemark = signOvertimeRemark;
    }

    public LocalDate getPostponeLimit() {
        return postponeLimit;
    }

    public void setPostponeLimit(LocalDate postponeLimit) {
        this.postponeLimit = postponeLimit;
    }

    public Float getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(Float downPayment) {
        this.downPayment = downPayment;
    }

    public String getPatyType() {
        return patyType;
    }

    public void setPatyType(String patyType) {
        this.patyType = patyType;
    }

    public Float getCommercialMoney() {
        return commercialMoney;
    }

    public void setCommercialMoney(Float commercialMoney) {
        this.commercialMoney = commercialMoney;
    }

    public Float getFundMoney() {
        return fundMoney;
    }

    public void setFundMoney(Float fundMoney) {
        this.fundMoney = fundMoney;
    }

    public Float getAdvanceMoney() {
        return advanceMoney;
    }

    public void setAdvanceMoney(Float advanceMoney) {
        this.advanceMoney = advanceMoney;
    }

    public LocalDate getAdvanceDate() {
        return advanceDate;
    }

    public void setAdvanceDate(LocalDate advanceDate) {
        this.advanceDate = advanceDate;
    }

    public LocalDate getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(LocalDate refundDate) {
        this.refundDate = refundDate;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public LocalDate getEarnestDate() {
        return earnestDate;
    }

    public void setEarnestDate(LocalDate earnestDate) {
        this.earnestDate = earnestDate;
    }
}
