package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Organization;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.security.admin.model.AdminRoleUser;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/6.
 */
/*
作者  SSF
时间   2018/7/6
*/
@RestController
@RequestMapping("/ljoa/expense/ljExpenseApiController")
public class LjExpenseApiController extends JGPController {

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
    @Autowired
    private RelationService relationService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;


    /*
    * 根据adminUser的id查询报销信息
    * */
    @RequestMapping("listAllLjExpenseByAdminUserId")
    public Result listAllLjExpenseByAdminUserId(@UIParam("pager") Pager pager,LjExpense ljExpense1){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        List<LjExpense> ljExpenses = ljExpenseService.queryGroupLjExpenseByExpenseMan(currentAdmin.getId(), ljExpense1, pager);
//        ljExpenseService.queryAllLjExpense();
        List<LjExpense> collect = ljExpenses.stream().map(ljExpense -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense.setExpenseMan(employee.getPersonName());
            return ljExpense;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));

        for( LjExpense ljExpense:collect){
            if(ljExpense.getProjectUuid()!=null){
                LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljExpense.getProjectUuid());
                if(ljProjectInfo!=null&&ljProjectInfo.getProjectName()!=null){
                    ljExpense.setProjectUuid(ljProjectInfo.getProjectName());
                }
            }
        }
        categories.add(new GridResult.Category("对应项目", "projectUuid"));
        categories.add(new GridResult.Category("其他对应", "otherProject"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    /*
    * 渠道总监查询所有报销
    * */
    @RequestMapping("listAllLjExpenseBXQD")
    public Result listAllLjExpenseBXQD(LjExpense ljExpense, @UIParam("pager")Pager pager){
        ljExpense.setOrgUuid("3bc4148f06684cf6bebcf80bbbf2971c");
        ljExpense.setStatus("1");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan", Employee.class,"personName"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        for( LjExpense ljExpense1:ljExpenses){
            if(ljExpense1.getProjectUuid()!=null){
                LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljExpense1.getProjectUuid());
                if(ljProjectInfo!=null&&ljProjectInfo.getProjectName()!=null){
                    ljExpense1.setProjectUuid(ljProjectInfo.getProjectName());
                }
            }
        }
        categories.add(new GridResult.Category("对应项目", "projectUuid"));
        categories.add(new GridResult.Category("其他对应", "otherProject"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljExpenses, pager);
    }

    /*
    * 营销总监查询所有报销
    * */
    @RequestMapping("listAllLjExpenseBXYX")
    public Result listAllLjExpenseBXYX(LjExpense ljExpense, @UIParam("pager")Pager pager){
        ljExpense.setOrgUuid("d4a1dd4ca37749fc9380f8d61454305c");
        ljExpense.setStatus("1");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan", Employee.class,"personName"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        for( LjExpense ljExpense1:ljExpenses){
            if(ljExpense1.getProjectUuid()!=null){
                LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljExpense1.getProjectUuid());
                if(ljProjectInfo!=null&&ljProjectInfo.getProjectName()!=null){
                    ljExpense1.setProjectUuid(ljProjectInfo.getProjectName());
                }
            }
        }
        categories.add(new GridResult.Category("对应项目", "projectUuid"));
        categories.add(new GridResult.Category("其他对应", "otherProject"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljExpenses, pager);
    }

    @RequestMapping("listGroupLjExpense/{id}")
    public Result listGroupLjExpense(@PathVariable String id,LjExpense ljExpense,@UIParam("pager") Pager pager){
        List<LjExpense> ljExpenses = ljExpenseService.queryGroupLjExpenseByExpenseMan(id,ljExpense, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        for( LjExpense ljExpense1:ljExpenses){
            if(ljExpense1.getProjectUuid()!=null){
                LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljExpense1.getProjectUuid());
                if(ljProjectInfo!=null&&ljProjectInfo.getProjectName()!=null){
                    ljExpense1.setProjectUuid(ljProjectInfo.getProjectName());
                }
            }
        }
        categories.add(new GridResult.Category("对应项目", "projectUuid"));
        categories.add(new GridResult.Category("其他对应", "otherProject"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljExpenses, pager);
    }


    @RequestMapping("saveLjExpense")
    public Result saveLjExpense(LjExpense ljExpense){
/*        if("47e34c25444041d799d04bdfb29a00a4".equals(ljExpense.getOrgUuid())){
            ljExpense.setStatus("3");//财务审批
        }*/
        ljExpense.setPayStatus("0");
        ljExpense = ljExpenseService.saveLjExpense(ljExpense);
        ajaxRe(true);
        return ajaxReData("id",ljExpense.getId());
    }
    @RequestMapping("editLjExpense")
    public Result editLjExpense(LjExpense ljExpense){
        //当前登陆用户
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Relation relation = relationService.queryRelationByAdminUserId(currentAdmin.getId());

        List<AdminRoleUser> adminRoleUsers = new ArrayList<>();
        //营销提交报销，向销售总监发送消息
        if("d4a1dd4ca37749fc9380f8d61454305c".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("销售总监");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljExpense.setStatus("2");
        //渠道提交报销，向渠道总监发送消息
        }else if("3bc4148f06684cf6bebcf80bbbf2971c".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("渠道总监");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljExpense.setStatus("2");
        //人事提交报销
        }else if("e92f50d1ce07428faf558b7313a149db".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("综合管理经理");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljExpense.setStatus("2");
        //财务/会计提交报销
        }else if("47e34c25444041d799d04bdfb29a00a4".equals(relation.getMainUuid())){
//            String roleId = messageService.queryAdminRoleIdByRoleName("副总经理");
            //向财务发送消息
            String roleId = messageService.queryAdminRoleIdByRoleName("财务");
            String roleId1 = messageService.queryAdminRoleIdByRoleName("会计");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            List<AdminRoleUser> adminRoleUsers1 = messageService.queryAdminUserByRoleId(roleId1);
            adminRoleUsers.addAll(adminRoleUsers1);
            ljExpense.setStatus("3");
        //创意策划提交报销，向策划总监发送消息
        }else if("4fce54ec7369496cbaa1cd008ea5688b".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("策划总监");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljExpense.setStatus("2");
        //家装设计提交报销，向设计总监发送消息
        }else if("98260d14ef3a4b098fcf0e46766b8bed".equals(relation.getMainUuid())){
        String roleId = messageService.queryAdminRoleIdByRoleName("设计总监");
        adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
        ljExpense.setStatus("2");
        //新房事业部提交报销，向新房事业部总经理发送消息
         }else if("fe488cafd8174bd789df302cae699263".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("新房事业部总经理");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljExpense.setStatus("2");
            //二手房事业部提交报销，向二手房事业部总经理
        }else if("80df0136da4c4582a6a113ff97073b8f".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("二手房事业部总经理");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljExpense.setStatus("2");
        }

        //员工姓名
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        for (AdminRoleUser a: adminRoleUsers) {
            AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
            if(Objects.nonNull(adminUser)){
                Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                if(Objects.nonNull(employee1)){
                    Message message = new Message();
                    message.setSendMan(currentAdmin.getId());
                    message.setSendManName(employee.getPersonName());
                    message.setMsgTitle("报销审批");
                    message.setMsgContent("请审批报销申请！");
                    message.setMsgTime(LocalDateTime.now());
                    message.setIsRead("0");
                    message.setMsgType(Message.MESSAGE_TYPE_6);//报销审批
                    //营销
                    if("d4a1dd4ca37749fc9380f8d61454305c".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_8);//销售总监链接
                    //渠道
                    }else if("3bc4148f06684cf6bebcf80bbbf2971c".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_9);//渠道总监链接
                    //人事
                    }else if("e92f50d1ce07428faf558b7313a149db".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_23);//综合管理经理链接
                    //财务/会计
                    }else if("47e34c25444041d799d04bdfb29a00a4".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_10);//财务
                    //创意策划
                    }else if("4fce54ec7369496cbaa1cd008ea5688b".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_21);//策划总监链接
                    //家装设计
                    }else if("98260d14ef3a4b098fcf0e46766b8bed".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_22);//设计总监链接
                    }//新房事业
                    else if("fe488cafd8174bd789df302cae699263".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_29);//新房总监链接
                    }
                    //二手房事业部
                    else if("80df0136da4c4582a6a113ff97073b8f".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_31);//二手房事业部总监链接
                    }
                    message.setAcceptMan(adminUser.getId());
                    message.setAcceptManName(employee1.getPersonName());
                    messageService.saveMessage(message);
                }
            }
        }
        ljExpenseService.saveLjExpense(ljExpense);
        return ajaxRe(true);
    }
    @RequestMapping("removeLjExpense/{id}")
    public Result removeLjExpense(@PathVariable String id){
        ljExpenseService.removeLjExpense(id);
        return ajaxRe(true);
    }

    //查询渠道整合报销
    @RequestMapping("listExpenseApproval")
    public Result listExpenseApproval(@UIParam("pager") Pager pager,LjExpense ljExpense){
        ljExpense.setOrgUuid("3bc4148f06684cf6bebcf80bbbf2971c");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense,pager);
        List<LjExpense> collect = ljExpenses.stream().map(expense -> {
            AdminUser adminUser = adminUserService.queryUserById(expense.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            expense.setExpenseMan(employee.getPersonName());
            return expense;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("部门", "orgUuid",Organization.class,"orgName"));
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        for( LjExpense ljExpense1:collect){
            if(ljExpense1.getProjectUuid()!=null){
                LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljExpense1.getProjectUuid());
                if(ljProjectInfo!=null&&ljProjectInfo.getProjectName()!=null){
                    ljExpense1.setProjectUuid(ljProjectInfo.getProjectName());
                }
            }
        }
        categories.add(new GridResult.Category("对应项目", "projectUuid"));
        categories.add(new GridResult.Category("其他对应", "otherProject"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("报销状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }
    //审批意见
    @RequestMapping("listApprovalByBusiUuid/{busiId}")
    public Result listApprovalByBusiUuid(@PathVariable String busiId,@UIParam("pager") Pager pager){
        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(busiId,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("审批业务", "checkContent","LJOA.TONG.CHECK_CONTENT"));
        categories.add(new GridResult.Category("审批类型", "checkType","LJOA.TONG.CHECK_TYPE"));
        categories.add(new GridResult.Category("审批结果", "checkResult","LJOA.TONG.CHECK_RESULT"));
//        categories.add(new GridResult.Category("审批人", "checkMan"));
        categories.add(new GridResult.Category("审批时间", "checkTime"));
        categories.add(new GridResult.Category("审批意见", "checkOption"));
        return ajaxReGrid("gdata",categories, approvals,pager);
    }
    //查询营销报销
    @RequestMapping("listExpenseApprovalMarketing")
    public Result listExpenseApprovalMarketing(@UIParam("pager") Pager pager,LjExpense ljExpense){
        ljExpense.setOrgUuid("d4a1dd4ca37749fc9380f8d61454305c");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense,pager);
        List<LjExpense> collect = ljExpenses.stream().map(expense -> {
            AdminUser adminUser = adminUserService.queryUserById(expense.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            expense.setExpenseMan(employee.getPersonName());
            return expense;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("部门", "orgUuid",Organization.class,"orgName"));
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        for( LjExpense ljExpense1:collect){
            if(ljExpense1.getProjectUuid()!=null){
                LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljExpense1.getProjectUuid());
                if(ljProjectInfo!=null&&ljProjectInfo.getProjectName()!=null){
                    ljExpense1.setProjectUuid(ljProjectInfo.getProjectName());
                }
            }
        }
        categories.add(new GridResult.Category("对应项目", "projectUuid"));
        categories.add(new GridResult.Category("其他对应", "otherProject"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("报销状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    //查询报销(月汇总)
    @RequestMapping("listExpense")
    public Result listExpense(@UIParam("pager") Pager pager,Integer month){

        List<LjExpense> ljExpenses = ljExpenseService.queryExpense(month,pager);
        List<LjExpense> collect = ljExpenses.stream().map(expense -> {
            AdminUser adminUser = adminUserService.queryUserById(expense.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            expense.setExpenseMan(employee.getPersonName());
            return expense;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("部门", "orgUuid",Organization.class,"orgName"));
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        for( LjExpense ljExpense1:collect){
            if(ljExpense1.getProjectUuid()!=null){
                LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljExpense1.getProjectUuid());
                if(ljProjectInfo!=null&&ljProjectInfo.getProjectName()!=null){
                    ljExpense1.setProjectUuid(ljProjectInfo.getProjectName());
                }
            }
        }
        categories.add(new GridResult.Category("其他对应", "otherProject"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("报销状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    //渠道已打款报销
    @RequestMapping("listGroupLjExpenseIndex")
    public Result listGroupLjExpenseIndex(LjExpense ljExpense, @UIParam("pager")Pager pager){
        ljExpense.setOrgUuid("3bc4148f06684cf6bebcf80bbbf2971c");
        ljExpense.setStatus("6");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan",Employee.class,"personName"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        for( LjExpense ljExpense1:ljExpenses){
            if(ljExpense1.getProjectUuid()!=null){
                LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljExpense1.getProjectUuid());
                if(ljProjectInfo!=null&&ljProjectInfo.getProjectName()!=null){
                    ljExpense1.setProjectUuid(ljProjectInfo.getProjectName());
                }
            }
        }
        categories.add(new GridResult.Category("其他对应", "otherProject"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljExpenses, pager);
    }

}
