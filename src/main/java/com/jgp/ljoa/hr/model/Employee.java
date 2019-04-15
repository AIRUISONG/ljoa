package com.jgp.ljoa.hr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@UI
@Entity
@Table(name = "LJ_EMPLOYEE")
public class Employee extends UUIDModel {
    //账号
    @Column(name = "ACCOUNT",length = 50)
    private String account;
    //姓名
    @Column(name = "PERSON_NAME",length = 20)
    private String personName;
    //工号
    @Column(name = "WORK_CODE",length = 50)
    private String workCode;
    //身份证号
    @Column(name = "IDENTITY",length = 18)
    private String identity;
    //联系方式
    @Column(name = "LINK_TEL",length = 20)
    private String linkTel;
    //性别
    @Column(name = "SEX",length = 1)
    private String sex;
    //生日
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "BIRTHDAY")
    private LocalDate birthday;
    //地址
    @Column(name = "ADDRESS",length = 200)
    private String address;
    //年龄
    @Column(name = "AGE",length = 3)
    private Integer age;
    //排序序号
    @Column(name = "ORDER_SN",length = 10)
    private Integer orderSn;
    //职级
    @Column(name = "POSI",length = 10)
    private String posi;
    //离职日期
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "LEAVE_TIME")
    private LocalDate leaveTime;
    //主动离职
    @Column(name = "LEAVE_TYPE",length = 10)
    private String leaveType;
    //被动离职
    @Column(name = "PASSIVE_TYPE",length = 10)
    private String passiveType;
    //学历
    @Column(name = "EDUCATION",length = 1)
    private String education;
    //毕业学校
    @Column(name = "SCHOOL",length = 50)
    private String school;
    //专业
    @Column(name = "SPECIALTY",length = 50)
    private String specialty;
    //紧急联系人姓名
    @Column(name = "CRITICAL_LINK_MAN",length = 20)
    private String criticalLinkMan;
    //紧急联系人电话
    @Column(name = "CRITICAL_LINK_TEL",length = 20)
    private String criticalLinkTel;
    //民族
    @Column(name = "NATION",length = 20)
    private String nation;
    //政治面貌
    @Column(name = "POLITICAL",length = 20)
    private String political;
    //籍贯
    @Column(name = "BIRTH_PLACE",length = 100)
    private String birthPlace;
    //户籍地址
    @Column(name = "CENSUS_ADDRESS",length = 100)
    private String censusAddress;
    //毕业时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "GRADUATE_TIME")
    private LocalDate graduateTime;
    //入职时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "IN_TIME")
    private LocalDate inTime;
    //员工类型
    @Column(name = "EMPLOYEE_TYPE",length = 1)
    private String employeeType;
    //合同签订公司
    @Column(name = "CONTRACT_COMPANY",length = 100)
    private String contractCompany;
    //最新合同起始时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CONTRACT_BEGIN_TIME")
    private LocalDate contractBeginTime;
    //最新合同终止时间
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "CONTRACE_END_TIME")
    private LocalDate contractEndTime;
    //档案所在地
    @Column(name = "FILE_LOCATION",length = 100)
    private String fileLocation;
    //劳动关系转移身份
    @Column(name = "RELATION_MOVE",length = 20)
    private String relationMove;
    //户籍性质
    @Column(name = "CENSUS_NATURE",length = 10)
    private String censusNature;
    //是否本地户口
    @Column(name = "IS_LOCAL_ACCOUNT",length = 10)
    private String isLocalAccount;
    //招聘渠道
    @Column(name = "RECRUIT_TYPE",length = 10)
    private String recruitType;

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIsLocalAccount() {
        return isLocalAccount;
    }

    public void setIsLocalAccount(String isLocalAccount) {
        this.isLocalAccount = isLocalAccount;
    }

    public String getPosi() {
        return posi;
    }

    public void setPosi(String posi) {
        this.posi = posi;
    }

    public LocalDate getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDate leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getPassiveType() {
        return passiveType;
    }

    public void setPassiveType(String passiveType) {
        this.passiveType = passiveType;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getCriticalLinkMan() {
        return criticalLinkMan;
    }

    public void setCriticalLinkMan(String criticalLinkMan) {
        this.criticalLinkMan = criticalLinkMan;
    }

    public String getCriticalLinkTel() {
        return criticalLinkTel;
    }

    public void setCriticalLinkTel(String criticalLinkTel) {
        this.criticalLinkTel = criticalLinkTel;
    }


    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getCensusAddress() {
        return censusAddress;
    }

    public void setCensusAddress(String censusAddress) {
        this.censusAddress = censusAddress;
    }

    public LocalDate getGraduateTime() {
        return graduateTime;
    }

    public void setGraduateTime(LocalDate graduateTime) {
        this.graduateTime = graduateTime;
    }

    public LocalDate getInTime() {
        return inTime;
    }

    public void setInTime(LocalDate inTime) {
        this.inTime = inTime;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getContractCompany() {
        return contractCompany;
    }

    public void setContractCompany(String contractCompany) {
        this.contractCompany = contractCompany;
    }

    public LocalDate getContractBeginTime() {
        return contractBeginTime;
    }

    public void setContractBeginTime(LocalDate contractBeginTime) {
        this.contractBeginTime = contractBeginTime;
    }

    public LocalDate getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(LocalDate contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getRelationMove() {
        return relationMove;
    }

    public void setRelationMove(String relationMove) {
        this.relationMove = relationMove;
    }

    public String getCensusNature() {
        return censusNature;
    }

    public void setCensusNature(String censusNature) {
        this.censusNature = censusNature;
    }



    public String getRecruitType() {
        return recruitType;
    }

    public void setRecruitType(String recruitType) {
        this.recruitType = recruitType;
    }

    public Employee() {
    }

    public Employee(String personName, String id) {
        super.setId(id);
        this.personName = personName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Integer getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(Integer orderSn) {
        this.orderSn = orderSn;
    }
}
