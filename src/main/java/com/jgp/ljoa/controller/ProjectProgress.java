package com.jgp.ljoa.controller;

/**
 * Created by Administrator on 2018/9/12.
 */
/*
作者  SSF
时间   2018/9/12
*/
//项目转换实体
public class ProjectProgress {

    private Integer proType;//项目类型
    private String projectName;//项目名
    private String prjProcess;//项目进度
    private Float ProgressRatio;//进度比

    public Integer getProType() {
        return proType;
    }

    public void setProType(Integer proType) {
        this.proType = proType;
    }

    public ProjectProgress() {
    }

    public ProjectProgress(String projectName, String prjProcess, Float progressRatio) {
        this.projectName = projectName;
        this.prjProcess = prjProcess;
        ProgressRatio = progressRatio;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPrjProcess() {
        return prjProcess;
    }

    public void setPrjProcess(String prjProcess) {
        this.prjProcess = prjProcess;
    }

    public Float getProgressRatio() {
        return ProgressRatio;
    }

    public void setProgressRatio(Float progressRatio) {
        ProgressRatio = progressRatio;
    }
}
