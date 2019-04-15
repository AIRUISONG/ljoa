package com.jgp.ljoa.expense.service;

import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * Created by Administrator on 2018/7/5.
 */
/*
作者  SSF
时间   2018/7/5
*/
public interface LjExpenseService {

    //通过状态查询
    List<LjExpense> queryGroupLjExpenseByStatus(String status);
    //保存
    LjExpense saveLjExpense(LjExpense ljExpense);
    //删除
    void removeLjExpense(String id);
    //查全部
    List<LjExpense> queryAllLjExpense();
    //单查
    LjExpense queryOneLjExpenseById(String id);
    //通过报销人id查询
    List<LjExpense> queryGroupLjExpenseByExpenseMan(String id,LjExpense ljExpense, Pager pager);

    //条件查询报销
    List<LjExpense> queryExpenseApproval(LjExpense ljExpense, Pager pager);
    List<LjExpense> queryExpenseApprovalByStatus(LjExpense ljExpense,List list, Pager pager);
    List<LjExpense> queryExpenseApprovalByOrgUuid(LjExpense ljExpense,List list, Pager pager);
    List<LjExpense> queryExpenseApprovalByNotStatus(LjExpense ljExpense,List list, Pager pager);

    //条件查询报营销
    List<LjExpense> queryExpenseApprovalMarketing(LjExpense ljExpense, Pager pager);

    //通过状态条件查询
    List<LjExpense> queryGroupLjExpenseBy(LjExpense ljExpense,Pager pager);

    List<LjExpense> queryGroupLjExpenseByStart(LjExpense ljExpense,List<String> list,Pager pager);

    List<LjExpense> queryGroupLjExpenseByID(List<LjExpense> list,Pager pager);
    //月汇总报销
    List<LjExpense> queryExpense(Integer month, Pager pager);
    /*
    * 根据adminUser的id查询报销信息
    * */
    List<LjExpense> queryAllLjExpenseByAdminUserId(String adminUserId, Pager pager);

    /*
 * 条件查询
 * */
    List<LjExpense> queryGroupLjExpenseCondition(LjExpense ljExpense,List<String> list,Pager pager);



}
