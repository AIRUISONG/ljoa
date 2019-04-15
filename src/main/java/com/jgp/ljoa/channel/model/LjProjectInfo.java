package com.jgp.ljoa.channel.model;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/


import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@UI
@Entity
@Table(name = "LJ_PROJECT_INFO")//项目信息
public class LjProjectInfo extends UUIDModel {
    //项目名称
    @Column(name = "PROJECT_NAME",length = 100)
    private String projectName;
    //区域经理（销售主管）
    @Column(name = "PRJ_DUTY_MAN",length = 32)
    private String prjDutyMan;
    //渠道总监（销售总监）
    @Column(name = "CHIEF_UUID",length = 32)
    private String chiefUuid;
    //渠道经理（销售经理）
    @Column(name = "MANAGER_UUID",length = 32)
    private String managerUuid;
    //案场经理（销售助理）
    @Column(name = "CASE_MANAGER",length = 32)
    private String caseManager;
    //概述
    @Column(name = "DESCRIBE",length = 200)
    private String describe;
    //项目类别
    @Column(name = "PROJECT_TYPE",length = 1)
    private String projectType;
    //省
    @Column(name = "PROVINCE",length = 2)
    private String province;
    //市
    @Column(name = "CITY",length = 2)
    private String city;
    //区
    @Column(name = "AREA",length = 2)
    private String area;
    //项目进度
    @Column(name = "PRJ_PROCESS",length = 1)
    private String prjProcess;
    //项目分区
    @Column(name = "projec_region",length = 2)
    private String projecRegion;

    public String getProjecRegion() {
        return projecRegion;
    }

    public void setProjecRegion(String projecRegion) {
        this.projecRegion = projecRegion;
    }

    public String getPrjProcess() {
        return prjProcess;
    }

    public void setPrjProcess(String prjProcess) {
        this.prjProcess = prjProcess;
    }

    public String getPrjDutyMan() {
        return prjDutyMan;
    }

    public void setPrjDutyMan(String prjDutyMan) {
        this.prjDutyMan = prjDutyMan;
    }

    public LjProjectInfo() {
    }

    public LjProjectInfo(String projectName, String id) {
        super.setId(id);
        this.projectName = projectName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getChiefUuid() {
        return chiefUuid;
    }

    public void setChiefUuid(String chiefUuid) {
        this.chiefUuid = chiefUuid;
    }

    public String getManagerUuid() {
        return managerUuid;
    }

    public void setManagerUuid(String managerUuid) {
        this.managerUuid = managerUuid;
    }

    public String getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(String caseManager) {
        this.caseManager = caseManager;
    }

 }
