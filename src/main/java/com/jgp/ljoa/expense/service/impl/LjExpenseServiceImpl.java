package com.jgp.ljoa.expense.service.impl;

import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.repository.LjExpenseRepository;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Organization;
import com.jgp.ljoa.hr.service.OrganizationService;
import com.jgp.sys.query.Operator;
import com.jgp.sys.query.OrderDirection;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/5.
 */
/*
作者  SSF
时间   2018/7/5
*/
@Service
public class LjExpenseServiceImpl implements LjExpenseService {

    @Autowired
    private LjExpenseRepository ljExpenseRepository;
    @Autowired
    private OrganizationService organizationService;


    @Override
    public List<LjExpense> queryGroupLjExpenseByStatus(String status) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("status","eq",status);

        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(list,orders);
    }
    @Transactional
    @Override
    public LjExpense saveLjExpense(LjExpense ljExpense) {
        return ljExpenseRepository.createOrUpdate(ljExpense);
    }
    @Transactional
    @Override
    public void removeLjExpense(String id) {
        ljExpenseRepository.delete(id);
    }

    @Override
    public List<LjExpense> queryAllLjExpense() {
        return ljExpenseRepository.readAll();
    }

    @Override
    public LjExpense queryOneLjExpenseById(String id) {
        return ljExpenseRepository.read(id);
    }

    @Override
    public List<LjExpense> queryGroupLjExpenseByExpenseMan(String id, LjExpense ljExpense, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("expenseMan","eq",id);
        if(StringUtils.isNotEmpty(ljExpense.getExpenseType())){
            list.addFilter("expenseType","eq",ljExpense.getExpenseType());
        }
        if(StringUtils.isNotEmpty(ljExpense.getStatus())){
            list.addFilter("status","eq",ljExpense.getStatus());
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime", OrderDirection.DESC);
        return ljExpenseRepository.read(list,orders, pager);
    }


    @Override
    public List<LjExpense> queryExpenseApproval(LjExpense ljExpense, Pager pager) {
//        List<Organization> organizations = organizationService.queryAllOrg();
//        List<String> list = organizations.stream().filter(organization -> {
//            if ("渠道整合".equals(organization.getOrgName())) {
//                return true;
//            } else {
//                return false;
//            }
//        }).map(organization -> {
//            return organization.getId();
//        }).collect(Collectors.toList());
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljExpense.getExpenseType())){
            queryFilterList.addFilter("expenseType","eq",ljExpense.getExpenseType());
        }
        if (StringUtils.isNotEmpty(ljExpense.getStatus())){
            queryFilterList.addFilter("status","eq",ljExpense.getStatus());
        }
        if(Objects.nonNull(ljExpense.getOrgUuid()) && StringUtils.isNotEmpty(ljExpense.getOrgUuid())){
            queryFilterList.addFilter("orgUuid","eq",ljExpense.getOrgUuid());
        }
        if(Objects.nonNull(ljExpense.getApplyTime())){
            queryFilterList.addFilter("applyTime","eq",ljExpense.getApplyTime());
        }
        if(Objects.nonNull(ljExpense.getPayStatus())){
            queryFilterList.addFilter("payStatus","eq",ljExpense.getPayStatus());
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(queryFilterList,orders,pager);
    }

    @Override
    public List<LjExpense> queryExpenseApprovalByStatus(LjExpense ljExpense, List list, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljExpense.getExpenseType())){
            queryFilterList.addFilter("expenseType","eq",ljExpense.getExpenseType());
        }
        if (list != null){
            queryFilterList.addFilter("status", Operator.in,list);
        }
        if(Objects.nonNull(ljExpense.getOrgUuid()) && org.apache.commons.lang3.StringUtils.isNotEmpty(ljExpense.getOrgUuid())){
            queryFilterList.addFilter("orgUuid","eq",ljExpense.getOrgUuid());
        }
        if(Objects.nonNull(ljExpense.getApplyTime())){
            queryFilterList.addFilter("applyTime","eq",ljExpense.getApplyTime());
        }
        if(Objects.nonNull(ljExpense.getPayStatus())){
            queryFilterList.addFilter("payStatus","eq",ljExpense.getPayStatus());
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(queryFilterList,orders,pager);
    }

    @Override
    public List<LjExpense> queryExpenseApprovalByOrgUuid(LjExpense ljExpense, List list, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljExpense.getExpenseType())){
            queryFilterList.addFilter("expenseType","eq",ljExpense.getExpenseType());
        }
        if(Objects.nonNull(ljExpense.getOrgUuid()) && org.apache.commons.lang3.StringUtils.isNotEmpty(ljExpense.getOrgUuid())){
            queryFilterList.addFilter("orgUuid","eq",ljExpense.getOrgUuid());
        }
        if (StringUtils.isNotEmpty(ljExpense.getStatus())){
            queryFilterList.addFilter("status","eq",ljExpense.getStatus());
        }
        if(Objects.nonNull(ljExpense.getApplyTime())){
            queryFilterList.addFilter("applyTime","eq",ljExpense.getApplyTime());
        }
        if(Objects.nonNull(ljExpense.getPayStatus())){
            queryFilterList.addFilter("payStatus","eq",ljExpense.getPayStatus());
        }
        if(list.size() > 0){
            queryFilterList.addFilter("projectUuid",Operator.nin,list);
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(queryFilterList,orders,pager);
    }

    @Override
    public List<LjExpense> queryExpenseApprovalByNotStatus(LjExpense ljExpense, List list, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljExpense.getExpenseType())){
            queryFilterList.addFilter("expenseType","eq",ljExpense.getExpenseType());
        }
        if(Objects.nonNull(ljExpense.getOrgUuid()) && org.apache.commons.lang3.StringUtils.isNotEmpty(ljExpense.getOrgUuid())){
            queryFilterList.addFilter("orgUuid","eq",ljExpense.getOrgUuid());
        }
        if(Objects.nonNull(ljExpense.getApplyTime())){
            queryFilterList.addFilter("applyTime","eq",ljExpense.getApplyTime());
        }
        if(Objects.nonNull(ljExpense.getPayStatus())){
            queryFilterList.addFilter("payStatus","eq",ljExpense.getPayStatus());
        }
        if(list.size() > 0){
            queryFilterList.addFilter("status",Operator.nin,list);
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(queryFilterList,orders,pager);
    }


    @Override
    public List<LjExpense> queryExpenseApprovalMarketing(LjExpense ljExpense, Pager pager) {
        List<Organization> organizations = organizationService.queryAllOrg();
        List<String> list = organizations.stream().filter(organization -> {
            if ("营销".equals(organization.getOrgName())) {
                return true;
            } else {
                return false;
            }
        }).map(organization -> {
            return organization.getId();
        }).collect(Collectors.toList());
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljExpense.getExpenseType())){
            queryFilterList.addFilter("expenseType","eq",ljExpense.getExpenseType());
        }
        if(Objects.nonNull(ljExpense.getPayStatus())){
            queryFilterList.addFilter("payStatus","eq",ljExpense.getPayStatus());
        }
        /*
        if (!"".equals(ljExpense.getStatus())){
            queryFilterList.addFilter("status","eq",ljExpense.getStatus());
        }*/
        queryFilterList.addFilter("orgUuid","in",list);
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        List<LjExpense> read = ljExpenseRepository.read(queryFilterList,orders, pager);
        return read;
    }


    @Override
    public List<LjExpense> queryGroupLjExpenseBy(LjExpense ljExpense, Pager pager) {
        QueryFilterList list=new QueryFilterList();
        if (StringUtils.isNotEmpty(ljExpense.getExpenseType())){
            list.addFilter("expenseType","eq",ljExpense.getExpenseType());
        }
        if (StringUtils.isNotEmpty(ljExpense.getStatus())){
            list.addFilter("status","eq",ljExpense.getStatus());
        }
        if(StringUtils.isNotEmpty(ljExpense.getPayStatus())){
            list.addFilter("payStatus","eq",ljExpense.getPayStatus());
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(list,orders);
    }

    @Override
    public List<LjExpense> queryGroupLjExpenseByStart(LjExpense ljExpense, List<String> list, Pager pager) {
        QueryFilterList list2=new QueryFilterList();
        if (StringUtils.isNotEmpty(ljExpense.getExpenseType())){
            list2.addFilter("expenseType","eq",ljExpense.getExpenseType());
        }

            list2.addFilter("status",Operator.in,list);

        if(StringUtils.isNotEmpty(ljExpense.getPayStatus())){
            list2.addFilter("payStatus","eq",ljExpense.getPayStatus());
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(list2,orders);
    }

    @Override
    public List<LjExpense> queryGroupLjExpenseByID(List<LjExpense> list, Pager pager) {
        List<String> listID = new ArrayList<>();
        for(LjExpense ljExpense : list){
            listID.add(ljExpense.getId());
        }
        QueryFilterList list2=new QueryFilterList();
        if (listID.size() > 0){
            list2.addFilter("id",Operator.in,listID);
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(list2,orders,pager);
    }


    @Override
    public List<LjExpense> queryAllLjExpenseByAdminUserId(String adminUserId, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("expenseMan","eq",adminUserId);
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        return ljExpenseRepository.read(list,orders, pager);
    }

    @Override
    public List<LjExpense> queryGroupLjExpenseCondition(LjExpense ljExpense, List<String> list, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljExpense.getExpenseType())) {
            queryFilterList.addFilter("expenseType", "eq", ljExpense.getExpenseType());
        }
            if (list != null) {
                queryFilterList.addFilter("status",Operator.nin, list);
            }
            if (Objects.nonNull(ljExpense.getOrgUuid()) && org.apache.commons.lang3.StringUtils.isNotEmpty(ljExpense.getOrgUuid())) {
                queryFilterList.addFilter("orgUuid", "eq", ljExpense.getOrgUuid());
            }
            if (Objects.nonNull(ljExpense.getApplyTime())) {
                queryFilterList.addFilter("applyTime", "eq", ljExpense.getApplyTime());
            }
            if (Objects.nonNull(ljExpense.getPayStatus())) {
                queryFilterList.addFilter("payStatus", "eq", ljExpense.getPayStatus());
            }
            OrderList orders = new OrderList();
            orders.addOrder("applyTime",OrderDirection.DESC);
            return ljExpenseRepository.read(queryFilterList,orders, pager);
        }



    @Override
    public List<LjExpense> queryExpense(Integer month, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        int year = LocalDate.now().getYear();
        if (Objects.nonNull(month)){
            if(month==12){
                queryFilterList.addFilter("applyTime","ge",LocalDate.of(year, month, 1));
                queryFilterList.addFilter("applyTime","le",LocalDate.of(year, month, LocalDate.of(year,month,1).getMonth().maxLength()));
            }else{
                queryFilterList.addFilter("applyTime","ge",LocalDate.of(year, month, 1));
                queryFilterList.addFilter("applyTime","lt",LocalDate.of(year, month+1, 1));
            }

        }else{
            queryFilterList.addFilter("applyTime","ge",LocalDate.of(year,LocalDate.now().getMonthValue(),1));
            queryFilterList.addFilter("applyTime","le",LocalDate.of(year,LocalDate.now().getMonthValue(),LocalDate.now().getMonth().maxLength()));
        }
        OrderList orders = new OrderList();
        orders.addOrder("applyTime",OrderDirection.DESC);
        List<LjExpense> read = ljExpenseRepository.read(queryFilterList,orders, pager);
        return read;
    }
}
