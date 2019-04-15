package com.jgp.ljoa.hr.service.impl;

import com.jgp.common.pojo.TreeBean;
import com.jgp.ljoa.hr.model.Organization;
import com.jgp.ljoa.hr.repository.OrganizationRepository;
import com.jgp.ljoa.hr.service.OrganizationService;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;


    @Override
    public List<TreeBean> queryOrganizationTree(String parentId, Boolean lazy) {
        OrderList orders = new OrderList();
        orders.addOrder("sortCode","ASC");
        return organizationRepository.tree(parentId,lazy,(QueryFilterList) null,orders);
    }

    @Override
    public Organization initOrganization(String upUuid) {
        Organization organization1 = new Organization();
        organization1.setParentId(upUuid);
        return organization1;
    }


    @Override
    public Organization queryOneOrganization(String id) {
        return organizationRepository.read(id);
    }

    @Transactional
    @Override
    public Organization saveOrganization(Organization organization) {
        return organizationRepository.createOrUpdate(organization);
    }

    @Transactional
    @Override
    public void removeOneOrganization(String id) {
        organizationRepository.delete(id);
    }

    @Transactional
    @Override
    public void removeSelectedOrganization(String[] array) {

    }

    @Transactional
    @Override
    public Organization moveTo(String id, String toId) {
        Organization org = organizationRepository.read(id);
        org.setParentId(toId);
        return organizationRepository.update(org,"parentId");
    }


    @Override
    public List<Organization> queryAllOrg() {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("parentId","eq","-1");
        List<Organization> read = organizationRepository.read(list);
        return read.size() > 0 ? read : new ArrayList<Organization>();
    }
}
