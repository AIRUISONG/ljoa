package com.jgp.ljoa.marketing.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/

@Controller
@RequestMapping("/ljoa/marketing/ljHouseInfoYXController")
public class LjHouseInfoYXController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private LjProjectInfoService ljChannelProjectService;

    @RequestMapping("listAllLjHouseInfo")//展示
    public String listAllLjHouseInfo(){
        return page("listAllLjHouseInfo");
    }


    //修改页面
    @RequestMapping("editLjHouseInfo/{id}")
    public String editLjHouseInfo(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        model.addAttribute("uuid",id);
        reForm(model,"fdata",ljHouseInfo);
        return page("editLjHouseInfo");
    }

    @RequestMapping("addLjHouseInfo/{id}")//添加
    public String addLjHouseInfo(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo=new LjHouseInfo();
        ljHouseInfo.setProjectUuid(id);
        reForm(model,"fdata",ljHouseInfo);
        model.addAttribute("uuid",id);
        return page("addLjHouseInfo");
    }
    @RequestMapping("showLjHouseInfo/{id}")//添加
    public String showLjHouseInfo(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        model.addAttribute("uuid",id);
        reForm(model,"fdata",ljHouseInfo);
        return page("showLjHouseInfo");
    }

    //查询里营销页面（已售、营销房源）
    @RequestMapping("listHouseInfoSold")
    public String listHouseInfoSold(){
        return page("listHouseInfoSold");
    }

    //查询营销中公司内部人员佣金页面
    @RequestMapping("listMarketingChargeInfoByHid/{hid}")
    public String listMarketingChargeInfoByHid(@PathVariable String hid, Model model){
        model.addAttribute("hid",hid);
        return page("listMarketingChargeInfoByHid");
    }
    //查询营销公司利润页面
    @RequestMapping("showMarketingSaleInfo/{hid}")
    public String showMarketingSaleInfo(@PathVariable String hid, Model model){
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(hid);
        reForm(model,"fdata",marketingSaleInfo);
        return page("showMarketingSaleInfo");
    }

    //营销总监首页应收款链接
    @RequestMapping("listGrouptHouseInfoAll")
    public String listGrouptHouseInfoAll(){

        return page("listGrouptHouseInfoAll");
    }

    //营销总监首页已收款链接
    @RequestMapping("listGrouptHouseInfoYES")
    public String listGrouptHouseInfoYES(){

        return page("listGrouptHouseInfoYES");
    }

    //营销总监首页利润链接
    @RequestMapping("listGroupHouseInfoProfit")
    public String lidtGroupHouseInfoProfit(){

        return page("listGroupHouseInfoProfit");
    }

    //营销总监首页对应项目信息
    @RequestMapping("listHouseInfoByuuid/{id}")
    public String listHouseInfoByuuid(@PathVariable String id,Model model){
        LjProjectInfo ljProjectInfo = ljChannelProjectService.queryOneLjProjectInfoById(id);
        reForm(model,"fdata",ljProjectInfo);
        model.addAttribute("uuid",id);
        return page("listHouseInfoByuuid");
    }

}
