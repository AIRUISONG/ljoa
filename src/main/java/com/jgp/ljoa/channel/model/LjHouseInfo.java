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
@Table(name = "LJ_HOUSE_INFO")//房源信息
public class LjHouseInfo extends UUIDModel {
    //项目主键
    @Column(name = "PROJECT_UUID",length = 32)
    private String projectUuid;
    //房主姓名
    @Column(name = "OWNER_NAME",length = 10)
    private String ownerName;
    //房主手机号
    @Column(name = "OWNER_TEL",length = 11)
    private String ownerTel;
    //房主身份证号
    @Column(name = "OWNER_IDENTITY",length = 18)
    private String ownerIdentity;
    //其他联系人姓名
    @Column(name = "OTHER_NAME",length = 10)
    private String otherName;
    //其他联系人手机号
    @Column(name = "OTHER_TEL",length = 11)
    private String otherTel;
    //其他联系人身份证号
    @Column(name = "OTHER_IDENTITY",length = 18)
    private String otherIdentity;
    //楼号
    @Column(name = "BUILDING_NO",length = 10)
    private String buildingNo;
    //单元
    @Column(name = "UNIT_NO",length = 10)
    private String unitNo;
    //房号
    @Column(name = "ROOM_NO",length = 10)
    private String roomNo;
    //面积
    @Column(name = "ROOM_AREA",length = 10,precision = 2)
    private Float roomArea;
    //产品分类
    @Column(name = "ROOM_TYPE",length = 2)
    private String roomType;
    //其他分类
    @Column(name = "OTHER_ROOM_TYPE",length = 20)
    private String otherRoomType;
    //备注
    @Column(name = "REMARK",length = 200)
    private String remark;
    //销售价格
    @Column(name = "SALE_MONEY",length = 10,precision = 2)
    private Float saleMoney;
    //销售底价
    @Column(name = "MIN_MONEY",length = 10,precision = 2)
    private Float minMoney;
    //溢价金额
    @Column(name = "PREMIUM",length = 10,precision = 2)
    private Float premium;
    // 状态
    @Column(name = "STATUS",length = 1)
    private String status;
    //房源归属
    @Column(name = "HOUSE_TYPE",length = 1)
    private String houseType;
    //公司佣金方式
    @Column(name = "COMPANY_CHARGE_TYPE",length = 1)
    private String companyChargeType;
    //公司佣金金额
    @Column(name = "COMPANY_CHARGE_MONEY",length = 10,precision = 2)
    private Float companyChargeMoney;
    //公司佣金实际支付
    @Column(name = "COMPANY_CHARGE_MONEY_TRUE",length = 10,precision = 2)
    private Float companyChargeMoneyTrue;
    //公司佣金手续费
    @Column(name = "COMPANY_CHARGE_MONEY_SERVICE_CHARGE",length = 10,precision = 2)
    private Float companyChargeMoneyServiceCharge;
    //公司佣金比例
    @Column(name = "COMPANY_CHARGE_SCALE",length = 10,precision = 2)
    private Float companyChargeScale;
    //公司佣金状态
    @Column(name = "COMPANY_CHARGE_STATUS",length = 1)
    private String companyChargeStatus;
    //结佣时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "COMPANY_CHARGE_TIME")
    private LocalDate companyChargeTime;
    //前置佣金
    @Column(name = "PRE_COMMISSION",length = 10,precision = 2)
    private Float preCommission;
    //后置佣金
    @Column(name = "POST_COMMISSION",length = 10,precision = 2)
    private Float postCommission;
    //其他佣金
    @Column(name = "OTHER_COMMISSION",length = 10,precision = 2)
    private Float otherCommission;

    public Float getCompanyChargeMoneyServiceCharge() {
        return companyChargeMoneyServiceCharge;
    }

    public void setCompanyChargeMoneyServiceCharge(Float companyChargeMoneyServiceCharge) {
        this.companyChargeMoneyServiceCharge = companyChargeMoneyServiceCharge;
    }

    public Float getPreCommission() {
        return preCommission;
    }

    public void setPreCommission(Float preCommission) {
        this.preCommission = preCommission;
    }

    public Float getPostCommission() {
        return postCommission;
    }

    public void setPostCommission(Float postCommission) {
        this.postCommission = postCommission;
    }

    public Float getOtherCommission() {
        return otherCommission;
    }

    public void setOtherCommission(Float otherCommission) {
        this.otherCommission = otherCommission;
    }

    public Float getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(Float roomArea) {
        this.roomArea = roomArea;
    }

    public LocalDate getCompanyChargeTime() {
        return companyChargeTime;
    }

    public void setCompanyChargeTime(LocalDate companyChargeTime) {
        this.companyChargeTime = companyChargeTime;
    }


    public String getCompanyChargeType() {
        return companyChargeType;
    }

    public void setCompanyChargeType(String companyChargeType) {
        this.companyChargeType = companyChargeType;
    }

    public Float getCompanyChargeMoney() {
        return companyChargeMoney;
    }

    public void setCompanyChargeMoney(Float companyChargeMoney) {
        this.companyChargeMoney = companyChargeMoney;
    }

    public Float getCompanyChargeScale() {
        return companyChargeScale;
    }

    public void setCompanyChargeScale(Float companyChargeScale) {
        this.companyChargeScale = companyChargeScale;
    }

    public String getCompanyChargeStatus() {
        return companyChargeStatus;
    }

    public void setCompanyChargeStatus(String companyChargeStatus) {
        this.companyChargeStatus = companyChargeStatus;
    }

    public Float getSaleMoney() {
        return saleMoney;
    }

    public void setSaleMoney(Float saleMoney) {
        this.saleMoney = saleMoney;
    }

    public Float getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(Float minMoney) {
        this.minMoney = minMoney;
    }

    public Float getPremium() {
        return premium;
    }

    public void setPremium(Float premium) {
        this.premium = premium;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getOwnerIdentity() {
        return ownerIdentity;
    }

    public void setOwnerIdentity(String ownerIdentity) {
        this.ownerIdentity = ownerIdentity;
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

    public String getOtherIdentity() {
        return otherIdentity;
    }

    public void setOtherIdentity(String otherIdentity) {
        this.otherIdentity = otherIdentity;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Float getCompanyChargeMoneyTrue() {
        return companyChargeMoneyTrue;
    }

    public void setCompanyChargeMoneyTrue(Float companyChargeMoneyTrue) {
        this.companyChargeMoneyTrue = companyChargeMoneyTrue;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getOtherRoomType() {
        return otherRoomType;
    }

    public void setOtherRoomType(String otherRoomType) {
        this.otherRoomType = otherRoomType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
