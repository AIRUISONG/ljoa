package com.jgp.ljoa.com.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
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
 * Created by Administrator on 2018/7/23.
 */
/*
作者  SSF
时间   2018/7/23
*/
@RestController
@RequestMapping("/ljoa/com/houseReturnZJLApiController")
public class HouseReturnZJLApiController extends JGPController {
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("listGroupHouseReturnZJLYES")//查询总经理已审批的退房信息
    public Result listGroupHouseReturnZJLYES(HouseReturn houseReturn, @UIParam("pager")Pager pager){
//        houseReturn.setApprovalStatus("5");//总经理审批
        List<String> list = new ArrayList<>();
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");

        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturnInfo(list,houseReturn, pager);
        List<HouseReturn> collect = houseReturns.stream().map(houseReturn1 -> {
            AdminUser adminUser = adminUserService.queryUserById(houseReturn1.getInputMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            houseReturn1.setInputMan(employee.getPersonName());
            return houseReturn1;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("姓名", "applyName"));
        categories.add(new GridResult.Category("申请填报人", "inputMan"));
        categories.add(new GridResult.Category("退款金额", "returnMoney"));
        categories.add(new GridResult.Category("申请日期", "applyDate"));
        categories.add(new GridResult.Category("审核状态", "approvalStatus","LJOA.EXPENSE"));
        categories.add(new GridResult.Category("退款状态", "moneyStatus","LJ_HOUSE_RETURN.MONEY_STATUS"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    @RequestMapping("listGroupHouseReturnZJLNO")//查询总经理待审批的退房信息
    public Result listGroupHouseReturnZJLNO(HouseReturn houseReturn, @UIParam("pager")Pager pager){
        houseReturn.setApprovalStatus("5");//总经理审批
        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturn(houseReturn, pager);
        List<HouseReturn> collect = houseReturns.stream().map(houseReturn1 -> {
            AdminUser adminUser = adminUserService.queryUserById(houseReturn1.getInputMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            houseReturn1.setInputMan(employee.getPersonName());
            return houseReturn1;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("姓名", "applyName"));
        categories.add(new GridResult.Category("申请填报人", "inputMan"));
        categories.add(new GridResult.Category("退款金额", "returnMoney"));
        categories.add(new GridResult.Category("申请日期", "applyDate"));
        categories.add(new GridResult.Category("审核状态", "approvalStatus","LJOA.EXPENSE"));
        categories.add(new GridResult.Category("退款状态", "moneyStatus","LJ_HOUSE_RETURN.MONEY_STATUS"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    @RequestMapping("saveHouseReturnZJL")
    public Result saveHouseReturnZJL(HouseReturn houseReturn){
        Approval approval = approvalService.queryOneApprovalByBusiUuid(houseReturn.getId());
        if("Y".equals(approval.getCheckResult())){
            houseReturn.setApprovalStatus("6");
        }else if("N".equals(approval.getCheckResult())){
            houseReturn.setApprovalStatus("4");
        }
        houseReturnService.saveHouseReturn(houseReturn);
        return ajaxRe(true);
    }
}
