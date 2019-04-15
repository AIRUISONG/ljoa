package com.jgp.ljoa.marketing.controller;

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
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/5 0005.
 */
@RestController
@RequestMapping("/ljoa/marketing/marketingSaleInfoApiController")
public class MarketingSaleInfoApiController extends JGPController {
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private CustomerInfoService customerInfoService;
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
    private LjProjectInfoService ljProjectInfoService;

    //查询已售房源
    @RequestMapping("listLjHouseInfoByStatusInfoSold/{id}")
    public Result listLjHouseInfoByStatusInfoSold(@UIParam("pager")Pager pager,@PathVariable String id,LjHouseInfo ljHouseInfo){
        ljHouseInfo.setProjectUuid(id);
        List<LjHouseInfo> marketingSaleInfos = marketingSaleInfoService.queryLjHouseInfo(ljHouseInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, marketingSaleInfos, pager);
    }

    //保存销售信息营销里销售总监设置公司利润
    @RequestMapping("saveMarketingSaleInfoLR")
    public Result saveMarketingSaleInfoLR(MarketingSaleInfo marketingSaleInfo){
        marketingSaleInfo.setGrossProfit(marketingSaleInfo.getGrossProfit()+marketingSaleInfo.getPrepositionEarn());
        marketingSaleInfoService.saveMarketingSaleInfo(marketingSaleInfo);
        return ajaxRe(true);
    }

    //保存销售信息
    @RequestMapping("saveMarketingSaleInfo")
    public Result saveMarketingSaleInfo(MarketingSaleInfo marketingSaleInfo){
        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryGroupMarketingChargeInfo(marketingSaleInfo.getHouseUuid(),null,null);
        //当渠道佣金状态和内部佣金状态相同,但不是同时为"12"时，佣金表的状态同步修改
        if(!"12".equals(marketingSaleInfo.getInsideChargeStatus())
                && marketingSaleInfo.getInsideChargeStatus().equals(marketingSaleInfo.getChanneChargeStatus())){
            marketingChargeInfos.forEach(marketingChargeInfo -> {
                marketingChargeInfo.setChargeStatus(marketingSaleInfo.getChanneChargeStatus());
                marketingChargeInfoService.saveMarketingChargeInfo(marketingChargeInfo);
            });
        //"12"为已结佣
        }else if("12".equals(marketingSaleInfo.getInsideChargeStatus())){
            marketingChargeInfos.forEach(marketingChargeInfo -> {
                marketingChargeInfo.setChargeStatus(marketingSaleInfo.getInsideChargeStatus());
                if(Objects.isNull(marketingChargeInfo.getChargeSendDate())){
                    marketingChargeInfo.setChargeSendDate(LocalDate.now());
                }
                marketingChargeInfoService.saveMarketingChargeInfo(marketingChargeInfo);
            });
        }
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());

        //财务 副总  总经理审核不通过
        if(("11".equals(marketingSaleInfo.getChanneChargeStatus()) && "11".equals(marketingSaleInfo.getInsideChargeStatus()))
                || ("7".equals(marketingSaleInfo.getChanneChargeStatus()) && "7".equals(marketingSaleInfo.getInsideChargeStatus()))
                || ("9".equals(marketingSaleInfo.getChanneChargeStatus()) && "9".equals(marketingSaleInfo.getInsideChargeStatus()))){
            //向销售总监发送消息
            String roleId = messageService.queryAdminRoleIdByRoleName("销售总监");
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
                        message.setLinkUrl(Message.LINK_URL_15);
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }
        }else if("10".equals(marketingSaleInfo.getChanneChargeStatus()) && "10".equals(marketingSaleInfo.getInsideChargeStatus())){
            //财务审核通过向副总经理发送消息
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
        }else if("6".equals(marketingSaleInfo.getChanneChargeStatus()) && "6".equals(marketingSaleInfo.getInsideChargeStatus())){
            //副总经理审核通过向总经理发送消息
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
        }else if("8".equals(marketingSaleInfo.getChanneChargeStatus()) && "8".equals(marketingSaleInfo.getInsideChargeStatus())){
            //总经理审核通过向财务发送消息
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
        marketingSaleInfoService.saveMarketingSaleInfo(marketingSaleInfo);
        return ajaxRe(true);
    }

    //申请审批时修改佣金状态
    @RequestMapping("saveMarketingSaleInfoChargeStatus/{hid}")
    public Result saveMarketingSaleInfoChargeStatus(@PathVariable String hid){
        CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(hid);
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(hid);

        if("0".equals(marketingSaleInfo.getInsideChargeStatus()) || "0".equals(marketingSaleInfo.getChanneChargeStatus())
                || "11".equals(marketingSaleInfo.getInsideChargeStatus()) || "11".equals(marketingSaleInfo.getChanneChargeStatus())
                || "3".equals(marketingSaleInfo.getInsideChargeStatus()) || "3".equals(marketingSaleInfo.getChanneChargeStatus())
                || "5".equals(marketingSaleInfo.getInsideChargeStatus()) || "5".equals(marketingSaleInfo.getChanneChargeStatus())
                || "7".equals(marketingSaleInfo.getInsideChargeStatus()) || "7".equals(marketingSaleInfo.getChanneChargeStatus())
                || "9".equals(marketingSaleInfo.getInsideChargeStatus()) || "9".equals(marketingSaleInfo.getChanneChargeStatus())
                ){
            //修改此房源的所有佣金信息为总监已审批
            List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid,null,null);
//            List<String> list = new ArrayList<>();
//            list.add("5");//渠道
//            List<MarketingChargeInfo> marketingChargeInfos1 = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid,list,null);//判断是不是渠道
            Float money = 0.0f;//（公司内部人员+优惠金额）或渠道消费钱数
//            if (marketingChargeInfos1.size()>0){
//                for(MarketingChargeInfo m:marketingChargeInfos){
//                    m.setChargeStatus("4");
//                    money+=m.getChargeMoney();
//                }
//            }else {
                for(MarketingChargeInfo m:marketingChargeInfos){
                    m.setChargeStatus("4");
                    money+=m.getChargeMoney();
                    marketingChargeInfoService.saveMarketingChargeInfo(m);
                }
                if(Objects.nonNull(customerInfo.getDiscountMoney())){
                    money+=customerInfo.getDiscountMoney();
                }
//            }
            //修改销售信息表

            marketingSaleInfo.setPureProfit(marketingSaleInfo.getGrossProfit()-money);//纯利润=毛利润-公司内部-渠道
            marketingSaleInfo.setChanneChargeStatus("4");//渠道结佣总监已通过
            marketingSaleInfo.setInsideChargeStatus("4");//内部结佣总监已通过
            MarketingSaleInfo marketingSaleInfo1 = marketingSaleInfoService.saveMarketingSaleInfo(marketingSaleInfo);
            if("4".equals(marketingSaleInfo1.getChanneChargeStatus())&&"4".equals(marketingSaleInfo1.getInsideChargeStatus())){
                Approval approval = new Approval();
                approval.setCheckType("1");
                approval.setBusiUuid(marketingSaleInfo1.getId());
                approval.setCheckTime(LocalDate.now());
                approval.setCheckContent("2");
                approval.setCheckResult("Y");
                approvalService.saveApproval(approval);
            }
            //向财务发送消息
            AdminUser currentAdmin = adminUserService.getCurrentAdmin();
            Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
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
            return ajaxRe(true);
        }else{
            return ajaxRe(false);
        }
    }
    //经理填佣金时查询房源信息
    @RequestMapping("listLjHouseInfoByStatusInfoSoldManager/{id}")
    public Result listLjHouseInfoByStatusInfoSoldManager(@UIParam("pager")Pager pager, @PathVariable String id, LjHouseInfo ljHouseInfo){
        ljHouseInfo.setProjectUuid(id);
        List<LjHouseInfo> marketingSaleInfos = marketingSaleInfoService.queryLjHouseInfoAssistant(ljHouseInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, marketingSaleInfos, pager);
    }
    //总监填佣金时查询的房源信息已审批
    @RequestMapping("listLjHouseInfoManager1/{id}")
    public Result listLjHouseInfoManager1(@UIParam("pager")Pager pager,@PathVariable String id,LjHouseInfo ljHouseInfo){
        ljHouseInfo.setProjectUuid(id);
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByStatus("2", ljHouseInfo, pager);//已售房源的

        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("2".equals(l.getHouseType())) {
                QueryFilterList list2 = new QueryFilterList();
                list2.addFilter("houseUuid", "eq", l.getId());
                List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryQueryFilterList(list2);
                if (marketingSaleInfos.size() > 0) {
                    if(!"0".equals(marketingSaleInfos.get(0).getInsideChargeStatus())
                            && !"1".equals(marketingSaleInfos.get(0).getChanneChargeStatus())
                            && !"2".equals(marketingSaleInfos.get(0).getChanneChargeStatus())
                            && !"3".equals(marketingSaleInfos.get(0).getChanneChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for(LjHouseInfo l:collect){
            list.add(l.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(list,ljHouseInfo, pager);
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryAllLjProjectInfo();
        ljHouseInfos1 = ljHouseInfos1.stream().map(ll -> {
            for(LjProjectInfo l : ljProjectInfos){
                if(ll.getProjectUuid().equals(l.getId())){
                    ll.setProjectUuid(l.getProjectName());
                }
            }
            return ll;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("溢价金额(元)", "premium"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos1, pager);
    }

    //总监填佣金时查询的房源信息未审批
    @RequestMapping("listLjHouseInfoManager/{id}")
    public Result listLjHouseInfoManager(@UIParam("pager")Pager pager,@PathVariable String id,LjHouseInfo ljHouseInfo){
        ljHouseInfo.setProjectUuid(id);
        List<LjHouseInfo> marketingSaleInfos = marketingSaleInfoService.queryLjHouseInfoManager(ljHouseInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("溢价金额(元)", "premium"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, marketingSaleInfos, pager);
    }

//    //总监填佣金时修改房源信息（还有客户的）
//    @RequestMapping("editLjHouseInfoManager")
//    public Result editLjHouseInfoManager(){
//
//    }
}
