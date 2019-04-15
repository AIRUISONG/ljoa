package com.jgp.ljoa.expense.controller;

import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.util.TimeInterval;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.model.LjMoneyBorrow;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.expense.service.LjMoneyBorrowService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.OrganizationService;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/7/5.
 */
/*
作者  SSF
时间   2018/7/5
*/

@Controller
@RequestMapping("/ljoa/expense/ljExpenseController")
public class LjExpenseController extends JGPController {

    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private LjMoneyBorrowService ljMoneyBorrowService;


    /*
    * 根据adminUser的id查询报销信息
    * */
    @RequestMapping("listAllLjExpenseByAdminUserId")
    public String listAllLjExpenseByAdminUserId(){
        return page("listAllLjExpenseByAdminUserId");
    }


    @RequestMapping("addLjExpense/{id}")//跳转添加
    public String addLjExpense(@PathVariable String id,Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();//员工（报销人）
        Relation relation=relationService.queryOrgByEmployeeId(currentAdmin.getId());//关系表
        LjExpense ljExpense=new LjExpense();
        ljExpense.setExpenseMan(currentAdmin.getId());
        ljExpense.setReceiver(currentAdmin.getId());
        ljExpense.setApplyTime(LocalDate.now());
        ljExpense.setStatus("0");
        ljExpense.setOrgUuid(relation.getMainUuid());//部门id
        model.addAttribute("typeUuid",ljExpense.getOrgUuid());
        reForm(model,"fdata",ljExpense);
        return  page("addLjExpense");
    }

    @RequestMapping("showLjExpense/{id}")//跳转详情
    public String showLjExpense(@PathVariable String id,Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);
        AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        reForm(model,"fdata",ljExpense);
        model.addAttribute("expenseManName", employee.getPersonName());
        return  page("showLjExpense");
    }

    @RequestMapping("editLjExpense/{id}")//跳转修改
    public String editLjExpense(@PathVariable String id,Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);
        AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        model.addAttribute("uuid",ljExpense.getId());
        reForm(model,"fdata",ljExpense);
        if("0".equals(ljExpense.getStatus())||"8".equals(ljExpense.getStatus())
                ||"9".equals(ljExpense.getStatus())||"10".equals(ljExpense.getStatus())
                ||"11".equals(ljExpense.getStatus())){
            model.addAttribute("flag","1");//可修改
        }else {
            model.addAttribute("flag","0");//不可修改
        }
        model.addAttribute("expenseManName", employee.getPersonName());
        return  page("editLjExpense");
    }
    //跳转报销审批信息查询界面
    @RequestMapping("listExpenseApproval")
    public String listExpenseApproval(){
        return page("listExpenseApproval");
    }

    //佣金审批意见查询（渠道整合）
    @RequestMapping("listApprovalByBusiUuid/{hid}")
    public String listApprovalByBusiUuid(@PathVariable String hid,Model model){
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(hid);
        model.addAttribute("busiId",ljHouseSaleInfo.getId());
        return page("listApprovalByBusiUuid");
    }

    //佣金审批意见查询（营销）
    @RequestMapping("listApprovalByBusiUuId/{hid}")
    public String listApprovalByBusiUuId(@PathVariable String hid,Model model){
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(hid);
        if(Objects.isNull(marketingSaleInfo.getId())){
            model.addAttribute("busiId","1");
        }else{
            model.addAttribute("busiId",marketingSaleInfo.getId());
        }
        return page("listApprovalByBusiUuid");
    }
    //报销审批意见查询
    @RequestMapping("listApprovalByBUuid/{hid}")
    public String listApprovalByBUuid(@PathVariable String hid,Model model){
        model.addAttribute("busiId",hid);
        return page("listApprovalByBusiUuid");
    }
    //查询里报销审批信息的营销
    @RequestMapping("listExpenseApprovalMarketing")
    public String listExpenseApprovalMarketing(){
        return page("listExpenseApprovalMarketing");
    }

    //月汇总报销审批
    @RequestMapping("listExpenseMonthlySummary")
    public String listExpenseMonthlySummary(){
        return page("listExpenseMonthlySummary");
    }


    //查看报销进度
    @RequestMapping("showApprovalProgress/{id}")
    public String showApprovalProgress(@PathVariable String id,Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);//对应报销
        //时间间隔
        String s0 = "";
        String s1 = "";
        String s2 = "";

        //流程时间
        String date0 = ljExpense.getCreateDatetime().toString().replace("T", " ");
        String date1 = "";
        String date2 = "";
        String date4 = "";
        //当前用户
        AdminUser currentAdmin = adminUserService.queryUserById(ljExpense.getExpenseMan());
        Relation relation = relationService.queryRelationByAdminUserId(currentAdmin.getId());
        //财务
        if("47e34c25444041d799d04bdfb29a00a4".equals(relation.getMainUuid())){
            Approval approval4 = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(), "4", "1");
            if(Objects.nonNull(approval4)){
                LocalDateTime modifyDatetime2 = ljExpense.getCreateDatetime();//提交时间
                LocalDateTime modifyDatetime4 = approval4.getModifyDatetime();//总经理审核时间
                date4 = approval4.getModifyDatetime().toString().replace("T", " ");
                Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime4);
                s2 = TimeInterval.timeInterval(duration2);
            }
            List<String> list = new ArrayList<>();
            list.add(s0);
            list.add(s1);
            list.add(s2);
            List<String> dateList = new ArrayList<>();
            dateList.add(date0);
            dateList.add(date1);
            dateList.add(date2);
            dateList.add(date4);
            model.addAttribute("timeList", list);
            model.addAttribute("dateList", dateList);
            model.addAttribute("status",ljExpense.getStatus());//状态
            return page("showApprovalProgressForCaiWu");
        }
        else {
                Approval approval1 = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(), "1", "1");
                Approval approval2 = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(), "2", "1");
                Approval approval4 = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(), "4", "1");
                if(Objects.nonNull(approval4)){
                    LocalDateTime modifyDatetime0 = ljExpense.getCreateDatetime();
                    LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                    LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
                    LocalDateTime modifyDatetime4 = approval4.getModifyDatetime();
                    date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                    date2 = approval2.getModifyDatetime().toString().replace("T", " ");
                    date4 = approval4.getModifyDatetime().toString().replace("T", " ");
                    Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                    s0 = TimeInterval.timeInterval(duration0);
                    Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                    s1 = TimeInterval.timeInterval(duration1);
                    Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime4);
                    s2 = TimeInterval.timeInterval(duration2);
                }else {
                    if(Objects.nonNull(approval2)){
                        LocalDateTime modifyDatetime0 = ljExpense.getCreateDatetime();
                        LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                        LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
                        date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                        date2 = approval2.getModifyDatetime().toString().replace("T", " ");
                        Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                        s0 = TimeInterval.timeInterval(duration0);
                        Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                        s1 = TimeInterval.timeInterval(duration1);
                    }else {
                        if(Objects.nonNull(approval1)){
                            LocalDateTime modifyDatetime0 = ljExpense.getCreateDatetime();
                            LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                            date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                            Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                            s0 = TimeInterval.timeInterval(duration0);
                        }
                    }
                }
                List<String> list = new ArrayList<>();
                list.add(s0);
                list.add(s1);
                list.add(s2);
                List<String> dateList = new ArrayList<>();
                dateList.add(date0);
                dateList.add(date1);
                dateList.add(date2);
                dateList.add(date4);
                model.addAttribute("timeList", list);
                model.addAttribute("dateList", dateList);
                model.addAttribute("status",ljExpense.getStatus());//状态
                return page("showApprovalProgress");
        }

    }


    //查看备用金进度
    @RequestMapping("showApprovalProgressLjMoneyBorrow/{id}")
    public String showApprovalProgressLjMoneyBorrow(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        //时间间隔
        String s0 = "";
        String s1 = "";
        String s2 = "";
        String s3 = "";
        //流程时间
        String date0 = ljMoneyBorrow.getCreateDatetime().toString().replace("T", " ");
        String date1 = "";
        String date2 = "";
        String date3 = "";
        String date4 = "";
//        return page("showApprovalProgress");
        //当前用户
        AdminUser currentAdmin = adminUserService.queryUserById(ljMoneyBorrow.getBorrowMan());
        Relation relation = relationService.queryRelationByAdminUserId(currentAdmin.getId());

            //财务
        if("47e34c25444041d799d04bdfb29a00a4".equals(relation.getMainUuid())){
//            Approval approval3 = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(), "3", "1");
            Approval approval4 = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(), "4", "4");
            if(Objects.nonNull(approval4)){
                LocalDateTime modifyDatetime2 = ljMoneyBorrow.getCreateDatetime();
//                LocalDateTime modifyDatetime3 = approval3.getModifyDatetime();
                LocalDateTime modifyDatetime4 = approval4.getModifyDatetime();
//               date3 = approval3.getModifyDatetime().toString().replace("T", " ");
                date4 = approval4.getModifyDatetime().toString().replace("T", " ");
                Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime4);
                s2 = TimeInterval.timeInterval(duration2);
//                Duration duration3 = Duration.between(modifyDatetime3,modifyDatetime4);
//                s3 = TimeInterval.timeInterval(duration3);
            }
          /*  else {
                if(Objects.nonNull(approval3)){
                    LocalDateTime modifyDatetime2 = ljMoneyBorrow.getCreateDatetime();
                    LocalDateTime modifyDatetime3 = approval4.getModifyDatetime();
                    date3 = approval3.getModifyDatetime().toString().replace("T", " ");
                    Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime3);
                    s2 = TimeInterval.timeInterval(duration2);
                }
            }*/
            List<String> list = new ArrayList<>();
            list.add(s0);
            list.add(s1);
            list.add(s2);
//            list.add(s3);
            List<String> dateList = new ArrayList<>();
            dateList.add(date0);
            dateList.add(date1);
            dateList.add(date2);
//            dateList.add(date3);
            dateList.add(date4);
            model.addAttribute("timeList", list);
            model.addAttribute("dateList", dateList);
            model.addAttribute("status",ljMoneyBorrow.getStatus());//状态
            return page("showApprovalProgressForCaiWu");
        }else {
                Approval approval1 = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(), "1", "4");
                Approval approval2 = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(), "2", "4");
//            Approval approval3 = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(), "3", "4");
                Approval approval4 = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(), "4", "4");
                if(Objects.nonNull(approval4)){
                    LocalDateTime modifyDatetime0 = ljMoneyBorrow.getCreateDatetime();
                    LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                    LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
//                LocalDateTime modifyDatetime3 = approval3.getModifyDatetime();
                    LocalDateTime modifyDatetime4 = approval4.getModifyDatetime();
                    date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                    date2 = approval2.getModifyDatetime().toString().replace("T", " ");
//                date3 = approval3.getModifyDatetime().toString().replace("T", " ");
                    date4 = approval4.getModifyDatetime().toString().replace("T", " ");
                    Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                    s0 = TimeInterval.timeInterval(duration0);
                    Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                    s1 = TimeInterval.timeInterval(duration1);
                    Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime4);
                    s2 = TimeInterval.timeInterval(duration2);
//                Duration duration3 = Duration.between(modifyDatetime3,modifyDatetime4);
//                s3 = TimeInterval.timeInterval(duration3);
                }else {

                    if(Objects.nonNull(approval2)){
                        LocalDateTime modifyDatetime0 = ljMoneyBorrow.getCreateDatetime();
                        LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                        LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
                        date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                        date2 = approval2.getModifyDatetime().toString().replace("T", " ");
                        Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                        s0 = TimeInterval.timeInterval(duration0);
                        Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                        s1 = TimeInterval.timeInterval(duration1);
                    }else {
                        if(Objects.nonNull(approval1)){
                            LocalDateTime modifyDatetime0 = ljMoneyBorrow.getCreateDatetime();
                            LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                            date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                            Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                            s0 = TimeInterval.timeInterval(duration0);
                        }
                    }

                }
                List<String> list = new ArrayList<>();
                list.add(s0);
                list.add(s1);
                list.add(s2);
//            list.add(s3);
                List<String> dateList = new ArrayList<>();
                dateList.add(date0);
                dateList.add(date1);
                dateList.add(date2);
//            dateList.add(date3);
                dateList.add(date4);
                model.addAttribute("timeList", list);
                model.addAttribute("dateList", dateList);
                model.addAttribute("status",ljMoneyBorrow.getStatus());//状态
                return page("showApprovalProgress");
        }
    }
}
