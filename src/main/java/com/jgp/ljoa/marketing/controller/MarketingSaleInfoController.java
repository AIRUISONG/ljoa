package com.jgp.ljoa.marketing.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/7/5 0005.
 */
@Controller
@RequestMapping("/ljoa/marketing/marketingSaleInfoController")
public class MarketingSaleInfoController extends JGPController {
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private CustomerInfoService customerInfoService;
    //跳顾问售信息录入
    @RequestMapping("listLjHouseInfoByStatusInfoSold")
    public String listLjHouseInfoByStatusInfoSold(){
        return page("listLjHouseInfoByStatusInfoSold");
    }
    //跳转已售房源销售经理录入
    @RequestMapping("listLjHouseInfoByStatusInfoSoldManager")
    public String listLjHouseInfoByStatusInfoSoldManager(){
        return page("listLjHouseInfoByStatusInfoSoldManager");
    }
    //跳转已售房源销售总监录入待审批
    @RequestMapping("listLjHouseInfoByStatusInfoSoldChiefInspector")
    public String listLjHouseInfoByStatusInfoSoldChiefInspector(){
        return page("listLjHouseInfoByStatusInfoSoldChiefInspector");
    }
    //跳转已售房源销售总监录入已审批
    @RequestMapping("listLjHouseInfoByStatusInfoSoldChiefInspector1")
    public String listLjHouseInfoByStatusInfoSoldChiefInspector1(){
        return page("listLjHouseInfoByStatusInfoSoldChiefInspector1");
    }
    //公司利润页面
    @RequestMapping("addMarketingSaleInfo/{hid}")
    public String addMarketingSaleInfo(@PathVariable String hid,Model model){
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(hid);
        CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(hid);
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(hid);
        if(StringUtils.isEmpty(marketingSaleInfo.getHouseUuid())){//不存在公司利润信息
            marketingSaleInfo.setHouseUuid(hid);
            marketingSaleInfo.setChanneChargeStatus("0");//渠道结佣未上报
            marketingSaleInfo.setInsideChargeStatus("0");//内部结佣未上报
            marketingSaleInfo.setGrossProfit(ljHouseInfo.getCompanyChargeMoney());
        }
        model.addAttribute("channeChargeStatus",marketingSaleInfo.getChanneChargeStatus());
        reForm(model,"fdata",marketingSaleInfo);
        return page("addMarketingSaleInfo");
    }

    //总监修改房源信息客户信息
    @RequestMapping("editLjHouseInfoManager/{id}")
    public String editLjHouseInfoManager(@PathVariable String id,Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);//房源信息
        CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(id);//客户信息
        model.addAttribute("hid",id);
        reForm(model,"fdata",ljHouseInfo);
        reForm(model,"fdata2",customerInfo);
        return page("editLjHouseInfoManager");
    }
}
