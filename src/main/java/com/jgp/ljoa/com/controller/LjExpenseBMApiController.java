package com.jgp.ljoa.com.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.com.model.Approval;
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

/**
 * Created by Administrator on 2018/7/7.
 */
/*
作者  SSF
时间   2018/7/7
*/
@RestController
@RequestMapping("/ljoa/com/ljExpenseBMApiController")
public class LjExpenseBMApiController extends JGPController {
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LjProjectInfoService service;

    /*
    * 营销总监审批报销
    * */
    //待审批
    @RequestMapping("listAllLjExpenseBMYX")
    public Result listAllLjExpenseBMYX(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setOrgUuid("d4a1dd4ca37749fc9380f8d61454305c");//营销部
        ljExpense.setStatus("2");//部门主管状态

        AdminUser currentAdmin = adminUserService.getCurrentAdmin();//登录人

        List<LjProjectInfo> ljChannelProjects = service.queryGroupLjProjectInfoByProjectType("2",pager);
        //对应项目
        List<LjProjectInfo> collect= ljChannelProjects.stream().filter(lp -> {
            if (currentAdmin.getId().equals(lp.getChiefUuid())) {
                return false;
            } else if (currentAdmin.getId().equals(lp.getPrjDutyMan())) {
                return false;
            } else if (currentAdmin.getId().equals(lp.getManagerUuid())) {
                return false;
            } else if (currentAdmin.getId().equals(lp.getCaseManager())) {
                return false;
            } else {
                return true;
            }
        }).collect(Collectors.toList());
        List<String> list=new ArrayList<>();//对应项目id集
        if(collect.size()>0){
            for (LjProjectInfo ljProjectInfo:collect ) {
                list.add(ljProjectInfo.getId());
            }
        }

        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApprovalByOrgUuid(ljExpense,list,pager);
        ljExpenses= ljExpenses.stream().map(ljExpense2 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense2.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense2.setExpenseMan(employee.getPersonName());
            return ljExpense2;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljExpenses, pager);
    }
    //已审批
    @RequestMapping("listAllLjExpenseBMYX1")
    public Result listAllLjExpenseBMYX1(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setOrgUuid("d4a1dd4ca37749fc9380f8d61454305c");
        //ljExpense.setStatus("1");//提交申请
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense,pager);
        List<LjExpense> collect = ljExpenses.stream().filter(ljExpense1 -> {
//            Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense1.getId(), "1", "1");
            //总监审核过的
            if ( !"0".equals(ljExpense1.getStatus())&&!"2".equals(ljExpense1.getStatus())) {
                return true;
            } else {
                return false;
            }
        }).map(ljExpense2 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense2.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense2.setExpenseMan(employee.getPersonName());
            return ljExpense2;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    /*
    * 渠道总监审批报销(已审批的)
    * */
    @RequestMapping("listAllLjExpenseBMQDYES")
    public Result listAllLjExpenseBMQDYES(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setOrgUuid("3bc4148f06684cf6bebcf80bbbf2971c");
        //ljExpense.setStatus("1");//提交申请
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense,pager);
        List<LjExpense> collect = ljExpenses.stream().filter(ljExpense1 -> {
            Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense1.getId(), "1", "1");
            if (!"0".equals(ljExpense1.getStatus())&&!"1".equals(ljExpense1.getStatus())&&!"2".equals(ljExpense1.getStatus())){
                return true;
            }else {
                return false;
            }
        }).map(ljExpense2 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense2.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense2.setExpenseMan(employee.getPersonName());
            return ljExpense2;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    /*
 * 渠道总监审批报销(未审批的)
 * */
    @RequestMapping("listAllLjExpenseBMQDNO")
    public Result listAllLjExpenseBMQDNO(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setOrgUuid("3bc4148f06684cf6bebcf80bbbf2971c");
        //ljExpense.setStatus("1");//提交申请
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense,pager);
        List<LjExpense> collect = ljExpenses.stream().filter(ljExpense1 -> {
            Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense1.getId(), "1", "1");
            if ("2".equals(ljExpense1.getStatus())) {
                return true;
            }else {
                return false;
            }
        }).map(ljExpense2 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljExpense2.getExpenseMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljExpense2.setExpenseMan(employee.getPersonName());
            return ljExpense2;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("报销人", "expenseMan"));
        categories.add(new GridResult.Category("报销种类", "expenseType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("申请日期", "applyTime"));
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }


    /**
     * 新房事业部审批报销（未审批）*/
    @RequestMapping("listAllLjExpenseBMXFNO")
    public Result listAllLjExpenseBMXFNO(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setStatus("2");
        ljExpense.setOrgUuid("fe488cafd8174bd789df302cae699263");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense,pager);

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
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    /**
     * 新房事业部审批报销（已审批）*/
    @RequestMapping("listAllLjExpenseBMXFYES")
    public Result listAllLjExpenseBMXFYES(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setOrgUuid("fe488cafd8174bd789df302cae699263");
        List<String> uuid = new ArrayList<>();
        uuid.add("0");
        uuid.add("1");
        uuid.add("2");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApprovalByNotStatus(ljExpense,uuid,pager);
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
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    /**
     * 二手房事业部审批报销（未审批）*/
    @RequestMapping("listAllLjExpenseBMERSNO")
    public Result listAllLjExpenseBMERSNO(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setStatus("2");
        ljExpense.setOrgUuid("80df0136da4c4582a6a113ff97073b8f");
        //ljExpense.setStatus("1");//提交申请
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense,pager);
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
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    /**
     * 二手房事业部审批报销（已审批）*/
    @RequestMapping("listAllLjExpenseBMERSYES")
    public Result listAllLjExpenseBMERSYES(LjExpense ljExpense,@UIParam("pager") Pager pager){
        ljExpense.setOrgUuid("80df0136da4c4582a6a113ff97073b8f");
        //ljExpense.setStatus("1");//提交申请
        List<String> uuid = new ArrayList<>();
        uuid.add("0");
        uuid.add("1");
        uuid.add("2");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApprovalByNotStatus(ljExpense,uuid,pager);

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
        categories.add(new GridResult.Category("金额(元)", "money"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

}
