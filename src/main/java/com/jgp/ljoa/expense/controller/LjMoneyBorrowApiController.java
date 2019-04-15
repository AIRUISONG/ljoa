package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.expense.model.LjMoneyBorrow;
import com.jgp.ljoa.expense.model.LjMoneyPayRecord;
import com.jgp.ljoa.expense.service.LjMoneyBorrowService;
import com.jgp.ljoa.expense.service.LjMoneyPayRecordService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.OrganizationService;
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

/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/
@RestController
@RequestMapping("/ljoa/expense/LjMoneyBorrowApiController")
public class LjMoneyBorrowApiController extends JGPController {

    @Autowired
    private LjMoneyBorrowService ljMoneyBorrowService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private LjMoneyPayRecordService ljMoneyPayRecordService;
    @Autowired
    private MessageService messageService;




    @RequestMapping("saveLjMoneyBorrow")//保存
    public Result saveLjMoneyBorrow(LjMoneyBorrow ljMoneyBorrow ){
        //打款时，向报销人发送消息
        if("1".equals(ljMoneyBorrow.getPayStatus())){
            AdminUser currentAdmin = adminUserService.getCurrentAdmin();
            Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
            Message message = new Message();
            message.setSendMan(currentAdmin.getId());
            message.setSendManName(employee.getPersonName());
            message.setMsgTitle("备用金打款");
            message.setMsgContent("备用金已打款！");
            message.setMsgTime(LocalDateTime.now());
            message.setIsRead("0");
            message.setMsgType(Message.MESSAGE_TYPE_8);
            message.setAcceptMan(ljMoneyBorrow.getBorrowMan());
            //报销人
            AdminUser adminUser = adminUserService.queryUserById(ljMoneyBorrow.getBorrowMan());
            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            message.setAcceptManName(employee1.getPersonName());
            messageService.saveMessage(message);
        }
        LjMoneyBorrow ljMoneyBorrow1 = ljMoneyBorrowService.saveLjMoneyBorrow(ljMoneyBorrow);
        return ajaxRe(true).addData("id",ljMoneyBorrow1.getId());
    }
    @RequestMapping("editLjMoneyBorrow")//提交
    public Result editLjMoneyBorrow(LjMoneyBorrow ljMoneyBorrow){
        //当前登陆用户
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Relation relation = relationService.queryRelationByAdminUserId(currentAdmin.getId());

        List<AdminRoleUser> adminRoleUsers = new ArrayList<>();
        //营销提交报销，向销售总监发送消息
        if("d4a1dd4ca37749fc9380f8d61454305c".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("销售总监");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljMoneyBorrow.setStatus("2");
            //渠道提交报销，向渠道总监发送消息
        }else if("3bc4148f06684cf6bebcf80bbbf2971c".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("渠道总监");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljMoneyBorrow.setStatus("2");
            //人事提交报销，向财务/会计发送消息
        }else if("e92f50d1ce07428faf558b7313a149db".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("综合管理经理");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljMoneyBorrow.setStatus("2");
            //财务/会计提交报销，向副总经理发送消息
        }else if("47e34c25444041d799d04bdfb29a00a4".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("副总经理");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljMoneyBorrow.setStatus("4");
            //创意策划提交报销，向策划总监发送消息
        }else if("4fce54ec7369496cbaa1cd008ea5688b".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("策划总监");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljMoneyBorrow.setStatus("2");
            //家装设计提交报销，向设计总监发送消息
        }else if("98260d14ef3a4b098fcf0e46766b8bed".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("设计总监");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljMoneyBorrow.setStatus("2");
            //新房事业部提交
        }else if("fe488cafd8174bd789df302cae699263".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("新房事业部总经理");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljMoneyBorrow.setStatus("2");
            //二手房事业部提交备用金，向二手房事业部总经理
        }else if("80df0136da4c4582a6a113ff97073b8f".equals(relation.getMainUuid())){
            String roleId = messageService.queryAdminRoleIdByRoleName("二手房事业部总经理");
            adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            ljMoneyBorrow.setStatus("2");
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
                    message.setMsgTitle("备用金审批");
                    message.setMsgContent("请审批备用金申请！");
                    message.setMsgTime(LocalDateTime.now());
                    message.setIsRead("0");
                    message.setMsgType(Message.MESSAGE_TYPE_8);//备用金审批
                    //营销
                    if("d4a1dd4ca37749fc9380f8d61454305c".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_24);//销售总监链接
                        //渠道
                    }else if("3bc4148f06684cf6bebcf80bbbf2971c".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_24);//渠道总监链接
                        //人事
                    }else if("e92f50d1ce07428faf558b7313a149db".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_24);//综合管理经理链接
                        //财务/会计
                    }else if("47e34c25444041d799d04bdfb29a00a4".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_25);//财务链接
                        //创意策划
                    }else if("4fce54ec7369496cbaa1cd008ea5688b".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_24);//策划总监链接
                        //家装设计
                    }else if("98260d14ef3a4b098fcf0e46766b8bed".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_24);//设计总监链接
                        //新房事业部
                    }else if("fe488cafd8174bd789df302cae699263".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_24);//设计总监链接
                    }
                    //二手房事业部
                    else if("80df0136da4c4582a6a113ff97073b8f".equals(relation.getMainUuid())){
                        message.setLinkUrl(Message.LINK_URL_24);//二手房事业部总监链接
                    }
                    message.setAcceptMan(adminUser.getId());
                    message.setAcceptManName(employee1.getPersonName());
                    messageService.saveMessage(message);
                }
            }
        }
        if(ljMoneyBorrow.getBorrowOrg()!=null && "47e34c25444041d799d04bdfb29a00a4".equals(ljMoneyBorrow.getBorrowOrg())){
            ljMoneyBorrow.setStatus("3");//财务部
        }else{
            ljMoneyBorrow.setStatus("2");//普通部门
        }
        LjMoneyBorrow ljMoneyBorrow1 = ljMoneyBorrowService.saveLjMoneyBorrow(ljMoneyBorrow);
        return ajaxRe(true);
    }
    @RequestMapping("listLjMoneyBorrowByAdminUser")//根据登陆人信息查询备用金list
    public Result listLjMoneyBorrowByAdminUser(@UIParam("pager") Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        LjMoneyBorrow ljMoneyBorrow=new LjMoneyBorrow();
        ljMoneyBorrow.setBorrowMan(currentAdmin.getId());
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrow(ljMoneyBorrow, pager);
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(ljMoneyBorrow1 -> {
            AdminUser adminUser = adminUserService.queryUserById(ljMoneyBorrow1.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            ljMoneyBorrow1.setBorrowMan(employee.getPersonName());
            return ljMoneyBorrow1;
        }).collect(Collectors.toList());

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }
    @RequestMapping("removeLjMoneyBorrowById/{id}")//删除
    public Result removeLjMoneyBorrowById(@PathVariable String id){
        ljMoneyBorrowService.removeLjMoneyBorrow(id);
        return ajaxRe(true);
    }
    @RequestMapping("finishMoney")//销账
    public Result finishMoney(String id){
        LjMoneyBorrow ljMoneyBorrow1 = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        LjMoneyPayRecord ljMoneyPayRecord=new LjMoneyPayRecord();
        ljMoneyPayRecord.setBorrowUuid(ljMoneyBorrow1.getId());
        List<LjMoneyPayRecord> ljMoneyPayRecords = ljMoneyPayRecordService.queryGroupLjMoneyPayRecord(ljMoneyPayRecord, null);
        String status="";
        if(ljMoneyPayRecords.size()==0){
            //请填写支出明细
            status="0";
        }
        if("6".equals(ljMoneyBorrow1.getStatus()) && ljMoneyPayRecords.size()!=0){
            ljMoneyBorrow1.setStatus("12");//已销账
            status="1";
        }
        if((!"6".equals(ljMoneyBorrow1.getStatus()))&&(!"7".equals(ljMoneyBorrow1.getStatus()))){
            //尚未通过审核
            status="2";
        }
        if("12".equals(ljMoneyBorrow1.getStatus()) ){
            status="3";//已销账
        }
        System.out.println(status);
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.saveLjMoneyBorrow(ljMoneyBorrow1);
        return ajaxRe(true).addData("status",status);
    }

    @RequestMapping("listLjMoneyBorrowByOrgUuidNO")//部门主管未审核数据
    public Result listLjMoneyBorrowByOrgUuidNO(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();//当前登录人
        Relation relation = relationService.queryRelationByAdminUserId(currentAdmin.getId());
        ljMoneyBorrow.setBorrowOrg(relation.getMainUuid());//部门id
        ljMoneyBorrow.setStatus("2");//部门主管审核数据
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrow(ljMoneyBorrow, pager);//对应部门对应状态数据
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);

    }
    @RequestMapping("listLjMoneyBorrowByOrgUuidYES")//部门主管已审核数据
    public Result listLjMoneyBorrowByOrgUuidYES(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();//当前登录人
        Relation relation = relationService.queryRelationByAdminUserId(currentAdmin.getId());
        ljMoneyBorrow.setBorrowOrg(relation.getMainUuid());//部门id
        List<String> list=new ArrayList<>();
        list.add("0");list.add("1");list.add("2");
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrowByStatus(ljMoneyBorrow,list, pager);//对应部门对应状态数据
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, collect, pager);

    }


    @RequestMapping("listLjMoneyBorrowByFinanceYES")//财务已审核数据
    public Result listLjMoneyBorrowByFinanceYES(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
        List<String> list=new ArrayList<>();
        list.add("0");list.add("1");list.add("2");list.add("3");
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrowByStatus(ljMoneyBorrow,list, pager);//对应部门对应状态数据
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljMoneyBorrows, pager);

    }
    @RequestMapping("listLjMoneyBorrowByFinanceNO")//财务未审核数据
    public Result listLjMoneyBorrowByFinanceNO(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
        ljMoneyBorrow.setStatus("3");//财务审核数据
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrow(ljMoneyBorrow, pager);
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljMoneyBorrows, pager);
    }


    @RequestMapping("listLjMoneyBorrowByFZYES")//副总经理已审核数据
    public Result listLjMoneyBorrowByFZYES(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
        List<String> list=new ArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrowByStatus(ljMoneyBorrow,list, pager);//对应部门对应状态数据
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljMoneyBorrows, pager);

    }
    @RequestMapping("listLjMoneyBorrowByFZNO")//副总经理未审核数据
    public Result listLjMoneyBorrowByFZNO(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
        ljMoneyBorrow.setStatus("4");//财务审核数据
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrow(ljMoneyBorrow, pager);
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljMoneyBorrows, pager);
    }


    @RequestMapping("listLjMoneyBorrowByZYES")//总经理已审核数据
    public Result listLjMoneyBorrowByZYES(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
        List<String> list=new ArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrowByStatus(ljMoneyBorrow,list, pager);//对应部门对应状态数据
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljMoneyBorrows, pager);

    }
    @RequestMapping("listLjMoneyBorrowByZNO")//总经理未审核数据
    public Result listLjMoneyBorrowByZNO(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
        ljMoneyBorrow.setStatus("5");//
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrow(ljMoneyBorrow, pager);
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljMoneyBorrows, pager);
    }


    @RequestMapping("listLjMoneyBorrowMakeMoney")//财务打款数据
    public Result listLjMoneyBorrowMakeMoney(LjMoneyBorrow ljMoneyBorrow,@UIParam("pager") Pager pager){
//        ljMoneyBorrow.setStatus("7");//
        List list=new ArrayList();
        list.add("0");  list.add("1");  list.add("2");
        list.add("3");  list.add("4");  list.add("5");
        list.add("8");  list.add("9");  list.add("10");  list.add("11");
        List<LjMoneyBorrow> ljMoneyBorrows = ljMoneyBorrowService.queryGroupLjMoneyBorrowByStatus(ljMoneyBorrow,list, pager);
        List<LjMoneyBorrow> collect = ljMoneyBorrows.stream().map(lm -> {
            AdminUser adminUser = adminUserService.queryUserById(lm.getBorrowMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            lm.setBorrowMan(employee.getPersonName());
            return lm;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("借款人", "borrowMan"));
        categories.add(new GridResult.Category("借款金额", "borrowMoney"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        categories.add(new GridResult.Category("状态", "status","LJOA.EXPENSE"));
        return ajaxReGrid("gdata", categories, ljMoneyBorrows, pager);
    }
}
