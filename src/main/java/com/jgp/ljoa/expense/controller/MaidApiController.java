package com.jgp.ljoa.expense.controller;/**
 * Created by Administrator on 2018/7/8.
 */

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/8
 */
@RestController
@RequestMapping("/ljoa/expense/maidApiController")
public class MaidApiController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MessageService messageService;

    /*
    * 财务查询可结佣的渠道房源信息
    * */
    @RequestMapping("listExpenseQueryMaidHouseInfo")
    private Result listExpenseQueryMaidHouseInfo(LjHouseInfo ljHouseInfo, @UIParam("pager")Pager pager){
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryExpenseMaidHouseInfo(ljHouseInfo, pager);
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(ljHouseInfo1 -> {
            if ("1".equals(ljHouseInfo1.getHouseType())) {
                LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(ljHouseInfo1.getId());
                if ("8".equals(ljHouseSaleInfo.getChargeStatus()) || "10".equals(ljHouseSaleInfo.getChargeStatus())) {
                    return true;
                }
            } else {
                MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByHouseUuid(ljHouseInfo1.getId()).get(0);
                if ("8".equals(marketingSaleInfo.getInsideChargeStatus())
                        || "8".equals(marketingSaleInfo.getChanneChargeStatus())
                        || "12".equals(marketingSaleInfo.getInsideChargeStatus())
                        || "12".equals(marketingSaleInfo.getChanneChargeStatus())) {
                    return true;
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
        categories.add(new GridResult.Category("房源归属", "houseType", "LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("所属项目", "projectUuid", LjProjectInfo.class, "projectName"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(m²)", "roomArea"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos1, pager);
    }

    /*
    * 财务为渠道销售信息结佣
    * */
    @RequestMapping("editExpenseMaidChannelHouseSaleInfo")
    private Result editExpenseMaidChannelHouseSaleInfo(LjHouseSaleInfo ljHouseSaleInfo){
        //当渠道和内部都结佣后，房源销售信息的佣金状态改为"10"
        if(Objects.nonNull(ljHouseSaleInfo.getChannelCompany())){
            if("2".equals(ljHouseSaleInfo.getChannelChargeStatus()) && "2".equals(ljHouseSaleInfo.getInsideChargeStatus())){
                ljHouseSaleInfo.setChargeStatus("10");
                ljHouseSaleInfo.setChargeSendDate(LocalDate.now());
            }
        }else{
            if("2".equals(ljHouseSaleInfo.getInsideChargeStatus())){
                ljHouseSaleInfo.setChargeStatus("10");
                ljHouseSaleInfo.setChargeSendDate(LocalDate.now());

            }
        }
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        List<String> userId = new ArrayList<>();
//        userId.add(ljHouseSaleInfo.getDutyManUuid());//个人
        userId.add(ljHouseSaleInfo.getCaseManager());//案场经理
        userId.add(ljHouseSaleInfo.getChannelManager());//渠道经理
        userId.add(ljHouseSaleInfo.getChannelChief());//渠道总监
        userId.add(ljHouseSaleInfo.getAreaManage());//区域经理
        userId.add(ljHouseSaleInfo.getAdviser());//渠道员工 （顾问）
        for (String id:userId) {
            if(id != null){
                AdminUser adminUser = adminUserService.queryUserById(id);
                if(Objects.nonNull(adminUser)){
                    Employee employee1 = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
                    if(Objects.nonNull(employee1)){
                        Message message = new Message();
                        message.setSendMan(currentAdmin.getId());
                        message.setSendManName(employee.getPersonName());
                        message.setMsgTitle("佣金发放");
                        message.setMsgContent("佣金已发放！");
                        message.setMsgTime(LocalDateTime.now());
                        message.setIsRead("0");
                        message.setMsgType(Message.MESSAGE_TYPE_7);//佣金发放
                        message.setAcceptMan(adminUser.getId());
                        message.setAcceptManName(employee1.getPersonName());
                        messageService.saveMessage(message);
                    }
                }
            }

        }
        ljHouseSaleInfoService.saveLjHouseSaleInfo(ljHouseSaleInfo);
        return ajaxRe(true);
    }

    /*
    * 财务为营销销售信息结佣
    * */
    @RequestMapping("editExpenseMaidMarketingHouseSaleInfo")
    private Result editExpenseMaidMarketingHouseSaleInfo(MarketingSaleInfo marketingSaleInfo){
        marketingSaleInfo = marketingSaleInfoService.queryOneMarketingSaleInfo(marketingSaleInfo.getId());
        /*if("8".equals(marketingSaleInfo.getChargeStatus())){
            marketingSaleInfo.setChargeStatus("10");//已结佣
            marketingSaleInfoService.saveMarketingSaleInfo(marketingSaleInfo);
            return ajaxRe(true);
        }else {
            return ajaxRe(false);
        }*/
        return ajaxRe(false);
    }


    /*
    * 为营销部门的单个员工撤销结佣
    * */
    @RequestMapping("editCancelOne")
    private Result editCancelOne(String chargeInfoId){
        MarketingChargeInfo marketingChargeInfo = marketingChargeInfoService.queryOneMarketingChargeInfo(chargeInfoId);
        marketingChargeInfo.setChargeStatus("8");
        marketingChargeInfo.setChargeSendDate(null);
        marketingChargeInfoService.saveMarketingChargeInfo(marketingChargeInfo);
        //发送消息
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee1 = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        AdminUser targetUser = adminUserService.queryUserById(marketingChargeInfo.getChargeTargetUuid());
        Employee employee2 = employeeService.queryOneEmployeeByAccount(targetUser.getUsername());
        Message message = new Message();
        message.setSendMan(currentAdmin.getId());
        message.setSendManName(employee1.getPersonName());
        message.setAcceptMan(targetUser.getId());
        message.setAcceptManName(employee2.getPersonName());
        message.setIsRead("0");
        message.setMsgTitle("佣金发放");
        message.setMsgContent("财务佣金误操作！");
        message.setMsgType(Message.MESSAGE_TYPE_7);//佣金发放
        message.setMsgTime(LocalDateTime.now());
        message.setLinkUrl(null);
        messageService.saveMessage(message);
        return ajaxRe(true);
    }
}
