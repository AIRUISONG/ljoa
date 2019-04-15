package com.jgp.ljoa.channel.model;

import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/8/14.
 */
/*
作者  SSF
时间   2018/8/14
*/
@UI
@Entity
@Table(name = "LJ_CHANNEL_COMPANY")//第三方信息
public class LjChannelCompany extends UUIDModel {
    //公司名称
    @Column(name = "COMPANY_NAME",length = 50)
    private String companyName;
    //公司账号
    @Column(name = "COMPANY_ACCOUNT",length = 50)
    private String companyAccount;
    //公司地址
    @Column(name = "ADDRESS",length = 100)
    private String address;
    //营业执照号码
    @Column(name = "LIC_NO",length = 50)
    private String licNo;
    //开户行
    @Column(name = "COMPANY_BANK",length = 50)
    private String companyBank;
    //委托收款人姓名
    @Column(name = "DEPUTE_NAME",length = 20)
    private String deputeName;
    //委托收款人身份证号
    @Column(name = "DEPUTE_IDENTITY",length = 18)
    private String deputeIdentity;
    //委托收款人账号
    @Column(name = "DEPUTE_ACCOUNT",length = 50)
    private String deputeAccount;
    //委托收款人开户行
    @Column(name = "DEPUTE_BANK",length = 50)
    private String deputeBank;

    public LjChannelCompany(String companyName, String companyAccount, String address, String licNo, String companyBank, String deputeName, String deputeIdentity, String deputeAccount, String deputeBank) {
        this.companyName = companyName;
        this.companyAccount = companyAccount;
        this.address = address;
        this.licNo = licNo;
        this.companyBank = companyBank;
        this.deputeName = deputeName;
        this.deputeIdentity = deputeIdentity;
        this.deputeAccount = deputeAccount;
        this.deputeBank = deputeBank;
    }

    public LjChannelCompany() {
    }
    public LjChannelCompany(String companyName, String id) {
        super.setId(id);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAccount() {
        return companyAccount;
    }

    public void setCompanyAccount(String companyAccount) {
        this.companyAccount = companyAccount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicNo() {
        return licNo;
    }

    public void setLicNo(String licNo) {
        this.licNo = licNo;
    }

    public String getCompanyBank() {
        return companyBank;
    }

    public void setCompanyBank(String companyBank) {
        this.companyBank = companyBank;
    }

    public String getDeputeName() {
        return deputeName;
    }

    public void setDeputeName(String deputeName) {
        this.deputeName = deputeName;
    }

    public String getDeputeIdentity() {
        return deputeIdentity;
    }

    public void setDeputeIdentity(String deputeIdentity) {
        this.deputeIdentity = deputeIdentity;
    }

    public String getDeputeAccount() {
        return deputeAccount;
    }

    public void setDeputeAccount(String deputeAccount) {
        this.deputeAccount = deputeAccount;
    }

    public String getDeputeBank() {
        return deputeBank;
    }

    public void setDeputeBank(String deputeBank) {
        this.deputeBank = deputeBank;
    }
}
