package com.jgp.ljoa.reporter.controller;


import com.jgp.attachment.model.FileInfo;
import com.jgp.attachment.service.DocService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Organization;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.OrganizationService;
import com.jgp.ljoa.reporter.common.Num2Rmb;
import com.jgp.reporter.ReporterFormat;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2018/8/9.
 */
/*
作者  SSF
时间   2018/8/9
*/
@Controller
@RequestMapping("/ljoa/reporter/ljExpenseReportController")
public class LjExpensereportController extends JGPController {
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private DocService docService;
    @Autowired
    private ApprovalService approvalService;

    @RequestMapping("ljExpensereport1/{id}")//报销报表1
    public String ljExpensereport1(@PathVariable String id,Model model) throws IOException {
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);
        Organization organization = organizationService.queryOneOrganization(ljExpense.getOrgUuid());//部门信息
        AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());//报销人
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
        AdminUser adminUser2 = adminUserService.queryUserById(ljExpense.getReceiver());//报销人
        Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());

        Map map=new HashMap();
        map.put("department",organization.getOrgName());//部门
        map.put("Year", ljExpense.getApplyTime().getYear());
        map.put("month", ljExpense.getApplyTime().getMonthValue());
        map.put("date",ljExpense.getApplyTime().getDayOfMonth());//日期
        List<FileInfo> fileInfos = docService.queryFiles(ljExpense.getId(),"com.jgp.ljoa.expense.model.LjExpense");//对应附件数量
        if(Objects.isNull(ljExpense.getFileNum())){
            map.put("fileNum",fileInfos.size());//附件数量
        }else{
            map.put("fileNum",ljExpense.getFileNum());//附件数量
        }
        map.put("expenseMan",employee.getPersonName());//报销人
        map.put("receiver",employee1.getPersonName());//收款人
        map.put("payUse",ljExpense.getPayUse());//付款用途
        Num2Rmb num=new Num2Rmb();
        map.put("money",num.toHanStr(ljExpense.getMoney()));//大写钱数
        map.put("moneyMin",ljExpense.getMoney());//数字钱数
        map.put("remarks","");//备注
        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(ljExpense.getId(), null);
        int label = 0;
        for (Approval approval:approvals){
            if("1".equals(approval.getCheckType())){
                AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                map.put("BMZG",employeeBM.getPersonName());//部门主管
                label = 1;
            }
            if("2".equals(approval.getCheckType())){
                AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                map.put("CW",employeeBM.getPersonName());//财务审批人
                if(label == 0){
                    map.put("BMZG",employeeBM.getPersonName());//部门主管
                }
            }
/*            if("3".equals(approval.getCheckType())){
                AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                map.put("FZJL",employeeBM.getPersonName());//副总经理姓名
            }*/
            if("4".equals(approval.getCheckType())){
                AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                map.put("ZJL",employeeBM.getPersonName());//总经理姓名
            }

        }

        InputStream i =  getClass().getClassLoader().getResourceAsStream("static/images/ljlogo.jpg");

        try {

            map.put("image", i);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStream i2 =  getClass().getClassLoader().getResourceAsStream("static/images/ysh.png");
        try {

            map.put("subimage", i2);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  jasperReporter(model,null,map,"LjExpensereport1", ReporterFormat.PDF);
    }
    @RequestMapping("ljExpensereport2/{id}")//报销报表2
    public String ljExpensereport2(@PathVariable String id,Model model) throws IOException {
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);
        Organization organization = organizationService.queryOneOrganization(ljExpense.getOrgUuid());//部门信息
        AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());//报销人
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
        AdminUser adminUser2 = adminUserService.queryUserById(ljExpense.getReceiver());//报销人
        Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
        Map map=new HashMap();
        map.put("department",organization.getOrgName());//部门
        map.put("Year", ljExpense.getApplyTime().getYear());
        map.put("month", ljExpense.getApplyTime().getMonthValue());
        map.put("date",ljExpense.getApplyTime().getDayOfMonth());//日期
        map.put("fileNum",ljExpense.getFileNum());//附件数量
        map.put("expenseMan",employee.getPersonName());//报销人
        map.put("receiver",employee1.getPersonName());//收款人
        map.put("payUse",ljExpense.getPayUse());
        Num2Rmb num=new Num2Rmb();
        map.put("money",num.toHanStr(ljExpense.getMoney()));//大写钱数
        map.put("moneyMin",ljExpense.getMoney());//数字钱数
        map.put("remarks","");

        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(ljExpense.getId(), null);
        int label = 0;
        for (Approval approval:approvals){
            if("1".equals(approval.getCheckType())){
                AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                map.put("BMZG",employeeBM.getPersonName());//部门主管
                label = 1;
            }
            if("2".equals(approval.getCheckType())){
                AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                map.put("CW",employeeBM.getPersonName());//财务审批人
                if(label == 0){
                    map.put("BMZG",employeeBM.getPersonName());//部门主管
                }
            }

        }
        map.put("QKR",employee.getPersonName());


        InputStream i =  getClass().getClassLoader().getResourceAsStream("static/images/ljlogo.jpg");
        try {
            map.put("image", i);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }


        InputStream i2 =  getClass().getClassLoader().getResourceAsStream("static/images/ysh.png");
        try {

            map.put("subimage", i2);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  jasperReporter(model,null,map,"LjExpensereport2", ReporterFormat.PDF);
    }



}
