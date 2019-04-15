package com.jgp.ljoa.channel.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/

@Controller
@RequestMapping("/ljoa/channel/ljHouseInfoController")
public class LjHouseInfoController extends JGPController {
    @Autowired
    private LjHouseInfoService service;
    @Autowired
    private LjProjectInfoService ljChannelProjectService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;


    @RequestMapping("listAllLjHouseInfo")//展示
    public String listAllLjHouseInfo(){
//System.out.print(123123);
        return page("listAllLjHouseInfo");
    }
    @RequestMapping("addLjHouseInfo/{id}")//添加
    public String addLjHouseInfo(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo=new LjHouseInfo();
        ljHouseInfo.setProjectUuid(id);
        reForm(model,"fdata",ljHouseInfo);
        model.addAttribute("uuid",id);
        return page("addLjHouseInfo");
    }
    @RequestMapping("editLjHouseInfo/{id}")//修改
    public String editLjHouseInfo(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = service.queryOneLjHouseInfoByid(id);
        reForm(model,"fdata",ljHouseInfo);
        model.addAttribute("uuid",id);
        model.addAttribute("status",ljHouseInfo.getStatus());
        return page("editLjHouseInfo");
    }
    @RequestMapping("showLjHouseInfo/{id}")//详情
    public String showLjHouseInfo(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = service.queryOneLjHouseInfoByid(id);
        LjHouseSaleInfo ljHouseSaleInfo = new LjHouseSaleInfo();
        if(Objects.nonNull(ljHouseInfo)){
            ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(ljHouseInfo.getId());
        }
        reForm(model,"saleData",ljHouseSaleInfo);
        reForm(model,"fdata",ljHouseInfo);
        model.addAttribute("uuid",id);
        return page("showLjHouseInfo");
    }
    //已售房源
    @RequestMapping("listHouseInfoSold")
    public String listHouseInfoSold(){
        return page("listHouseInfoSold");
    }

    //销售信息
    @RequestMapping("showHouseSaleInfo/{id}")
    public String showHouseSaleInfo(@PathVariable String id,Model model){
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(id);
        reForm(model,"fdata",ljHouseSaleInfo);
        return page("showHouseSaleInfo");
    }

    //已售已结佣房源（已收款）
    @RequestMapping("listGroudHouseInfo")
    public String listGroudHouseInfo(){

        return page("listGroudHouseInfo");
    }
    //已售房源(未收款)
    @RequestMapping("listGroudHouseInfoNO")
    public String listGroudHouseInfoNO(){

        return page("listGroudHouseInfoNO");
    }

    //利润（渠道首页利润）
    @RequestMapping("listProfit")
    public String listProfit(){

        return page("listProfit");
    }
    //对应项目房源信息
    @RequestMapping("listHouseInfoByuuid/{id}")
    public String listHouseInfoByuuid(@PathVariable String id,Model model){
        LjProjectInfo ljProjectInfo = ljChannelProjectService.queryOneLjProjectInfoById(id);
        reForm(model,"fdata",ljProjectInfo);
        model.addAttribute("uuid",id);
        return page("listHouseInfoByuuid");
    }
}
