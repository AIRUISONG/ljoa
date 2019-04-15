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
 * Created by Administrator on 2018/7/10 0010.
 */
@UI
@Entity
@Table(name = "LJ_MARKETING_CHARGE_INFO")//营销佣金信息
public class MarketingChargeInfo extends UUIDModel {
    //房源主键
    @Column(name = "HOUSE_UUID",length = 32)
    private String houseUuid;
    //佣金对象主键
    @Column(name = "CHARGE_TARGET_UUID",length = 32)
    private String chargeTargetUuid;
    //佣金对象名称
    @Column(name = "CHARGE_TARGET_NAME",length = 50)
    private String chargeTargetName;
    //总佣金金额
    @Column(name = "CHARGE_MONEY",length = 10,precision = 2)
    private Float chargeMoney;
    //佣金比例
    @Column(name = "CHARGE_PROPORTION",length = 10,precision = 2)
    private Float chargeProportion;
    //交房前比例
    @Column(name = "FRONT_HOUSE",length = 10,precision = 2)
    private Float frontHouse ;
    //交房后比例
    @Column(name = "AFTER_HOUSE",length = 10,precision = 2)
    private Float afterHouse ;
    //佣金类型
    @Column(name = "CHARGE_TYPE",length = 1)
    private String chargeType;
    //佣金状态
    @Column(name = "CHARGE_STATUS",length = 2)
    private String chargeStatus;
    //结佣时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Column(name = "CHARGE_SEND_DATE")
    private LocalDate chargeSendDate;

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getHouseUuid() {
        return houseUuid;
    }

    public void setHouseUuid(String houseUuid) {
        this.houseUuid = houseUuid;
    }

    public String getChargeTargetUuid() {
        return chargeTargetUuid;
    }

    public void setChargeTargetUuid(String chargeTargetUuid) {
        this.chargeTargetUuid = chargeTargetUuid;
    }

    public String getChargeTargetName() {
        return chargeTargetName;
    }

    public void setChargeTargetName(String chargeTargetName) {
        this.chargeTargetName = chargeTargetName;
    }

    public Float getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(Float chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public LocalDate getChargeSendDate() {
        return chargeSendDate;
    }

    public void setChargeSendDate(LocalDate chargeSendDate) {
        this.chargeSendDate = chargeSendDate;
    }

    public Float getChargeProportion() {
        return chargeProportion;
    }

    public void setChargeProportion(Float chargeProportion) {
        this.chargeProportion = chargeProportion;
    }

    public Float getFrontHouse() {
        return frontHouse;
    }

    public void setFrontHouse(Float frontHouse) {
        this.frontHouse = frontHouse;
    }

    public Float getAfterHouse() {
        return afterHouse;
    }

    public void setAfterHouse(Float afterHouse) {
        this.afterHouse = afterHouse;
    }
}
