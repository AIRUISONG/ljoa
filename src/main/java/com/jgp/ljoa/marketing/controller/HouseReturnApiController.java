package com.jgp.ljoa.marketing.controller;/**
 * Created by Administrator on 2018/7/23.
 */

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.HouseReturnService;
import com.jgp.security.admin.model.AdminRoleUser;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 时间   2018/7/23
 */
@RestController
@RequestMapping("/ljoa/marketing/houseReturnApiController")
public class HouseReturnApiController extends JGPController {
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private LjProjectInfoService service;

    /*
    * 查询可退房的房源列表
    * */
    @RequestMapping("listAllHouseInfoForHouseReturn")
    public Result listAllHouseInfoForHouseReturn(LjHouseInfo ljHouseInfo, @UIParam("pager") Pager pager){
        List<LjHouseInfo> ljHouseInfoList = ljHouseInfoService.queryAllHouseInfoForHouseReturn(ljHouseInfo,pager);
    /*    List<String> uuid = new ArrayList<>();
        if(ljHouseInfoList.size() > 0){
            for(int i = 0 ; i <= ljHouseInfoList.size() ; i ++){
                uuid.add(ljHouseInfoList.get(i).getId());
            }
        }
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(uuid, ljHouseInfo, pager);*/
        List<GridResult.Category> categories = new ArrayList<>();
//        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
//        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
//        categories.add(new GridResult.Category("定金金额(元)", "saleMoney"));
//        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfoList, pager);
    }

    /*
    * 添加退房申请
    * */
    @RequestMapping("addHouseReturn")
    public Result addHouseReturn(HouseReturn houseReturn){
        HouseReturn houseReturn1 = houseReturnService.saveHouseReturn(houseReturn);
        return ajaxRe(true);
    }


    /*
    * 提交退房申请
    * */
    @RequestMapping("editCommitHouseReturn")
    public Result editCommitHouseReturn(String id){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);
        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(houseReturn.getId(), null);
        approvalService.removeApproval(approvals);
        houseReturn.setApprovalStatus("2");//总监审核
        houseReturnService.saveHouseReturn(houseReturn);
        //向总监发送消息
        String roleId = messageService.queryAdminRoleIdByRoleName("销售总监");
        List<AdminRoleUser> adminRoleUsers = messageService.queryAdminUserByRoleId(roleId);
        //当前登陆用户
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
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
                    message.setMsgTitle("退房审批");
                    message.setMsgContent("请审批退房申请！");
                    message.setMsgTime(LocalDateTime.now());
                    message.setIsRead("0");
                    message.setMsgType(Message.MESSAGE_TYPE_4);//退房审批
                    message.setLinkUrl(Message.LINK_URL_2);
                    message.setAcceptMan(adminUser.getId());
                    message.setAcceptManName(employee1.getPersonName());
                    messageService.saveMessage(message);
                }
            }
        }
        return ajaxRe(true);
    }



    /*
    * 删除退房申请
    * */
    @RequestMapping("removeOneHouseReturn")
    public Result removeOneHouseReturn(String id){
        houseReturnService.removeOneHouseReturn(id);
        return ajaxRe(true);
    }

    @RequestMapping("listAllHouseReturn")//查询退房信息
    public Result listAllHouseReturn( HouseReturn houseReturn,@UIParam("pager")Pager pager){
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
        categories.add(new GridResult.Category("审核状态", "approvalStatus","LJ_HOUSE_RETURN.APPROVAL_STATUS"));
        categories.add(new GridResult.Category("退款状态", "moneyStatus","LJ_HOUSE_RETURN.MONEY_STATUS"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    //销售总监需要审批的退房信息（待审批）
    @RequestMapping("listHouseReturnSaleDirector")
    public Result listHouseReturnSaleDirector(HouseReturn houseReturn,@UIParam("pager") Pager pager){

        houseReturn.setApprovalStatus("2");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();//登录人
        //对应项目
        List<LjProjectInfo> ljChannelProjects = service.queryGroupLjProjectInfoByProjectTypeByMan("2",currentAdmin.getId(),pager);

        List<LjHouseInfo> ljHouseInfo = ljHouseInfoService.queryGroupLjHouseInfoByHouseType("2");//所有房源
        List< String> ljHouseInfoList=new ArrayList<>();//对应房源id
        for(LjHouseInfo jj: ljHouseInfo){
            for(LjProjectInfo ljProjectInfo : ljChannelProjects){
                if(jj.getProjectUuid().equals(ljProjectInfo.getId())){
                    ljHouseInfoList.add(jj.getId());
                }
            }
        }

        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturnInfoByHouseUuid(ljHouseInfoList,houseReturn, pager);
        List<HouseReturn> list1 = houseReturns.stream().map(houseReturn2 -> {
            AdminUser adminUser = adminUserService.queryUserById(houseReturn2.getInputMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            houseReturn2.setInputMan(employee.getPersonName());
            return houseReturn2;
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
        return ajaxReGrid("gdata",categories,list1,pager);
    }
    //销售总监需要审批的退房信息（已审批）
    @RequestMapping("listHouseReturnSaleDirector1")
    public Result listHouseReturnSaleDirector1(HouseReturn houseReturn,@UIParam("pager") Pager pager){
        List<String> list=new ArrayList<>();
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturnInfo(list,houseReturn, pager);
        List<HouseReturn> list1 = houseReturns.stream().map(houseReturn2 -> {
            AdminUser adminUser = adminUserService.queryUserById(houseReturn2.getInputMan());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            houseReturn2.setInputMan(employee.getPersonName());
            return houseReturn2;
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
        return ajaxReGrid("gdata",categories,list1,pager);
    }


    //提交申请，营销总监审批
    @RequestMapping("editCommitHouseReturnSaleDirector")
    public Result editCommitHouseReturnSaleDirector(Approval approval){
        //当前登陆用户
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        //员工姓名
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(approval.getBusiUuid());
        if("Y".equals(approval.getCheckResult())){
            houseReturn.setApprovalStatus("3");//申请通过，财务审批
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
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("退房审批");
                        message.setMsgContent("请审批退房申请！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_4);//退房审批
                        message.setLinkUrl(Message.LINK_URL_3);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }else{
//            houseReturn.setApprovalStatus("1");//申请不通过，申请填报
            String inputMan = houseReturn.getInputMan();
            AdminUser adminUser = adminUserService.queryUserById(inputMan);
            Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            Message message = new Message();
            message.setSendMan(currentAdmin.getId());
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
        houseReturnService.saveHouseReturn(houseReturn);
        approvalService.saveApproval(approval);
        return ajaxRe(true);
    }

}
