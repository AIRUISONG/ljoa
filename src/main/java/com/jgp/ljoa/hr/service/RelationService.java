package com.jgp.ljoa.hr.service;

import com.jgp.ljoa.hr.model.Relation;
import com.jgp.sys.ui.Pager;

import java.util.List;

public interface RelationService {

    /*
    * 添加关联关系
    * */
    Relation saveRelation(Relation ljRelation);

    /*
    * 根据员工id查询所属部门id
    * */
    Relation queryOrgByEmployeeId(String id);

    /*
   * 根据被关联主键和关系类别查找
   * */
    List<Relation> queryGroupRelation(String mainUuid,String type,Pager pager);
    /*
  * 删除
  * */
    void removeRelation(String id);

    /*
    * 根据adminUser员工id查询部门id
    * */
    Relation queryRelationByAdminUserId(String adminUserId);

    /*
    * 条件查询
    * */
    List<Relation> queryGroupRelation(Relation relation);
}
