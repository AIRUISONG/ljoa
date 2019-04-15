package com.jgp.ljoa.expense.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by Administrator on 2018/7/5.
 */
/*
作者  SSF
时间   2018/7/5
*/
@UI
@Entity
@Table(name = "LJ_EXPENSE")//报销
public class LjExpense extends UUIDModel {
    //部门
    @Column(name = "ORG_UUID",length = 32)
    private String orgUuid;
    //项目
    @Column(name = "PROJECT_UUID",length = 100)
    private String projectUuid;
    //其他项目
    @Column(name = "other_project",length = 32)
    private String otherProject;
    //申请日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "APPLY_TIME")
    private LocalDate applyTime;
    //附件数量
    @Column(name = "FILE_NUM",length = 2)
    private Integer fileNum;
    //报销人
    @Column(name = "EXPENSE_MAN",length = 32)
    private String expenseMan;
    //收款人
    @Column(name = "RECEIVER",length = 32)
    private String receiver;
    //报销类型
    @Column(name = "EXPENSE_CATEGORY",length = 1)
    private String expenseCategory;
    //报销种类
    @Column(name = "EXPENSE_TYPE",length = 2)
    private String expenseType;
    //付款用途
    @Column(name = "PAY_USE",length = 200)
    private String payUse;
    //金额
    @Column(name = "MONEY",length = 10,precision = 2)
    private Float money;
    //部门意见
    @Column(name = "ORG_OPITION",length = 100)
    private String orgOpition;
    //财务意见
    @Column(name = "FINANCE_OPITION",length = 100)
    private String finangeOpition;
    //副总经理意见
    @Column(name = "VICE_MANAGER_OPITION",length = 100)
    private String viceManagerOpition;
    //总经理意见
    @Column(name = "MANAGER_OPITION",length = 100)
    private String managerOpition;
    //状态
    @Column(name = "STATUS",length = 5)
    private String status;
    //打款状态
    @Column(name = "PAY_STATUS",length = 1)
    private String payStatus;

    public String getOtherProject() {
        return otherProject;
    }

    public void setOtherProject(String otherProject) {
        this.otherProject = otherProject;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getFileNum() {
        return fileNum;
    }

    public void setFileNum(Integer fileNum) {
        this.fileNum = fileNum;
    }

    public String getOrgUuid() {
        return orgUuid;
    }

    public void setOrgUuid(String orgUuid) {
        this.orgUuid = orgUuid;
    }

    public LocalDate getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDate applyTime) {
        this.applyTime = applyTime;
    }


    public String getExpenseMan() {
        return expenseMan;
    }

    public void setExpenseMan(String expenseMan) {
        this.expenseMan = expenseMan;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getPayUse() {
        return payUse;
    }

    public void setPayUse(String payUse) {
        this.payUse = payUse;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getOrgOpition() {
        return orgOpition;
    }

    public void setOrgOpition(String orgOpition) {
        this.orgOpition = orgOpition;
    }

    public String getFinangeOpition() {
        return finangeOpition;
    }

    public void setFinangeOpition(String finangeOpition) {
        this.finangeOpition = finangeOpition;
    }

    public String getViceManagerOpition() {
        return viceManagerOpition;
    }

    public void setViceManagerOpition(String viceManagerOpition) {
        this.viceManagerOpition = viceManagerOpition;
    }

    public String getManagerOpition() {
        return managerOpition;
    }

    public void setManagerOpition(String managerOpition) {
        this.managerOpition = managerOpition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
