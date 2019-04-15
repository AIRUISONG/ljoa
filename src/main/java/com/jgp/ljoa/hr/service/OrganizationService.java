package com.jgp.ljoa.hr.service;

import com.jgp.common.pojo.TreeBean;
import com.jgp.ljoa.hr.model.Organization;

import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
public interface OrganizationService {

    /*
    * 查找树
    * */
    List<TreeBean> queryOrganizationTree(String parentId, Boolean lazy);

    /*
    * 初始化公司部门
    * */
    Organization initOrganization(String upUuid);

    /*
    * 查询单个公司部门
    * */
    Organization queryOneOrganization(String id);

    /*
    * 保存公司部门
    * */
    Organization saveOrganization(Organization organization);

    /*
    * 删除单个公司部门
    * */
    void removeOneOrganization(String id);

    /*
    * 批量删除公司部门
    * */
    void removeSelectedOrganization(String[] array);

    /*
    * 移动部门
    * */
    Organization moveTo(String id, String toId);

    /*
    * 查询所有部门
    * */
    List<Organization> queryAllOrg();
}
