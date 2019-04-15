package com.jgp.ljoa.expense.model;

/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@UI
@Entity
@Table(name = "lj_money_pay_record")//备用金明细
public class LjMoneyPayRecord extends UUIDModel {
    //备用金主键
    @Column(name = "borrow_uuid",length = 32)
    private String borrowUuid;
    //支出类型
    @Column(name = "pay_type",length = 2)
    private String payType;
    //支出名
    @Column(name = "pay_item",length = 100)
    private String payItem;
    //借款金额
    @Column(name = "pay_money",columnDefinition = "float(10,3) default '0.000'")
    private Float payMoney;
    //支出日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "pay_date")
    private LocalDate payDate;
    //支出人
    @Column(name = "pay_man",length = 32)
    private String payMan;
    //支出部门
    @Column(name = "pay_org",length = 32)
    private String  payOrg;
    //备注
    @Column(name = "remark",length = 500)
    private String remark;

    public String getBorrowUuid() {
        return borrowUuid;
    }

    public void setBorrowUuid(String borrowUuid) {
        this.borrowUuid = borrowUuid;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayItem() {
        return payItem;
    }

    public void setPayItem(String payItem) {
        this.payItem = payItem;
    }

    public Float getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Float payMoney) {
        this.payMoney = payMoney;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public String getPayMan() {
        return payMan;
    }

    public void setPayMan(String payMan) {
        this.payMan = payMan;
    }

    public String getPayOrg() {
        return payOrg;
    }

    public void setPayOrg(String payOrg) {
        this.payOrg = payOrg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
