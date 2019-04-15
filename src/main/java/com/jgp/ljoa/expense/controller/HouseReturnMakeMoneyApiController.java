package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
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
 * Created by Administrator on 2018/8/1.
 */
/*
作者  SSF
时间   2018/8/1
*/
@RestController
@RequestMapping("/ljoa/expense/houseReturnMakeMoneyApiController")
public class HouseReturnMakeMoneyApiController extends JGPController {
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerInfoService customerInfoService;

    @RequestMapping("listAllHouseReturnMakeMoney")
    public Result listAllHouseReturnMakeMoney(HouseReturn houseReturn, @UIParam("pager")Pager pager){
        List<String> list= new ArrayList<>();
        list.add("6");
        list.add("7");
//        houseReturn.setApprovalStatus("7");
        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturnInfo(list,houseReturn, pager);
        List<HouseReturn> collect = houseReturns.stream().map(houseReturn1 -> {
            AdminUser adminUser = adminUserService.queryUserById(houseReturn1.getInputMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            houseReturn1.setInputMan(employee.getPersonName());
            return houseReturn1;
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

    @RequestMapping("addHouseReturn")
    public Result addHouseReturn(HouseReturn houseReturn){
        houseReturn.setApprovalStatus("6");
        if("2".equals(houseReturn.getMoneyStatus())){
            CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(houseReturn.getHouseUuid());
            customerInfoService.removeOneCustomerInfo(customerInfo.getId());
        }
        houseReturnService.saveHouseReturn(houseReturn);
        return ajaxRe(true);
    }
}
