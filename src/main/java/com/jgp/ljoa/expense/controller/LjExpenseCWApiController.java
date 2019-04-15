package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.HouseReturnService;
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

/**
 * Created by Administrator on 2018/7/8.
 */
/*
作者  SSF
时间   2018/7/8
*/
@RestController
@RequestMapping("/ljoa/expense/ljExpenseCWApiController")
public class LjExpenseCWApiController extends JGPController {
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private MessageService messageService;

    @RequestMapping("listAllLjExpenseCWYES")
    public Result listAllLjExpenseCWYES(LjExpense ljExpense, @UIParam("pager") Pager pager){
        List<String> list = new ArrayList<>();
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("9");
        list.add("10");
        list.add("11");

        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApprovalByStatus(ljExpense,list, pager);
        List<LjExpense> collect = ljExpenses.stream().map(ljExpense2 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense2.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense2.setExpenseMan(employee.getPersonName());
            return ljExpense2;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    @RequestMapping("listAllLjExpenseCWNO")
    public Result listAllLjExpenseCWNO(LjExpense ljExpense, @UIParam("pager") Pager pager){
        ljExpense.setStatus("3");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense, pager);
        ljExpenses=ljExpenses.stream().map(ljExpense2 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense2.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense2.setExpenseMan(employee.getPersonName());
            return ljExpense2;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljExpenses, pager);
    }


    //财务需要审批的退房信息
    @RequestMapping("listHouseReturnFinanceNO")
    public Result listHouseReturnFinanceNO(HouseReturn houseReturn, @UIParam("pager") Pager pager){
     /*   List<String> list=new ArrayList<>();
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");*/
        houseReturn.setApprovalStatus("3");
        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturn(houseReturn, pager);
        List<HouseReturn> list1 = houseReturns.stream().map(houseReturn2 -> {
            AdminUser adminUser = adminUserService.queryUserById(houseReturn2.getInputMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            houseReturn2.setInputMan(employee.getPersonName());
            return houseReturn2;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("姓名", "applyName"));
        categories.add(new GridResult.Category("联系方式", "tel"));
        categories.add(new GridResult.Category("身份证号", "identityCode"));
        categories.add(new GridResult.Category("银行卡号", "bankCard"));
        categories.add(new GridResult.Category("退款金额", "returnMoney"));
        categories.add(new GridResult.Category("申请日期", "applyDate"));
        categories.add(new GridResult.Category("申请填报人", "inputMan"));
        categories.add(new GridResult.Category("退款状态", "moneyStatus","LJ_HOUSE_RETURN.MONEY_STATUS"));
        categories.add(new GridResult.Category("审核状态", "approvalStatus","LJOA.EXPENSE"));
        categories.add(new GridResult.Category("退款原因", "returnReason"));
        return ajaxReGrid("gdata",categories,list1,pager);
    }
    //财务已经审批的退房信息
    @RequestMapping("listHouseReturnFinanceYES")
    public Result listHouseReturnFinanceYES(HouseReturn houseReturn, @UIParam("pager") Pager pager){
        List<String> list=new ArrayList<>();
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");

        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturnInfo(list,houseReturn, pager);
        List<HouseReturn> collect = houseReturns.stream().map(houseReturn2 -> {
            AdminUser adminUser = adminUserService.queryUserById(houseReturn2.getInputMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            houseReturn2.setInputMan(employee.getPersonName());
            return houseReturn2;
        }).collect(Collectors.toList());

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("姓名", "applyName"));
        categories.add(new GridResult.Category("联系方式", "tel"));
        categories.add(new GridResult.Category("身份证号", "identityCode"));
        categories.add(new GridResult.Category("银行卡号", "bankCard"));
        categories.add(new GridResult.Category("退款金额", "returnMoney"));
        categories.add(new GridResult.Category("申请日期", "applyDate"));
        categories.add(new GridResult.Category("申请填报人", "inputMan"));
        categories.add(new GridResult.Category("退款状态", "moneyStatus","LJ_HOUSE_RETURN.MONEY_STATUS"));
        categories.add(new GridResult.Category("审核状态", "approvalStatus","LJOA.EXPENSE"));
        categories.add(new GridResult.Category("退款原因", "returnReason"));
        return ajaxReGrid("gdata",categories,collect,pager);
    }

    //提交申请，营销总监审批
    @RequestMapping("addHouseReturnSaleDirector")
    public Result addHouseReturnSaleDirector(Approval approval){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(approval.getBusiUuid());
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());

        if("Y".equals(approval.getCheckResult())){
            houseReturn.setApprovalStatus("4");//申请通过，财务审批
        }else{
            houseReturn.setApprovalStatus("2");//申请不通过，申请填报
    }
        houseReturnService.saveHouseReturn(houseReturn);
        approvalService.saveApproval(approval);
        return ajaxRe(true);
    }
}
