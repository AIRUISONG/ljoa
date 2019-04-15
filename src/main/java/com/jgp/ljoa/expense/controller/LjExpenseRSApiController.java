package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
项目  ljoa
作者  SSF
时间   2018/11/27
*/
@RestController
@RequestMapping("/ljoa/expense/LjExpenseRSApiController")
public class LjExpenseRSApiController  extends JGPController {
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;

    //查询未审批的综合管理部的审批
    @RequestMapping("listAllLjExpenseRSNO")
    public Result listAllLjExpenseRSNO(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setOrgUuid("e92f50d1ce07428faf558b7313a149db");//综合管理部
        ljExpense.setStatus("2");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense,pager);
        List<LjExpense> collect = ljExpenses.stream().map(ljExpense1 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense1.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense1.setExpenseMan(employee.getPersonName());
            return ljExpense1;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }


    //查询已审批的综合管理部的审批
    @RequestMapping("listAllLjExpenseRSYES")
    public Result listAllLjExpenseRSYES(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setOrgUuid("e92f50d1ce07428faf558b7313a149db");//综合管理部
        List<String> list=new ArrayList<>();
        list.add("0");list.add("1");list.add("2");
        List<LjExpense> ljExpenses = ljExpenseService.queryGroupLjExpenseCondition(ljExpense,list,pager);
        List<LjExpense> collect = ljExpenses.stream().map(ljExpense1 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense1.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense1.setExpenseMan(employee.getPersonName());
            return ljExpense1;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }
}
