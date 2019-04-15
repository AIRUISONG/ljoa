package com.jgp.ljoa.reporter.controller;

import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.expense.model.LjMoneyBorrow;
import com.jgp.ljoa.expense.model.LjMoneyPayRecord;
import com.jgp.ljoa.expense.service.LjMoneyBorrowService;
import com.jgp.ljoa.expense.service.LjMoneyPayRecordService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Organization;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.OrganizationService;
import com.jgp.ljoa.reporter.bean.MoneyPayRecord;
import com.jgp.ljoa.reporter.common.Num2Rmb;
import com.jgp.reporter.ReporterFormat;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
项目  ljoa
作者  SSF
时间   2018/11/29
*/
@Controller
@RequestMapping("/ljoa/reporter/LjMoneyBorrowReportController")
public class LjMoneyBorrowReportController extends JGPController {

    @Autowired
    private LjMoneyBorrowService ljMoneyBorrowService;
    @Autowired
    private LjMoneyPayRecordService ljMoneyPayRecordService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AttributeService attributeService;


    @RequestMapping("ljMoneyBorrowJasper/{id}")//备用金报表
    public String ljMoneyBorrowJasper(@PathVariable String id, Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);//对应备用金信息
        Map map=new HashMap<>();
        if(ljMoneyBorrow!=null){
            if(ljMoneyBorrow.getBorrowOrg() != null){
                Organization organization = organizationService.queryOneOrganization(ljMoneyBorrow.getBorrowOrg());//部门信息
                map.put("BORROWORG",organization.getOrgName());//部门
            }
            if(ljMoneyBorrow.getBorrowMan() != null){
                AdminUser adminUser = adminUserService.queryUserById(ljMoneyBorrow.getBorrowMan());
                Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                map.put("BORROWMAN",employee.getPersonName());//申请人
            }
            if(ljMoneyBorrow.getBorrowDate()!= null){
                map.put("BORROWDATE",ljMoneyBorrow.getBorrowDate());//申请时间
            }
            if(ljMoneyBorrow.getBorrowUses()!= null){
                map.put("BORROWUSES",ljMoneyBorrow.getBorrowUses());//申请用途
            }
            Num2Rmb num=new Num2Rmb();
            if(ljMoneyBorrow.getBorrowMoney()!= null){
                map.put("MAXBORROWMONEY",num.toHanStr(ljMoneyBorrow.getBorrowMoney()));//大写金额
                map.put("BORROWMONEY",ljMoneyBorrow.getBorrowMoney());//小写金额
            }
            List<Approval> approvals = approvalService.queryApprovalByBusiUuid(ljMoneyBorrow.getId(), null);
            for (Approval approval:approvals){
                if("1".equals(approval.getCheckType())){
                    AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                    Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                    map.put("BMZG",employeeBM.getPersonName());//部门主管
                    map.put("BMZGDATE",approval.getCheckTime());
                }
                if("2".equals(approval.getCheckType())){
                    AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                    Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                    map.put("CW",employeeBM.getPersonName());//财务审批人
                    map.put("CWDATE",approval.getCheckTime());
                }
/*                if("3".equals(approval.getCheckType())){
                    AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                    Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                    map.put("FZJL",employeeBM.getPersonName());//副总经理姓名
                    map.put("FZJLDATE",approval.getCheckTime());
                }*/
                if("4".equals(approval.getCheckType())){
                    AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                    Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                    map.put("ZJL",employeeBM.getPersonName());//总经理姓名
                    map.put("ZJLDATE",approval.getCheckTime());
                }
            }
            if("12".equals(ljMoneyBorrow.getStatus())){
                map.put("XZ","已销账");
                map.put("XZDATE",ljMoneyBorrow.getModifyDatetime());
            }else{
                map.put("XZ","未销账");
                map.put("XZDATE","");
            }
            map.put("BZ","");
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

        return jasperReporter(model,null,map,"ljMoneyBorrowJasper", ReporterFormat.PDF);
    }

    @RequestMapping("ljMoneyPayRecordJasper/{id}")//备用金明细报表
    public String ljMoneyPayRecordJasper(@PathVariable String id, Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);//对应备用金信息
        LjMoneyPayRecord ljMoneyPayRecord=new LjMoneyPayRecord();
        ljMoneyPayRecord.setBorrowUuid(ljMoneyBorrow.getId());
        List<LjMoneyPayRecord> ljMoneyPayRecords = ljMoneyPayRecordService.queryGroupLjMoneyPayRecord(ljMoneyPayRecord, null);
        Map map=new HashMap<>();
        Float money=0f;
        List<MoneyPayRecord> list=new ArrayList<>();

        for(LjMoneyPayRecord li:ljMoneyPayRecords){
            money+=li.getPayMoney();
            MoneyPayRecord moneyPayRecord=new MoneyPayRecord();
            moneyPayRecord.setPayDate(li.getPayDate());
            moneyPayRecord.setPayItem(li.getPayItem());
            AdminUser adminUser = adminUserService.queryUserById(li.getPayMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            moneyPayRecord.setPayMan(employee.getPersonName());
            moneyPayRecord.setPayMoney(li.getPayMoney());
            Organization organization = organizationService.queryOneOrganization(li.getPayOrg());//部门信息
            moneyPayRecord.setPayOrg(organization.getOrgName());
            moneyPayRecord.setRemark(li.getRemark());
            List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.EXPENSE_TYPE");
            for (Attribute aa:attributes) {
                if(aa.getValue().equals(li.getPayType())){
                    moneyPayRecord.setPayType(aa.getLabel());
                }
            }
            list.add(moneyPayRecord);

        }
        if(list.size()<=0){
            MoneyPayRecord moneyPay=new MoneyPayRecord();
            list.add(moneyPay);
        }

        if(ljMoneyBorrow.getBorrowMoney()!= null){
            map.put("BORROWMONEY",ljMoneyBorrow.getBorrowMoney());//小写金额
            map.put("SurplusMoney",ljMoneyBorrow.getBorrowMoney()-money);//结余金额
        }
        if(ljMoneyBorrow.getBorrowDate()!= null){
            map.put("BORROWDATE",ljMoneyBorrow.getBorrowDate());//申请时间
        }
        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(ljMoneyBorrow.getId(), null);
        for (Approval approval:approvals) {
            if ("1".equals(approval.getCheckType())) {
                AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                map.put("BMZG", employeeBM.getPersonName());//部门主管
                map.put("BMZGDATE", approval.getCheckTime());
            }
            if ("2".equals(approval.getCheckType())) {
                AdminUser adminUserBM = adminUserService.queryUserById(approval.getCheckMan());
                Employee employeeBM = employeeService.queryOneEmployeeByAccount(adminUserBM.getUsername());
                map.put("CW", employeeBM.getPersonName());//财务审批人
                map.put("CWDATE", approval.getCheckTime());
            }
        }
        AdminUser adminUser = adminUserService.queryUserById(ljMoneyBorrow.getBorrowMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
        map.put("JBR",employee.getPersonName());
        map.put("JBRDATE",ljMoneyBorrow.getBorrowDate());


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


        return jasperReporter(model,list,map,"ljMoneyPayRecordJasper", ReporterFormat.PDF);
    }
}
