package com.jgp.ljoa.channel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
@UI
@Entity
@Table(name = "LJ_HOUSE_SALE_INFO")//房源销售信息
public class LjHouseSaleInfo extends UUIDModel {
    //房源主键
    @Column(name = "HOUSE_UUID",length = 32)
    private String houseUuid;
    //客户姓名
    @Column(name = "CUSTOMER_NAME",length = 20)
    private String customerName;
    //客户电话
    @Column(name = "CUSTOMER_TEL",length = 11)
    private String customerTel;
    //身份证号
    @Column(name = "IDENTITY_CODE",length = 18)
    private String identityCode;
    //成交总价
    @Column(name = "TOTAL_PRICE",length = 10,precision = 5)
    private Float totalPrice;
    //认购日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "BUY_TIME")
    private LocalDate buyTime;
    //签约日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "SIGN_TIME")
    private LocalDate signTime;
    //付款方式
    @Column(name = "PAY_TYPE",length = 1)
    private String payType;
    //负责人
    @Column(name = "DUTY_MAN_UUID",length = 32)
    private String dutyManUuid;

    //公司业绩
    @Column(name = "company_tax",columnDefinition = "float(10,3) default '0.000'")
    private Float companyTax;
    //公司业绩扣税方式
    @Column(name = "company_tax_deduction",length = 1)
    private String companyTaxDeduction;
    //公司业绩扣税比例
    @Column(name = "company_tax_deduction_scale",length = 5)
    private String companyTaxDeductionScale;
    //公司业绩扣税金额
    @Column(name = "company_tax_deduction_money",columnDefinition = "float(10,3) default '0.000'")
    private Float companyTaxDeductionMoney;
    //公司业绩税后金额
    @Column(name = "company_tax_deduction_after_money",columnDefinition = "float(10,3) default '0.000'")
    private Float companyTaxDeductionAfterMoney;

    //渠道公司
    @Column(name = "CHANNEL_COMPANY",length = 32)
    private String channelCompany;
    //渠道佣金方式
    @Column(name = "CHANNEL_CHARGE_TYPE",length = 1)
    private String channelChargeType;
    //渠道佣金金额
    @Column(name = "CHANNEL_CHARGE_MONEY",columnDefinition = "float(10,3) default '0.000'")
    private Float channelChargeMoney;
    //渠道佣金比例
    @Column(name = "CHANNEL_CHARGE_SCALE",columnDefinition = "float(10,3) default '0.000'")
    private Float channelChargeScale;
    //渠道佣金缴税方式
    @Column(name = "CHANNEL_TAX_TYPE",length = 1)
    private String channelTaxType;
    //渠道扣税比例
    @Column(name = "CHANNEL_TAX_SCALE",columnDefinition = "float(10,3) default '0.000'")
    private Float  channelTaxScale;
    //渠道扣税金额
    @Column(name = "CHANNEL_TAX_MONEY",columnDefinition = "float(10,5) default '0.00000'")
    private Float channelTaxMoney;
    //渠道公司税后佣金
    @Column(name = "CHANNEL_AFTER_TAX_MONEY",columnDefinition = "float(10,5) default '0.00000'")
    private Float channelAfterTaxMoney;
    //渠道佣金状态
    @Column(name = "CHANNEL_CHARGE_STATUS",length = 1)
    private String channelChargeStatus;
    //渠道结佣时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CHANNEL_CHARGE_SEND_DATE")
    private LocalDate channelChargeSendDate;
    //个人奖
    @Column(name = "DUTY_MAN_MONEY",columnDefinition = "float(10,5) default '0.00000'")
    private Float dutyManMoney;
    //个人
    @Column(name = "DUTY_MAN",length = 10)
    private String dutyMan;
    //案场经理
    @Column(name = "CASE_MANAGER",length = 32)
    private String caseManager;
    //案场经理提成方式
    @Column(name = "CASE_MANAGER_CHARGE_TYPE",length = 1)
    private String caseManagerChargeType;
    //案场经理提成比例
    @Column(name = "CASE_MANAGER_CHARGE_SCALE",length = 2)
    private String caseManagerChargeScale;
    //案场经理提成金额
    @Column(name = "CASE_MANAGER_CHARGE_MONEY",columnDefinition = "float(10,5) default '0.00000'")
    private Float caseManagerChargeMoney;
    //渠道经理
    @Column(name = "CHANNEL_MANGER",length = 32)
    private String channelManager;
    //渠道经理提成方式
    @Column(name = "CHANNEL_MANGER_CHARGE_TYPE",length = 1)
    private String channelManagerChargeType;
    //渠道经理提成比例
    @Column(name = "CHANNEL_MANAGER_CHARGE_SCALE",length = 2)
    private String channelManagerChargeScale;
    //渠道经理提成金额
    @Column(name = "CHANNEL_MANAGER_CHARGE_MONEY",columnDefinition = "float(10,5) default '0.00000'")
    private Float channelManagerChargeMoney;
    //渠道总监
    @Column(name = "CHANNEL_CHIEF",length = 32)
    private String channelChief;
    //渠道总监提成方式
    @Column(name = "CHANNEL_CHIEF_CHARGE_TYPE",length = 1)
    private String channelChiefChargeType;
    //渠道总监提成比例
    @Column(name = "CHANNEL_CHIEF_CHARGE_SCALE",length = 2)
    private String channelChiefChargeScale;
    //渠道总监提成金额
    @Column(name = "CHANNEL_CHIEF_CHARGE_MONEY",columnDefinition = "float(10,5) default '0.00000'")
    private Float channelChiefChargeMoney;
    //内部佣金状态
    @Column(name = "INSIDE_CHARGE_STATUS",length = 1)
    private String insideChargeStatus;
    //内部结佣时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "INSIDE_CHARGE_SEND_DATE")
    private LocalDate insideChargeSendDate;
    //顾问姓名
    @Column(name = "ADVISER",length = 32)
    private String adviser;
    //顾问提成
    @Column(name = "ADVISER_MONTY",columnDefinition = "float(10,5) default '0.00000'")
    private Float adviserMonty;
    //公司利润
    @Column(name = "COMPANY_PROFIT",columnDefinition = "float(10,5) default '0.00000'")
    private Float companyProfit;
    //佣金状态
    @Column(name = "CHARGE_STATUS",length = 2)
    private String chargeStatus;
    //结佣时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CHARGE_SEND_DATE")
    private LocalDate chargeSendDate;
    //区域经理
    @Column(name = "AREA_MANAGE",length = 32)
    private String areaManage;
    //区域经理提成方式
    @Column(name = "AREA_MANAGER_CHARGE_TYPE",length = 1)
    private String areaManagerChargeType;
    //区域经理提成比例
    @Column(name = "AREA_MANGER_CHARGE_SCALE",length = 2)
    private String areaManagerChargeScale;
    //区域经理提成金额
    @Column(name = "AREA_MANGER_CHARGE_MONEY",columnDefinition = "float(10,5) default '0.00000'")
    private Float areaManagerChargeMoney;
    //放款时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "LENDING_TIME")
    private LocalDate lendingTime;
    //是否上传发票附件
    @Column(name = "invoice",length = 2)
    private String invoice;

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Float getCompanyTaxDeductionAfterMoney() {
        return companyTaxDeductionAfterMoney;
    }

    public void setCompanyTaxDeductionAfterMoney(Float companyTaxDeductionAfterMoney) {
        this.companyTaxDeductionAfterMoney = companyTaxDeductionAfterMoney;
    }

    public String getCompanyTaxDeductionScale() {
        return companyTaxDeductionScale;
    }

    public void setCompanyTaxDeductionScale(String companyTaxDeductionScale) {
        this.companyTaxDeductionScale = companyTaxDeductionScale;
    }

    public String getCompanyTaxDeduction() {
        return companyTaxDeduction;
    }

    public void setCompanyTaxDeduction(String companyTaxDeduction) {
        this.companyTaxDeduction = companyTaxDeduction;
    }


    public Float getCompanyTax() {
        return companyTax;
    }

    public void setCompanyTax(Float companyTax) {
        this.companyTax = companyTax;
    }


    public Float getCompanyTaxDeductionMoney() {
        return companyTaxDeductionMoney;
    }

    public void setCompanyTaxDeductionMoney(Float companyTaxDeductionMoney) {
        this.companyTaxDeductionMoney = companyTaxDeductionMoney;
    }

    public Float getChannelTaxScale() {
        return channelTaxScale;
    }

    public void setChannelTaxScale(Float channelTaxScale) {
        this.channelTaxScale = channelTaxScale;
    }

    public LocalDate getLendingTime() {
        return lendingTime;
    }

    public void setLendingTime(LocalDate lendingTime) {
        this.lendingTime = lendingTime;
    }

    public Float getAreaManagerChargeMoney() {
        return areaManagerChargeMoney;
    }

    public void setAreaManagerChargeMoney(Float areaManagerChargeMoney) {
        this.areaManagerChargeMoney = areaManagerChargeMoney;
    }

    public String getChannelChargeStatus() {
        return channelChargeStatus;
    }

    public void setChannelChargeStatus(String channelChargeStatus) {
        this.channelChargeStatus = channelChargeStatus;
    }

    public LocalDate getChannelChargeSendDate() {
        return channelChargeSendDate;
    }

    public void setChannelChargeSendDate(LocalDate channelChargeSendDate) {
        this.channelChargeSendDate = channelChargeSendDate;
    }

    public String getInsideChargeStatus() {
        return insideChargeStatus;
    }

    public void setInsideChargeStatus(String insideChargeStatus) {
        this.insideChargeStatus = insideChargeStatus;
    }

    public LocalDate getInsideChargeSendDate() {
        return insideChargeSendDate;
    }

    public void setInsideChargeSendDate(LocalDate insideChargeSendDate) {
        this.insideChargeSendDate = insideChargeSendDate;
    }

    public String getAreaManage() {
        return areaManage;
    }

    public void setAreaManage(String areaManage) {
        this.areaManage = areaManage;
    }

    public String getAreaManagerChargeType() {
        return areaManagerChargeType;
    }

    public void setAreaManagerChargeType(String areaManagerChargeType) {
        this.areaManagerChargeType = areaManagerChargeType;
    }

    public String getAreaManagerChargeScale() {
        return areaManagerChargeScale;
    }

    public void setAreaManagerChargeScale(String areaManagerChargeScale) {
        this.areaManagerChargeScale = areaManagerChargeScale;
    }



    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public Float getChannelChargeScale() {
        return channelChargeScale;
    }

    public void setChannelChargeScale(Float channelChargeScale) {
        this.channelChargeScale = channelChargeScale;
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

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(LocalDate buyTime) {
        this.buyTime = buyTime;
    }

    public LocalDate getSignTime() {
        return signTime;
    }

    public void setSignTime(LocalDate signTime) {
        this.signTime = signTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getDutyManUuid() {
        return dutyManUuid;
    }

    public void setDutyManUuid(String dutyManUuid) {
        this.dutyManUuid = dutyManUuid;
    }

    public String getChannelCompany() {
        return channelCompany;
    }

    public void setChannelCompany(String channelCompany) {
        this.channelCompany = channelCompany;
    }

    public String getChannelChargeType() {
        return channelChargeType;
    }

    public void setChannelChargeType(String channelChargeType) {
        this.channelChargeType = channelChargeType;
    }

    public Float getChannelChargeMoney() {
        return channelChargeMoney;
    }

    public void setChannelChargeMoney(Float channelChargeMoney) {
        this.channelChargeMoney = channelChargeMoney;
    }


    public String getChannelTaxType() {
        return channelTaxType;
    }

    public void setChannelTaxType(String channelTaxType) {
        this.channelTaxType = channelTaxType;
    }


    public Float getChannelTaxMoney() {
        return channelTaxMoney;
    }

    public void setChannelTaxMoney(Float channelTaxMoney) {
        this.channelTaxMoney = channelTaxMoney;
    }

    public Float getChannelAfterTaxMoney() {
        return channelAfterTaxMoney;
    }

    public void setChannelAfterTaxMoney(Float channelAfterTaxMoney) {
        this.channelAfterTaxMoney = channelAfterTaxMoney;
    }

    public Float getDutyManMoney() {
        return dutyManMoney;
    }

    public void setDutyManMoney(Float dutyManMoney) {
        this.dutyManMoney = dutyManMoney;
    }

    public String getDutyMan() {
        return dutyMan;
    }

    public void setDutyMan(String dutyMan) {
        this.dutyMan = dutyMan;
    }

    public String getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(String caseManager) {
        this.caseManager = caseManager;
    }

    public String getCaseManagerChargeType() {
        return caseManagerChargeType;
    }

    public void setCaseManagerChargeType(String caseManagerChargeType) {
        this.caseManagerChargeType = caseManagerChargeType;
    }

    public String getCaseManagerChargeScale() {
        return caseManagerChargeScale;
    }

    public void setCaseManagerChargeScale(String caseManagerChargeScale) {
        this.caseManagerChargeScale = caseManagerChargeScale;
    }

    public Float getCaseManagerChargeMoney() {
        return caseManagerChargeMoney;
    }

    public void setCaseManagerChargeMoney(Float caseManagerChargeMoney) {
        this.caseManagerChargeMoney = caseManagerChargeMoney;
    }

    public String getChannelManager() {
        return channelManager;
    }

    public void setChannelManager(String channelManager) {
        this.channelManager = channelManager;
    }

    public String getChannelManagerChargeType() {
        return channelManagerChargeType;
    }

    public void setChannelManagerChargeType(String channelManagerChargeType) {
        this.channelManagerChargeType = channelManagerChargeType;
    }

    public String getChannelManagerChargeScale() {
        return channelManagerChargeScale;
    }

    public void setChannelManagerChargeScale(String channelManagerChargeScale) {
        this.channelManagerChargeScale = channelManagerChargeScale;
    }

    public Float getChannelManagerChargeMoney() {
        return channelManagerChargeMoney;
    }

    public void setChannelManagerChargeMoney(Float channelManagerChargeMoney) {
        this.channelManagerChargeMoney = channelManagerChargeMoney;
    }

    public String getChannelChief() {
        return channelChief;
    }

    public void setChannelChief(String channelChief) {
        this.channelChief = channelChief;
    }

    public String getChannelChiefChargeType() {
        return channelChiefChargeType;
    }

    public void setChannelChiefChargeType(String channelChiefChargeType) {
        this.channelChiefChargeType = channelChiefChargeType;
    }

    public String getChannelChiefChargeScale() {
        return channelChiefChargeScale;
    }

    public void setChannelChiefChargeScale(String channelChiefChargeScale) {
        this.channelChiefChargeScale = channelChiefChargeScale;
    }

    public Float getChannelChiefChargeMoney() {
        return channelChiefChargeMoney;
    }

    public void setChannelChiefChargeMoney(Float channelChiefChargeMoney) {
        this.channelChiefChargeMoney = channelChiefChargeMoney;
    }

    public String getAdviser() {
        return adviser;
    }

    public void setAdviser(String adviser) {
        this.adviser = adviser;
    }

    public Float getAdviserMonty() {
        return adviserMonty;
    }

    public void setAdviserMonty(Float adviserMonty) {
        this.adviserMonty = adviserMonty;
    }

    public Float getCompanyProfit() {
        return companyProfit;
    }

    public void setCompanyProfit(Float companyProfit) {
        this.companyProfit = companyProfit;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public LocalDate getChargeSendDate() {
        return chargeSendDate;
    }

    public void setChargeSendDate(LocalDate chargeSendDate) {
        this.chargeSendDate = chargeSendDate;
    }
}
