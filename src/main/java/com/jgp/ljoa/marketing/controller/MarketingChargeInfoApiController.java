package com.jgp.ljoa.marketing.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjChannelCompany;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.service.LjChannelCompanyService;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/7/10 0010.
 */
@RestController
@RequestMapping("/ljoa/marketing/marketingChargeInfoApiController")
public class MarketingChargeInfoApiController extends JGPController {
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LjChannelCompanyService ljChannelCompanyService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private MessageService messageService;

    //查询置业顾问佣金和渠道佣金
    @RequestMapping("listGroupMarketingChargeInfoCounselorMan/{hid}")
    public Result listGroupMarketingChargeInfoCounselorMan(@PathVariable String hid, @UIParam("pager") Pager pager){

        List<String> list=new ArrayList<>();
        list.add("1");//置业顾问
        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid,list,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("佣金对象名称", "chargeTargetName"));
        categories.add(new GridResult.Category("佣金金额", "chargeMoney"));
        categories.add(new GridResult.Category("佣金类型", "chargeType","LJ_CUSTOMER_INFO.CHARGE_TYPE"));
        categories.add(new GridResult.Category("佣金状态", "chargeStatus","LJ_CUSTOMER_INFO.CHARGESTATUS"));
        categories.add(new GridResult.Category("结佣时间", "chargeSendDate"));
        return ajaxReGrid("gdata", categories, marketingChargeInfos, pager);
    }

    //保存佣金信息
    @RequestMapping("saveMarketingChargeInfo")
    public Result saveMarketingChargeInfo(MarketingChargeInfo marketingChargeInfo){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(marketingChargeInfo.getHouseUuid());

        Float companyChargeMoney = ljHouseInfo.getCompanyChargeMoney();//公司佣金金额
        Float chargeProportion = marketingChargeInfo.getChargeProportion();//佣金比例
        if(Objects.nonNull(marketingChargeInfo.getChargeProportion())){
            marketingChargeInfo.setChargeMoney(companyChargeMoney*chargeProportion/100);//佣金金额
        }
        if("5".equals(marketingChargeInfo.getChargeType())){
            LjChannelCompany ljChannelCompany = ljChannelCompanyService.queryOneLjChannelCompany(marketingChargeInfo.getChargeTargetUuid());
            marketingChargeInfo.setChargeTargetName(ljChannelCompany.getCompanyName());
        }else{
            AdminUser adminUser = adminUserService.queryUserById(marketingChargeInfo.getChargeTargetUuid());
            Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
            marketingChargeInfo.setChargeTargetName(employee.getPersonName());
        }
        MarketingChargeInfo marketingChargeInfo1 = marketingChargeInfoService.saveMarketingChargeInfo(marketingChargeInfo);

        LjHouseInfo ljHouseInfo1 = ljHouseInfoService.queryOneLjHouseInfoByid(marketingChargeInfo1.getHouseUuid());//对应房源
        List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryMarketingSaleInfoByHouseUuid(ljHouseInfo1.getId());
        MarketingSaleInfo marketingSaleInfo=new MarketingSaleInfo();//销售信息
        if(marketingSaleInfos.size()>0){marketingSaleInfo=marketingSaleInfos.get(0);}
        if("5".equals(marketingChargeInfo1.getChargeType())&&"12".equals(marketingChargeInfo1.getChargeStatus())){//渠道类型并已结佣
            marketingSaleInfo.setChanneChargeStatus("12");//渠道佣金状态已结佣
            marketingSaleInfoService.saveMarketingSaleInfo(marketingSaleInfo);//修改
        }

        if((!"5".equals(marketingChargeInfo1.getChargeType()))&&"12".equals(marketingChargeInfo1.getChargeStatus())){//不是渠道类型已结佣
            marketingSaleInfo.setInsideChargeStatus("12");//内部佣金状态已结佣
            marketingSaleInfoService.saveMarketingSaleInfo(marketingSaleInfo);//修改
        }
        if("12".equals(marketingSaleInfo.getChanneChargeStatus()) && "12".equals(marketingSaleInfo.getInsideChargeStatus())){
            //发送消息
            AdminUser currentAdmin = adminUserService.getCurrentAdmin();
            Employee employee1 = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
            AdminUser targetUser = adminUserService.queryUserById(marketingChargeInfo.getChargeTargetUuid());
            if(Objects.nonNull(targetUser)){
                Employee employee2 = employeeService.queryOneEmployeeByAccount(targetUser.getUsername());
                Message message = new Message();
                message.setSendMan(currentAdmin.getId());
                message.setSendManName(employee1.getPersonName());
                message.setAcceptMan(targetUser.getId());
                message.setAcceptManName(employee2.getPersonName());
                message.setIsRead("0");
                message.setMsgTitle("佣金发放");
                message.setMsgContent("佣金已发放！");
                message.setMsgType(Message.MESSAGE_TYPE_7);//佣金发放
                message.setMsgTime(LocalDateTime.now());
                message.setLinkUrl(null);
                messageService.saveMessage(message);
            }
        }
        return ajaxRe(true);
    }

    //保存佣金信息
    @RequestMapping("editMarketingChargeInfo")
    public Result editMarketingChargeInfo(MarketingChargeInfo marketingChargeInfo){
        MarketingChargeInfo marketingChargeInfo1 = marketingChargeInfoService.saveMarketingChargeInfo(marketingChargeInfo);
        return ajaxRe(true);
    }
    //删除单个佣金信息
    @RequestMapping("removeOneMarketingProject")
    public Result removeOneMarketingProject(MarketingChargeInfo marketingChargeInfo){
        marketingChargeInfoService.removeMarketingChargeInfo(marketingChargeInfo.getId());
        return ajaxRe(true);
    }

    //查询置业顾问佣金和主管佣金
    @RequestMapping("listGroupMarketingChargeInfoManager/{hid}")
    public Result listGroupMarketingChargeInfoManager(@PathVariable String hid, @UIParam("pager") Pager pager){
        List<String> list=new ArrayList<>();
        list.add("1");//置业顾问
        list.add("2");//主管
        list.add("5");//渠道
        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid,list,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("佣金对象名称", "chargeTargetName"));
        categories.add(new GridResult.Category("佣金金额", "chargeMoney"));
        categories.add(new GridResult.Category("佣金类型", "chargeType","LJ_CUSTOMER_INFO.CHARGE_TYPE"));
        categories.add(new GridResult.Category("佣金状态", "chargeStatus","LJ_CUSTOMER_INFO.CHARGESTATUS"));
        categories.add(new GridResult.Category("结佣时间", "chargeSendDate"));
        return ajaxReGrid("gdata", categories, marketingChargeInfos, pager);
    }
    //查询所有人的佣金
    @RequestMapping("listGroupMarketingChargeInfo/{hid}")
    public Result listGroupMarketingChargeInfo(@PathVariable String hid, @UIParam("pager") Pager pager){
        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid,null,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("佣金对象名称", "chargeTargetName"));
        categories.add(new GridResult.Category("佣金金额", "chargeMoney"));
        categories.add(new GridResult.Category("佣金类型", "chargeType","LJ_CUSTOMER_INFO.CHARGE_TYPE"));
        categories.add(new GridResult.Category("佣金状态", "chargeStatus","LJ_CUSTOMER_INFO.CHARGESTATUS"));
        categories.add(new GridResult.Category("结佣时间", "chargeSendDate"));
        return ajaxReGrid("gdata", categories, marketingChargeInfos, pager);
    }
}
