package com.jgp.ljoa.channel.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.security.admin.model.AdminRoleUser;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
@RestController
@RequestMapping("/ljoa/channel/ljHouseSaleInfoApiController")
public class LjHouseSaleInfoApiController extends JGPController {
    @Autowired
    private LjHouseSaleInfoService service;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("saveLjHouseSaleInfo")
    public Result saveLjHouseSaleInfo(LjHouseSaleInfo ljHouseSaleInfo){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        if("2".equals(ljHouseSaleInfo.getChargeStatus()) || "11".equals(ljHouseSaleInfo.getChargeStatus())){
            Approval approval = new Approval();
            approval.setCheckType("1");
            approval.setBusiUuid(ljHouseSaleInfo.getId());
            approval.setCheckTime(LocalDate.now());
            approval.setCheckContent("2");
            approval.setCheckResult("Y");
            approval.setCheckMan(currentAdmin.getId());//审批人
            approvalService.saveApproval(approval);
        }
        if("11".equals(ljHouseSaleInfo.getChargeStatus())){
            String roleId = messageService.queryAdminRoleIdByRoleName("渠道经理");
            List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            for (AdminRoleUser a: adminRoleUsers) {
                AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金审批");
                        message.setMsgContent("请确认发票信息！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_2);//佣金审批
                        message.setLinkUrl(Message.LINK_URL_30);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }
        //渠道总监审核通过向财务发送消息
        if("2".equals(ljHouseSaleInfo.getChargeStatus())){
            String roleId = messageService.queryAdminRoleIdByRoleName("财务");
            String roleId1 = messageService.queryAdminRoleIdByRoleName("会计");
            List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            List<AdminRoleUser> adminRoleUsers1 = messageService.queryAdminUserByRoleId(roleId1);
            adminRoleUsers.addAll(adminRoleUsers1);
            for (AdminRoleUser a: adminRoleUsers) {
                AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金审批");
                        message.setMsgContent("请审批佣金申请！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_2);//佣金审批
                        message.setLinkUrl(Message.LINK_URL_17);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }
        //财务审核通过向副总经理发送消息
        if("4".equals(ljHouseSaleInfo.getChargeStatus())){
            String roleId = messageService.queryAdminRoleIdByRoleName("总经理");
            List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            for (AdminRoleUser a: adminRoleUsers) {
                AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金审批");
                        message.setMsgContent("请审批佣金申请！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_2);//佣金审批
                        message.setLinkUrl(Message.LINK_URL_19);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }
        //副总经理审核通过向总经理发送消息
        if("6".equals(ljHouseSaleInfo.getChargeStatus())){
            String roleId = messageService.queryAdminRoleIdByRoleName("总经理");
            List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            for (AdminRoleUser a: adminRoleUsers) {
                AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金审批");
                        message.setMsgContent("请审批佣金申请！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_2);//佣金审批
                        message.setLinkUrl(Message.LINK_URL_19);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }
        //总经理审核通过向财务发送消息
        if("8".equals(ljHouseSaleInfo.getChargeStatus())){
            String roleId = messageService.queryAdminRoleIdByRoleName("财务");
            String roleId1 = messageService.queryAdminRoleIdByRoleName("会计");
            List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            List<AdminRoleUser> adminRoleUsers1 = messageService.queryAdminUserByRoleId(roleId1);
            adminRoleUsers.addAll(adminRoleUsers1);
            for (AdminRoleUser a: adminRoleUsers) {
                AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金反馈");
                        message.setMsgContent("请进行佣金打款！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_1);//佣金反馈
                        message.setLinkUrl(Message.LINK_URL_20);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }
        //财务 副总  总经理审核不通过向渠道总监发送消息
        if("5".equals(ljHouseSaleInfo.getChargeStatus())
                || "7".equals(ljHouseSaleInfo.getChargeStatus())
                || "9".equals(ljHouseSaleInfo.getChargeStatus())){
            String roleId = messageService.queryAdminRoleIdByRoleName("渠道总监");
            List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            for (AdminRoleUser a: adminRoleUsers) {
                AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金反馈");
                        message.setMsgContent("请查看佣金反馈！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_1);//佣金反馈
                        message.setLinkUrl(Message.LINK_URL_16);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }
        //
        float yMoney=0;//公司佣金
        Float money=ljHouseSaleInfo.getTotalPrice();//成交总价
        LjHouseInfo ljHouseInfo2 = ljHouseInfoService.queryOneLjHouseInfoByid(ljHouseSaleInfo.getHouseUuid());//对应房源
        if("1".equals(ljHouseInfo2.getCompanyChargeType())){
            ljHouseInfo2.setCompanyChargeMoney(money*ljHouseInfo2.getCompanyChargeScale()/100);
        }

        if("1".equals(ljHouseInfo2.getCompanyChargeType())){
            yMoney=(money*ljHouseInfo2.getCompanyChargeScale())/100;
        }else if(ljHouseInfo2.getCompanyChargeMoney()!=null){
            yMoney=ljHouseInfo2.getCompanyChargeMoney();//公司佣金金额
        }
        ljHouseSaleInfo.setCompanyTax(ljHouseInfo2.getCompanyChargeMoney());//公司业绩
        if(ljHouseSaleInfo.getCompanyTaxDeduction() != null){
            if("1".equals(ljHouseSaleInfo.getCompanyTaxDeduction())){//比例
                ljHouseSaleInfo.setCompanyTaxDeductionMoney(Float.parseFloat(ljHouseSaleInfo.getCompanyTaxDeductionScale())/ 100 * ljHouseSaleInfo.getCompanyTax());
            }else{
                ljHouseSaleInfo.setCompanyTaxDeductionMoney(0f);
            }
            ljHouseSaleInfo.setCompanyTaxDeductionAfterMoney(ljHouseSaleInfo.getCompanyTax() -  ljHouseSaleInfo.getCompanyTaxDeductionMoney());
        }


        float sMoney=0;
        //渠道公司不为空
        if(StringUtils.isNotEmpty(ljHouseSaleInfo.getChannelCompany())){
            if("1".equals(ljHouseSaleInfo.getChannelChargeType())){
                ljHouseSaleInfo.getChannelChargeScale();//渠道佣金比例
                ljHouseSaleInfo.setChannelChargeMoney((ljHouseSaleInfo.getChannelChargeScale()*money)/100);
            }
            //渠道扣税比例
            if("2".equals(ljHouseSaleInfo.getChannelTaxType())){
//            ljHouseSaleInfo.getChannelChargeMoney();
//                Float aFloat = Float.valueOf(ljHouseSaleInfo.getChannelTaxScale());
                Float channelTaxScale = ljHouseSaleInfo.getChannelTaxScale();

                ljHouseSaleInfo.getChannelTaxScale();//扣税比例
                Float v1 = ljHouseSaleInfo.getChannelChargeMoney() * channelTaxScale / 100;
                ljHouseSaleInfo.setChannelTaxMoney((ljHouseSaleInfo.getChannelChargeMoney()*channelTaxScale)/100);//扣税金额
                Float v = ljHouseSaleInfo.getChannelChargeMoney() - v1;
                sMoney=v;
                ljHouseSaleInfo.setChannelAfterTaxMoney(v);//税后金额
            }else{
                sMoney=ljHouseSaleInfo.getChannelChargeMoney();
                ljHouseSaleInfo.setChannelAfterTaxMoney(ljHouseSaleInfo.getChannelChargeMoney());//税后金额
            }
        }


        float fMoney = yMoney-sMoney;//公司佣金-渠道税后佣金
        if(ljHouseSaleInfo.getDutyManMoney() != null){
            fMoney=fMoney-ljHouseSaleInfo.getDutyManMoney();
        }
        if(ljHouseSaleInfo.getCompanyTaxDeductionMoney() !=null){
            fMoney=fMoney-ljHouseSaleInfo.getCompanyTaxDeductionMoney();
        }
        float zong= fMoney;
        //案场经理
        if("1".equals(ljHouseSaleInfo.getCaseManagerChargeType())){
            Float aFloat = Float.valueOf(ljHouseSaleInfo.getCaseManagerChargeScale());
            Float v = (fMoney * aFloat)/100;
            ljHouseSaleInfo.setCaseManagerChargeMoney(v);//案场经理提成金额
            zong = zong - ljHouseSaleInfo.getCaseManagerChargeMoney();
        }else if("2".equals(ljHouseSaleInfo.getCaseManagerChargeType())){
            zong = zong - ljHouseSaleInfo.getCaseManagerChargeMoney();
        }
        //渠道经理
        if("1".equals(ljHouseSaleInfo.getChannelManagerChargeType())){
            Float aFloat = Float.valueOf(ljHouseSaleInfo.getChannelManagerChargeScale());
            Float v = (fMoney * aFloat)/100;
            ljHouseSaleInfo.setChannelManagerChargeMoney(v);
            zong = zong -ljHouseSaleInfo.getChannelManagerChargeMoney();
        }else if("2".equals(ljHouseSaleInfo.getChannelManagerChargeType())){
            zong = zong -ljHouseSaleInfo.getChannelManagerChargeMoney();
        }
        //渠道总监
        if("1".equals(ljHouseSaleInfo.getChannelChiefChargeType())){
            Float aFloat = Float.valueOf(ljHouseSaleInfo.getChannelChiefChargeScale());
            Float v = fMoney * aFloat/100;
            ljHouseSaleInfo.setChannelChiefChargeMoney(v);
            zong = zong -ljHouseSaleInfo.getChannelChiefChargeMoney();
        }else if("2".equals(ljHouseSaleInfo.getChannelChiefChargeType())){
            zong = zong -ljHouseSaleInfo.getChannelChiefChargeMoney();
        }

        if("1".equals(ljHouseSaleInfo.getAreaManagerChargeType())){
            Float aFloat = Float.valueOf(ljHouseSaleInfo.getAreaManagerChargeScale());
            Float v = fMoney * aFloat/100;
            ljHouseSaleInfo.setAreaManagerChargeMoney(v);
            zong = zong -ljHouseSaleInfo.getAreaManagerChargeMoney();
        }else if("2".equals(ljHouseSaleInfo.getAreaManagerChargeType())){
            zong = zong -ljHouseSaleInfo.getAreaManagerChargeMoney();
        }

        if("1".equals(ljHouseSaleInfo.getChannelChief())){
            Float aFloat = Float.valueOf(ljHouseSaleInfo.getChannelChiefChargeScale());
            Float v = fMoney * aFloat/100;
            ljHouseSaleInfo.setChannelManagerChargeMoney(v);
            zong = zong -ljHouseSaleInfo.getChannelChargeMoney();
        }else if("2".equals(ljHouseSaleInfo.getChannelChief())){
            zong = zong -ljHouseSaleInfo.getChannelChargeMoney();
        }

       /* if(StringUtils.isNotEmpty(ljHouseSaleInfo.getDutyMan())){
            zong = zong -ljHouseSaleInfo.getDutyManMoney();
        }*/
        if(StringUtils.isNotEmpty(ljHouseSaleInfo.getAdviser())){
            zong = zong -ljHouseSaleInfo.getAdviserMonty();
        }

        Float v = zong;
        ljHouseSaleInfo.setCompanyProfit(v);

        ljHouseSaleInfo.setChannelChargeStatus("1");
        ljHouseSaleInfo.setInsideChargeStatus("1");
        LjHouseSaleInfo ljHouseSaleInfo1 = service.saveLjHouseSaleInfo(ljHouseSaleInfo);
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(ljHouseSaleInfo1.getHouseUuid());//对应房源
        ljHouseInfo.setStatus("2");
        ljHouseInfo.setSaleMoney(ljHouseSaleInfo.getTotalPrice());
        ljHouseInfoService.saveLjHouseInfo(ljHouseInfo);
        return ajaxRe(true);
    }
    @RequestMapping("editljHouseSaleInfoChargeStatus/{id}")
    public Result editljHouseSaleInfoChargeStatus(@PathVariable String id){
        LjHouseSaleInfo ljHouseSaleInfo = service.queryOneLjHouseSaleInfo(id);
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        if("0".equals(ljHouseSaleInfo.getChargeStatus())
                ||"3".equals(ljHouseSaleInfo.getChargeStatus())
                ||"5".equals(ljHouseSaleInfo.getChargeStatus())
                ||"7".equals(ljHouseSaleInfo.getChargeStatus())
                ||"9".equals(ljHouseSaleInfo.getChargeStatus())){
            ljHouseSaleInfo.setChargeStatus("1");//未审批
            //向渠道总监发送消息
            String roleId = messageService.queryAdminRoleIdByRoleName("渠道总监");
            List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            for (AdminRoleUser a: adminRoleUsers) {
                AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金审批");
                        message.setMsgContent("请审批佣金申请！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_2);//佣金审批
                        message.setLinkUrl(Message.LINK_URL_16);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }
        service.saveLjHouseSaleInfo(ljHouseSaleInfo);
        return ajaxRe(true);
    }

    @RequestMapping("editljHouseSaleInfoInvoice/{id}")
    public Result editljHouseSaleInfoInvoice(@PathVariable String id){
        LjHouseSaleInfo ljHouseSaleInfo = service.queryOneLjHouseSaleInfo(id);
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        if("11".equals(ljHouseSaleInfo.getChargeStatus())){
            ljHouseSaleInfo.setChargeStatus("2");//部门审核通过
            String roleId = messageService.queryAdminRoleIdByRoleName("财务");
            String roleId1 = messageService.queryAdminRoleIdByRoleName("会计");
            List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
            List<AdminRoleUser> adminRoleUsers1 = messageService.queryAdminUserByRoleId(roleId1);
            adminRoleUsers.addAll(adminRoleUsers1);
            for (AdminRoleUser a: adminRoleUsers) {
                AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金审批");
                        message.setMsgContent("请审批佣金申请！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_2);//佣金审批
                        message.setLinkUrl(Message.LINK_URL_17);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }
        service.saveLjHouseSaleInfo(ljHouseSaleInfo);
        return ajaxRe(true);
    }

    /*
    * 财务更新渠道房源销售信息的佣金状态
    * */
    @RequestMapping("expensePayChannelHouseSaleInfo")
    private Result expensePayChannelHouseSaleInfo(LjHouseSaleInfo ljHouseSaleInfo){
        ljHouseSaleInfo.setChargeStatus("10");//已结佣
        LjHouseSaleInfo ljHouseSaleInfo1 = service.saveLjHouseSaleInfo(ljHouseSaleInfo);
        return ajaxRe(true);
    }
}

