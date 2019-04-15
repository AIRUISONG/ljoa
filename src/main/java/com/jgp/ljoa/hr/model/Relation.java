package com.jgp.ljoa.hr.model;


import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@UI
@Entity
@Table(name = "LJ_RELATION")
public class Relation extends UUIDModel {

    public final static String RELATION_TYPE_1 = "1";
    public final static String RELATION_TYPE_2 = "2";
    public final static String RELATION_TYPE_3 = "3";
    public final static String RELATION_TYPE_4 = "4";
    public final static String RELATION_TYPE_5 = "5";//用户是否阅读信息

    //被关联主键
    @Column(name = "MAIN_UUID",length = 32)
    private String mainUuid;
    //关联主键
    @Column(name = "SUB_UUID",length = 32)
    private String subUuid;
    //被关联主键2
    @Column(name = "MAIN_ID",length = 32)
    private String mainId;
    //关联主键2
    @Column(name = "SUB_ID",length = 32)
    private String subId;
    //关系类别
    @Column(name = "RLATION_TYPE",length = 1)
    private String relationType;

    public String getMainUuid() {
        return mainUuid;
    }

    public void setMainUuid(String mainUuid) {
        this.mainUuid = mainUuid;
    }

    public String getSubUuid() {
        return subUuid;
    }

    public void setSubUuid(String subUuid) {
        this.subUuid = subUuid;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
