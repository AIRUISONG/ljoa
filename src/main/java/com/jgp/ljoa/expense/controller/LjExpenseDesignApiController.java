package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/10/25.
 */
/*
作者  SSF
时间   2018/10/25
*/
//设计部报销
@RestController
@RequestMapping("/ljoa/expense/LjExpenseDesignApiController")
public class LjExpenseDesignApiController extends JGPController {

    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;

    //查询设计部总监未审核报销信息
    @RequestMapping("listGroupLjExpenseDesignNo")
    public Result listGroupLjExpenseDesignNo(LjExpense ljExpense, Model model, @UIParam("pager")Pager pager){
        ljExpense.setOrgUuid("98260d14ef3a4b098fcf0e46766b8bed");//设计部门id
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
    //查询设计部总监已审核报销信息
    @RequestMapping("listGroupLjExpenseDesignYES")
    public Result listGroupLjExpenseDesignYES(LjExpense ljExpense, Model model, @UIParam("pager")Pager pager){
        ljExpense.setOrgUuid("98260d14ef3a4b098fcf0e46766b8bed");//设计部门id
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

    //查询创意部总监未审核报销信息
    @RequestMapping("listGroupLjExpenseOriginalityNO")
    public Result listGroupLjExpenseOriginalityNO(LjExpense ljExpense, Model model, @UIParam("pager")Pager pager){
        ljExpense.setOrgUuid("4fce54ec7369496cbaa1cd008ea5688b");//创意部门id
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

    //查询创意部总监已审核报销信息
    @RequestMapping("listGroupLjExpenseOriginalityYES")
    public Result listGroupLjExpenseOriginalityYES(LjExpense ljExpense, Model model, @UIParam("pager")Pager pager){
        ljExpense.setOrgUuid("4fce54ec7369496cbaa1cd008ea5688b");//创意部门id
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
