package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/8/1.
 */
/*
作者  SSF
时间   2018/8/1
*/
@RestController
@RequestMapping("/ljoa/expense/ljExpenseMakeMoneyApiController")
public class LjExpenseMakeMoneyApiController extends JGPController {
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MessageService messageService;

    @RequestMapping("listAllLjExpenseMakeMoney")
    public Result listAllLjExpenseMakeMoney(LjExpense ljExpense, @UIParam("pager")Pager pager){
        //总经理审批的
        List<String> ll=new ArrayList<>();
        ll.add("6");
        ll.add("7");
        List<LjExpense> ljExpenses = ljExpenseService.queryGroupLjExpenseByStart(ljExpense,ll,pager);

        List<LjExpense> collect = ljExpenses.stream().map(ljExpense2 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense2.getExpenseMan());
            Employee employee = new Employee();
            if(Objects.nonNull(adminUser) && StringUtils.isNotEmpty(adminUser.getUsername())){
                employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            }
            if(Objects.nonNull(employee)){
                if(!StringUtils.isNotEmpty(employee.getPersonName())){
                    ljExpense2.setExpenseMan(employee.getPersonName());
                }
            }
            ljExpense2.setExpenseMan(employee.getPersonName());
            return ljExpense2;
        }).collect(Collectors.toList());
        List<LjExpense> ljExpenses1 = ljExpenseService.queryGroupLjExpenseByID(collect, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        categories.add(new GridResult.Category("打款状态", "payStatus","LJOA.PAY_STATUS"));
        return ajaxReGrid("gdata", categories, ljExpenses1, pager);
    }

    @RequestMapping("saveLjExpenseMakeMoney")
    public Result saveLjExpenseMakeMoney(LjExpense ljExpense){
        //打款时，向报销人发送消息
        if("1".equals(ljExpense.getPayStatus())){
            AdminUser currentAdmin = adminUserService.getCurrentAdmin();
            Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
            Message message = new Message();
            message.setSendMan(currentAdmin.getId());
            message.setSendManName(employee.getPersonName());
            message.setMsgTitle("报销打款");
            message.setMsgContent("报销已打款！");
            message.setMsgTime(LocalDateTime.now());
            message.setIsRead("0");
            message.setMsgType(Message.MESSAGE_TYPE_6);//报销审批
            message.setAcceptMan(ljExpense.getExpenseMan());
            //报销人
            AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());
            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            message.setAcceptManName(employee1.getPersonName());
            messageService.saveMessage(message);
        }
        ljExpense = ljExpenseService.saveLjExpense(ljExpense);
        ajaxRe(true);
        return ajaxReData("id",ljExpense.getId());
    }
}
