package com.jgp.ljoa.expense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/
@UI
@Entity
@Table(name = "lj_money_borrow")//备用金
public class LjMoneyBorrow extends UUIDModel {

    //部门
    @Column(name = "borrow_org",length = 32)
    private String borrowOrg;
    //借款日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "borrow_date")
    private LocalDate borrowDate;
    //借款人
    @Column(name = "borrow_man",length = 32)
    private String borrowMan;
    //借款用途
    @Column(name = "borrow_uses",length = 200)
    private String borrowUses;
    //借款金额
    @Column(name = "borrow_money",columnDefinition = "float(10,3) default '0.000'")
    private Float borrowMoney;
    //支出类型
    @Column(name = "pay_type",length = 2)
    private String payType;
    //销账截止日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "finish_date")
    private LocalDate finishDate;
    //状态
    @Column(name = "status",length = 2)
    private String status;
    //打款状态
    @Column(name = "pay_status",length = 2)
    private String payStatus;

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getBorrowOrg() {
        return borrowOrg;
    }

    public void setBorrowOrg(String borrowOrg) {
        this.borrowOrg = borrowOrg;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getBorrowMan() {
        return borrowMan;
    }

    public void setBorrowMan(String borrowMan) {
        this.borrowMan = borrowMan;
    }

    public String getBorrowUses() {
        return borrowUses;
    }

    public void setBorrowUses(String borrowUses) {
        this.borrowUses = borrowUses;
    }

    public Float getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(Float borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
