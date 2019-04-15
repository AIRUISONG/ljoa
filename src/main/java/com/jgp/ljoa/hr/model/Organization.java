package com.jgp.ljoa.hr.model;

import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.TreeUUIDModel;
import com.jgp.common.pojo.TreeBean;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@UI
@Entity
@Table(name = "LJ_ORGANIZATION")
public class Organization extends TreeUUIDModel {
   
    //组织机构编号
    @Column(name = "ORG_CODE",length = 50)
    private String orgCode;
    //组织机构名称
    @Column(name = "ORG_NAME",length = 100)
    private String orgName;
    //组织类别
    @Column(name = "ORG_TYPE",length = 1)
    private String orgType;
    //创建日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CREATE_TIME")
    private String createTime;
    //排序号码
    @Column(name = "SORT_CODE",length = 10)
    private String sortCode;
    //备注
    @Column(name = "REMARK",length = 255)
    private String remark;
    public Organization() {
    }

    public Organization(String orgName,String id) {
        this.orgName = orgName;
        super.setId(id);
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public TreeBean mapping() {
        TreeBean bean = new TreeBean();
        bean.setKey(this.getId() + "");
        bean.setTitle(this.getOrgName());
        bean.setBean(this);
        return bean;
    }
}
