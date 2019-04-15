package com.jgp.ljoa.bean;

/**
 * Created by Administrator on 2018/8/15 0015.
 */
//项目的收支情况
public class ProjectIncomeAndExpenditure {


    //项目id
    private String id;
    //项目名称
    private String projectName;
    //应收款
    private Float income;
    //已收款
    private Float receivable;
    //支出
    private Float expenditure;
    //利润
    private Float profit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getReceivable() {
        return receivable;
    }

    public void setReceivable(Float receivable) {
        this.receivable = receivable;
    }

    public Float getProfit() {
        return profit;
    }

    public void setProfit(Float profit) {
        this.profit = profit;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Float getIncome() {
        return income;
    }

    public void setIncome(Float income) {
        this.income = income;
    }

    public Float getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Float expenditure) {
        this.expenditure = expenditure;
    }
}
