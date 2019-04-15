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
@Table(name = "LJ_MARKETING_SALE_INFO")//营销销售信息
public class MarketingSaleInfo extends UUIDModel {
    //房源主键
    @Column(name = "HOUSE_UUID",length = 32)
    private String houseUuid;
    //前置赚取
    @Column(name = "PREPOSITION_EARN",length = 10,precision = 2)
    private Float prepositionEarn;
    //公司毛利润
    @Column(name = "GROSS_PROFIT",length = 10,precision = 2)
    private Float grossProfit;
    //公司纯利润
    @Column(name = "PURE_PROFIT",length = 10,precision = 2)
    private Float pureProfit;
    //备注
    @Column(name = "REMARK",length =200 )
    private String remark;
    //渠道佣金状态
    @Column(name = "CHANNE_CHARGE_STATUS",length = 2)
    private String channeChargeStatus;
    //渠道结佣时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "CHANNE_CHARGE_SEND_DATE")
    private LocalDate channeChargeSengDate;
    //内部佣金状态
    @Column(name = "INSIDE_CHARGE_STATUS",length = 2)
    private String insideChargeStatus;
    //内部结佣时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "INSIDE_CHARGE_SEND_DATE")
    private LocalDate insideChargeSengDate;
    //放款时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "LENDING_TIME")
    private LocalDate lendingTime;

    public LocalDate getLendingTime() {
        return lendingTime;
    }

    public void setLendingTime(LocalDate lendingTime) {
        this.lendingTime = lendingTime;
    }

    public String getHouseUuid() {
        return houseUuid;
    }

    public void setHouseUuid(String houseUuid) {
        this.houseUuid = houseUuid;
    }

    public Float getPrepositionEarn() {
        return prepositionEarn;
    }

    public void setPrepositionEarn(Float prepositionEarn) {
        this.prepositionEarn = prepositionEarn;
    }

    public Float getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(Float grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Float getPureProfit() {
        return pureProfit;
    }

    public void setPureProfit(Float pureProfit) {
        this.pureProfit = pureProfit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChanneChargeStatus() {
        return channeChargeStatus;
    }

    public void setChanneChargeStatus(String channeChargeStatus) {
        this.channeChargeStatus = channeChargeStatus;
    }

    public LocalDate getChanneChargeSengDate() {
        return channeChargeSengDate;
    }

    public void setChanneChargeSengDate(LocalDate channeChargeSengDate) {
        this.channeChargeSengDate = channeChargeSengDate;
    }

    public String getInsideChargeStatus() {
        return insideChargeStatus;
    }

    public void setInsideChargeStatus(String insideChargeStatus) {
        this.insideChargeStatus = insideChargeStatus;
    }

    public LocalDate getInsideChargeSengDate() {
        return insideChargeSengDate;
    }

    public void setInsideChargeSengDate(LocalDate insideChargeSengDate) {
        this.insideChargeSengDate = insideChargeSengDate;
    }
}
