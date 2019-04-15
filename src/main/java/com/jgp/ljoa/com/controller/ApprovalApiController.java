package com.jgp.ljoa.com.controller;/**
 * Created by Administrator on 2018/7/5.
 */

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.HouseReturnService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.security.admin.model.AdminRoleUser;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.query.QueryFilterList;
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
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@RestController
@RequestMapping("/ljoa/com/approvalApiController")
public class ApprovalApiController extends JGPController {
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private HouseReturnService houseReturnService;

    /*
    * 条件查询审批
    * */
    @RequestMapping("listGroupApprovalByCheckType/{checkType}")
    private Result listGroupApprovalByCheckType(@PathVariable String checkType, Approval approval, @UIParam("pager")Pager pager){
        approval.setCheckType(checkType);
        List<Approval> approvals = approvalService.queryGroupApproval(approval, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元号", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType"));
        return ajaxReGrid("gdata", categories, approvals, pager);
    }

    /*
    * 添加并更新审批
    * */
    @RequestMapping("addAndEditLjApproval")
    private Result addAndEditLjApproval(Approval approval){
        approvalService.saveApproval(approval);
        return ajaxRe(true);
    }

    /*
    * 保存审批
    * */
    @RequestMapping("saveLjApproval")//保存审批
    public Result saveLjApproval(Approval approval){
        AdminUser checkMan = adminUserService.queryUserById(approval.getCheckMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(checkMan.getUsername());
        if("Y".equals(approval.getCheckResult())) {
            //报销
            if("1".equals(approval.getCheckContent())){
                //总监
                if("1".equals(approval.getCheckType())){
                    //向财务发送消息
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
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("报销审批");
                                message.setMsgContent("请审批报销申请！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_6);//报销审批
                                message.setLinkUrl(Message.LINK_URL_10);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
                //财务
                if("2".equals(approval.getCheckType())){
                    LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(approval.getBusiUuid());
                    //2000 <= 报销金额, 发送信息，否则不发送
                    if(1500f <= ljExpense.getMoney()){
                        //向总经理发送消息
                        String roleId = messageService.queryAdminRoleIdByRoleName("总经理");
                        List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
                        for (AdminRoleUser a: adminRoleUsers) {
                            AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                            if(Objects.nonNull(adminUser)){
                                Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                                if(Objects.nonNull(employee1)){
                                    Message message = new Message();
                                    message.setSendMan(checkMan.getId());
                                    message.setSendManName(employee.getPersonName());
                                    message.setMsgTitle("报销审批");
                                    message.setMsgContent("请审批报销申请！");
                                    message.setMsgTime(LocalDateTime.now());
                                    message.setIsRead("0");
                                    message.setMsgType(Message.MESSAGE_TYPE_6);//报销审批
                                    message.setLinkUrl(Message.LINK_URL_12);
                                    message.setAcceptMan(adminUser.getId());
                                    message.setAcceptManName(employee1.getPersonName());
                                    messageService.saveMessage(message);
                                }
                            }
                        }
                    }
                }
                //副总
                if("3".equals(approval.getCheckType())){
                    //向总经理发送消息
                    String roleId = messageService.queryAdminRoleIdByRoleName("总经理");
                    List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
                    for (AdminRoleUser a: adminRoleUsers) {
                        AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                        if(Objects.nonNull(adminUser)){
                            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                            if(Objects.nonNull(employee1)){
                                Message message = new Message();
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("报销审批");
                                message.setMsgContent("请审批报销申请！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_6);//报销审批
                                message.setLinkUrl(Message.LINK_URL_12);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
                //总经理
                if("4".equals(approval.getCheckType())){
                    //向财务发送消息
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
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("报销反馈");
                                message.setMsgContent("请进行报销打款！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_5);//报销反馈
                                message.setLinkUrl(Message.LINK_URL_13);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
            }
            //佣金
            if("2".equals(approval.getCheckContent())){
                //总监
                if("1".equals(approval.getCheckType())){

                }
                //财务
                if("2".equals(approval.getCheckType())){

                }
                //副总
                if("3".equals(approval.getCheckType())){

                }
                //总经理
                if("4".equals(approval.getCheckType())){

                }
            }
            //退房
            if("3".equals(approval.getCheckContent())){
                //总监
                if("1".equals(approval.getCheckType())){
//                    //向财务发送消息
//                    String roleId = messageService.queryAdminRoleIdByRoleName("财务");
//                    String roleId1 = messageService.queryAdminRoleIdByRoleName("会计");
//                    List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
//                    List<AdminRoleUser> adminRoleUsers1 = messageService.queryAdminUserByRoleId(roleId1);
//                    adminRoleUsers.addAll(adminRoleUsers1);
//
//                    for (AdminRoleUser a: adminRoleUsers) {
//                        AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
//                        if(Objects.nonNull(adminUser)){
//                            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
//                            if(Objects.nonNull(employee1)){
//                                Message message = new Message();
//                                message.setSendMan(checkMan.getId());
//                                message.setSendManName(employee.getPersonName());
//                                message.setMsgTitle("退房审批");
//                                message.setMsgContent("请审批退房申请！");
//                                message.setMsgTime(LocalDateTime.now());
//                                message.setIsRead("0");
//                                message.setMsgType(Message.MESSAGE_TYPE_4);//退房审批
//                                message.setLinkUrl(Message.LINK_URL_3);
//                                message.setAcceptMan(adminUser.getId());
//                                message.setAcceptManName(employee1.getPersonName());
//                                messageService.saveMessage(message);
//                            }
//                        }
//                    }
                }
                //财务
                if("2".equals(approval.getCheckType())){
                    //向总经理发送消息
                    String roleId = messageService.queryAdminRoleIdByRoleName("总经理");
                    List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
                    for (AdminRoleUser a: adminRoleUsers) {
                        AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                        if(Objects.nonNull(adminUser)){
                            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                            if(Objects.nonNull(employee1)){
                                Message message = new Message();
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("退房审批");
                                message.setMsgContent("请审批退房申请！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_4);//退房审批
                                message.setLinkUrl(Message.LINK_URL_5);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
                //副总
                if("3".equals(approval.getCheckType())){
                    //向总经理发送消息
                    String roleId = messageService.queryAdminRoleIdByRoleName("总经理");
                    List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
                    for (AdminRoleUser a: adminRoleUsers) {
                        AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                        if(Objects.nonNull(adminUser)){
                            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                            if(Objects.nonNull(employee1)){
                                Message message = new Message();
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("退房审批");
                                message.setMsgContent("请审批退房申请！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_4);//退房审批
                                message.setLinkUrl(Message.LINK_URL_5);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
                //总经理
                if("4".equals(approval.getCheckType())){
                    //向财务发送消息
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
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("退房反馈");
                                message.setMsgContent("请进行退房打款！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_3);//退房审批
                                message.setLinkUrl(Message.LINK_URL_6);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
            }
        }else if ("N".equals(approval.getCheckResult())){
            //报销
            if("1".equals(approval.getCheckContent())){
                //总监 财务  副总 总经理
                LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(approval.getBusiUuid());
                String inputMan = ljExpense.getExpenseMan();
                AdminUser adminUser = adminUserService.queryUserById(inputMan);
                Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                Message message = new Message();
                message.setSendMan(checkMan.getId());
                message.setSendManName(employee.getPersonName());
                message.setAcceptMan(adminUser.getId());
                message.setAcceptManName(employee1.getPersonName());
                message.setMsgTitle("报销反馈");
                message.setMsgContent("请查看报销反馈！");
                message.setMsgTime(LocalDateTime.now());
                message.setIsRead("0");
                message.setMsgType(Message.MESSAGE_TYPE_5);//报销反馈
                message.setLinkUrl(Message.LINK_URL_7);
                messageService.saveMessage(message);
            }
            //佣金
            if("2".equals(approval.getCheckContent())){
                //总监
                if("1".equals(approval.getCheckType())){

                }
                //财务
                if("2".equals(approval.getCheckType())){

                }
                //副总
                if("3".equals(approval.getCheckType())){

                }
                //总经理
                if("4".equals(approval.getCheckType())){

                }
            }
            //退房
            if("3".equals(approval.getCheckContent())){
                //总监 财务  副总 总经理
                HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(approval.getBusiUuid());
                String inputMan = houseReturn.getInputMan();//申请填报人
                AdminUser adminUser = adminUserService.queryUserById(inputMan);
                Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                Message message = new Message();
                message.setSendMan(checkMan.getId());
                message.setSendManName(employee.getPersonName());
                message.setAcceptMan(adminUser.getId());
                message.setAcceptManName(employee1.getPersonName());
                message.setMsgTitle("退房反馈");
                message.setMsgContent("请查看退房反馈！");
                message.setMsgTime(LocalDateTime.now());
                message.setIsRead("0");
                message.setMsgType(Message.MESSAGE_TYPE_3);//退房反馈
                message.setLinkUrl(Message.LINK_URL_1);
                messageService.saveMessage(message);
            }
        }
        Approval approval1 = approvalService.saveApproval(approval);
        return ajaxRe(true);
    }
    @RequestMapping("saveLjApprovalto")//保存审批（备用金）
    public Result saveLjApprovalto(Approval approval){
        AdminUser checkMan = adminUserService.queryUserById(approval.getCheckMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(checkMan.getUsername());
        if("Y".equals(approval.getCheckResult())) {
            //备用金
            if ("4".equals(approval.getCheckContent())) {
                //总监
                if ("1".equals(approval.getCheckType())) {
                    //向财务发送消息
                    String roleId = messageService.queryAdminRoleIdByRoleName("财务");
                    String roleId1 = messageService.queryAdminRoleIdByRoleName("会计");
                    List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
                    List<AdminRoleUser> adminRoleUsers1 = messageService.queryAdminUserByRoleId(roleId1);
                    adminRoleUsers.addAll(adminRoleUsers1);
                    for (AdminRoleUser a : adminRoleUsers) {
                        AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                        if (Objects.nonNull(adminUser)) {
                            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                            if (Objects.nonNull(employee1)) {
                                Message message = new Message();
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("备用金审批");
                                message.setMsgContent("请审批备用金申请！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_8);//备用金审批
                                message.setLinkUrl(Message.LINK_URL_25);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
                //财务
                if ("2".equals(approval.getCheckType())) {

                    //向副总经理发送消息
                    String roleId = messageService.queryAdminRoleIdByRoleName("总经理");
                    List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
                    for (AdminRoleUser a : adminRoleUsers) {
                        AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                        if (Objects.nonNull(adminUser)) {
                            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                            if (Objects.nonNull(employee1)) {
                                Message message = new Message();
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("备用金审批");
                                message.setMsgContent("请审批备用金申请！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_8);//备用金审批
                                message.setLinkUrl(Message.LINK_URL_27);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
                //副总
                if ("3".equals(approval.getCheckType())) {
                    //向总经理发送消息
                    String roleId = messageService.queryAdminRoleIdByRoleName("总经理");
                    List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
                    for (AdminRoleUser a : adminRoleUsers) {
                        AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                        if (Objects.nonNull(adminUser)) {
                            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                            if (Objects.nonNull(employee1)) {
                                Message message = new Message();
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("备用金审批");
                                message.setMsgContent("请审批备用金申请！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_8);
                                message.setLinkUrl(Message.LINK_URL_27);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
                //总经理
                if ("4".equals(approval.getCheckType())) {
                    //向财务发送消息
                    String roleId = messageService.queryAdminRoleIdByRoleName("财务");
                    String roleId1 = messageService.queryAdminRoleIdByRoleName("会计");
                    List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
                    List<AdminRoleUser> adminRoleUsers1 = messageService.queryAdminUserByRoleId(roleId1);
                    adminRoleUsers.addAll(adminRoleUsers1);
                    for (AdminRoleUser a : adminRoleUsers) {
                        AdminUser adminUser = adminUserService.queryUserById(a.getUserId());
                        if (Objects.nonNull(adminUser)) {
                            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                            if (Objects.nonNull(employee1)) {
                                Message message = new Message();
                                message.setSendMan(checkMan.getId());
                                message.setSendManName(employee.getPersonName());
                                message.setMsgTitle("备用金反馈");
                                message.setMsgContent("请进行备用金打款！");
                                message.setMsgTime(LocalDateTime.now());
                                message.setIsRead("0");
                                message.setMsgType(Message.MESSAGE_TYPE_8);//报销反馈
                                message.setLinkUrl(Message.LINK_URL_28);
                                message.setAcceptMan(adminUser.getId());
                                message.setAcceptManName(employee1.getPersonName());
                                messageService.saveMessage(message);
                            }
                        }
                    }
                }
            }
        }

        Approval approval1 = approvalService.saveApproval(approval);
        return ajaxRe(true);
    }
/////////////////总经理///////////////////////////
    /*
     * 总经理查询所有房屋销售信息
     * */

    //佣金已审批（总经理）
    @RequestMapping("listGeneralManagerQueryHouseSaleInfoYES")
    private Result listGeneralManagerQueryHouseSaleInfoYES(LjHouseInfo ljHouseInfo, @UIParam("pager") Pager pager){
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGeneralManagerQueryHouseSaleInfo(ljHouseInfo, pager);
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) {
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    if( "8".equals(ljHouseSaleInfos.get(0).getChargeStatus())
                            || "9".equals(ljHouseSaleInfos.get(0).getChargeStatus())
                            || "10".equals(ljHouseSaleInfos.get(0).getChargeStatus())){
                        return true;
                    }
                }
            }
            if ("2".equals(l.getHouseType())) {
                QueryFilterList list2 = new QueryFilterList();
                list2.addFilter("houseUuid", "eq", l.getId());
                List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryQueryFilterList(list2);
                if (marketingSaleInfos.size() > 0) {
                    if( "8".equals(marketingSaleInfos.get(0).getInsideChargeStatus())
                            || "9".equals(marketingSaleInfos.get(0).getInsideChargeStatus())
                            || "10".equals(marketingSaleInfos.get(0).getInsideChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<String> idList = new ArrayList<>();
        for(LjHouseInfo ljHouseInfo1 : collect){
            idList.add(ljHouseInfo1.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(idList, ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        //categories.add(new GridResult.Category("房主姓名", "ownerName"));
        categories.add(new GridResult.Category("房源类型", "houseType", "LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("所属项目", "projectUuid", LjProjectInfo.class, "projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元号", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(m²)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        //categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata",categories,ljHouseInfos1,pager);
    }

    //佣金待审批（总经理）
    @RequestMapping("listGeneralManagerQueryHouseSaleInfoNO")
    private Result listGeneralManagerQueryHouseSaleInfoNO(LjHouseInfo ljHouseInfo, @UIParam("pager") Pager pager){
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGeneralManagerQueryHouseSaleInfo(ljHouseInfo, pager);
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) {
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    /*4：财务审批通过  6：副总审批通过*/
                    if("4".equals(ljHouseSaleInfos.get(0).getChargeStatus())){
                        return true;
                    }
                }
            }
            if ("2".equals(l.getHouseType())) {
                QueryFilterList list2 = new QueryFilterList();
                list2.addFilter("houseUuid", "eq", l.getId());
                List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryQueryFilterList(list2);
                if (marketingSaleInfos.size() > 0) {
                    /*10：财务审批通过  */
                    if( "10".equals(marketingSaleInfos.get(0).getInsideChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<String> idList = new ArrayList<>();
        for(LjHouseInfo ljHouseInfo1 : collect){
            idList.add(ljHouseInfo1.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(idList, ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        //categories.add(new GridResult.Category("房主姓名", "ownerName"));
        categories.add(new GridResult.Category("房源类型", "houseType", "LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("所属项目", "projectUuid", LjProjectInfo.class, "projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元号", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(m²)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        //categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata",categories,ljHouseInfos1,pager);
    }

    /*
    * 总经理审批渠道佣金
    * */
    @RequestMapping("addGeneralManagerApproval/{houseType}")
    private Result addGeneralManagerApproval(@PathVariable String houseType, Approval approval){
        if("1".equals(houseType)){
            if("Y".equals(approval.getCheckResult())){
                LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfo(approval.getBusiUuid());
                ljHouseSaleInfo.setChargeStatus("8");//总经理审核通过
                ljHouseSaleInfoService.saveLjHouseSaleInfo(ljHouseSaleInfo);
            }else {
                LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfo(approval.getBusiUuid());
                ljHouseSaleInfo.setChargeStatus("9");//总经理审核不通过
                ljHouseSaleInfoService.saveLjHouseSaleInfo(ljHouseSaleInfo);
            }
        }else {
            if("Y".equals(approval.getCheckResult())){
                MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryOneMarketingSaleInfo(approval.getBusiUuid());
                marketingSaleInfo.setChanneChargeStatus("8");//总经理审核通过
                marketingSaleInfo.setInsideChargeStatus("8");//总经理审核通过
                marketingSaleInfoService.saveMarketingSaleInfo(marketingSaleInfo);
            }else {
                MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryOneMarketingSaleInfo(approval.getBusiUuid());
                marketingSaleInfo.setChanneChargeStatus("8");//总经理审核通过
                marketingSaleInfo.setInsideChargeStatus("8");//总经理审核通过
                marketingSaleInfoService.saveMarketingSaleInfo(marketingSaleInfo);
            }
        }
        approvalService.saveApproval(approval);
        return ajaxRe(true);
    }

    /*
    * 总经理查询所有已审批报销
    * */
    @RequestMapping("listGeneralManagerQueryExpenseYES")
    public Result listGeneralManagerQueryExpenseYES(LjExpense ljExpense, @UIParam("pager") Pager pager){
        List<String> list = new ArrayList<>();
        list.add("7");
        list.add("6");
        list.add("11");
        list.add("12");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApprovalByStatus(ljExpense,list, pager);

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
    /*
   * 总经理查询所有待审批报销
   * */
    @RequestMapping("listGeneralManagerQueryExpenseNO")
    public Result listGeneralManagerQueryExpenseNO(LjExpense ljExpense, @UIParam("pager") Pager pager){
        List<String> list = new ArrayList<>();
        list.add("5");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApprovalByStatus(ljExpense,list, pager);
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


///////////////////////////////////////
////////////////副总////////////////////////
    /*
    * 副总查询所有报销
    * */
    //未审批
    @RequestMapping("listDeputyGeneralManagerQueryExpense")
    public Result listDeputyGeneralManagerQueryExpense(LjExpense ljExpense, @UIParam("pager") Pager pager){
        ljExpense.setStatus("4");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense, pager);
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
    //已审批
    @RequestMapping("listDeputyGeneralManagerQueryExpense1")
    public Result listDeputyGeneralManagerQueryExpense1(LjExpense ljExpense, @UIParam("pager") Pager pager){
        List<String> list = new ArrayList<>();
        list.add("5");
        list.add("6");
        list.add("10");
        list.add("11");
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApprovalByStatus(ljExpense,list, pager);
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

//////////////////////////////////////////////////addLjExpense
    //查询审批信息
    @RequestMapping("listApprovalByBusiUuid/{busiId}")
    public Result listApprovalByBusiUuid(@PathVariable String busiId,@UIParam("pager") Pager pager){
        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(busiId,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("审批业务", "checkContent"));
        categories.add(new GridResult.Category("审批类型", "checkType"));
        categories.add(new GridResult.Category("审批结果", "checkResult"));
        categories.add(new GridResult.Category("审批人", "checkMan"));
        categories.add(new GridResult.Category("审批时间", "checkTime"));
        categories.add(new GridResult.Category("审批意见", "checkOption"));
        return ajaxReGrid("gdata",categories, approvals,pager);
    }

    //修改房屋销售信息的状态
    @RequestMapping("editChangeLjHouseSaleInfoStatus")
    private Result editChangeLjHouseSaleInfoStatus(String houseId, String status){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(houseId);
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(ljHouseInfo.getId());
        ljHouseSaleInfo.setChargeStatus(status);
        ljHouseSaleInfoService.saveLjHouseSaleInfo(ljHouseSaleInfo);
        return ajaxRe(true);
    }
}
