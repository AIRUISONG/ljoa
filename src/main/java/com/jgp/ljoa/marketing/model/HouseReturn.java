package com.jgp.ljoa.marketing.model;/**
 * Created by Administrator on 2018/7/23.
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
 * 时间   2018/7/23
 */
@UI
@Entity
@Table(name = "LJ_HOUSE_RETURN")//退房
public class HouseReturn extends UUIDModel {
    //房源id
    @Column(name = "HOUSE_UUID",length = 32)
    private String houseUuid;
    //姓名
    @Column(name = "APPLY_NAME",length = 10)
    private String applyName;
    //联系方式
    @Column(name = "TEL",length = 11)
    private String tel;
    //身份证号
    @Column(name = "IDENTITY_CODE",length = 18)
    private String identityCode;
    //银行卡号
    @Column(name = "BANK_CARD",length = 30)
    private String bankCard;
    //开户行
    @Column(name = "BANK",length = 20)
    private String bank;
    //应退款金额
    @Column(name = "RETURN_MONEY",length = 10,precision = 2)
    private Float returnMoney;
    //实际退款金额
    @Column(name = "ACTUAL_RETURN_MONEY",length = 10,precision = 2)
    private Float actuaLReturnMoney;
    //退款原因
    @Column(name = "RETURN_REASON",length = 200)
    private String returnReason;
    //申请日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "APPLY_DATE")
    private LocalDate applyDate;
    //申请填报人
    @Column(name = "INPUT_MAN",length = 32)
    private String inputMan;
    //审核状态
    @Column(name = "APPROVAL_STATUS",length = 2)
    private String approvalStatus;
    //退款状态
    @Column(name = "MONEY_STATUS",length = 1)
    private String moneyStatus;
    //来源
    @Column(name = "SOURCE",length = 20)
    private String source;
    //备注
    @Column(name = "REMARK",length = 200)
    private String remark;

    public String getHouseUuid() {
        return houseUuid;
    }

    public void setHouseUuid(String houseUuid) {
        this.houseUuid = houseUuid;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Float getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Float returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public LocalDate getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
    }

    public String getInputMan() {
        return inputMan;
    }

    public void setInputMan(String inputMan) {
        this.inputMan = inputMan;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getMoneyStatus() {
        return moneyStatus;
    }

    public void setMoneyStatus(String moneyStatus) {
        this.moneyStatus = moneyStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Float getActuaLReturnMoney() {
        return actuaLReturnMoney;
    }

    public void setActuaLReturnMoney(Float actuaLReturnMoney) {
        this.actuaLReturnMoney = actuaLReturnMoney;
    }
}
