package com.jgp.ljoa.bean;

/**
 * Created by Administrator on 2018/8/15 0015.
 */
//库存
public class Stock {
    //项目名称
    private String projectName;
    //录入房源数量
    private Integer entry;
    //卖出房源数量
    private Integer maid;
    //剩余
    private Integer surplus;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getEntry() {
        return entry;
    }

    public void setEntry(Integer entry) {
        this.entry = entry;
    }

    public Integer getMaid() {
        return maid;
    }

    public void setMaid(Integer maid) {
        this.maid = maid;
    }

    public Integer getSurplus() {
        return surplus;
    }

    public void setSurplus(Integer surplus) {
        this.surplus = surplus;
    }
}
