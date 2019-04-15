package com.jgp.ljoa.com.controller;/**
 * Created by Administrator on 2018/7/8.
 */

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/8
 */
@Controller
@RequestMapping("/ljoa/com/marketingHouseInfoController")
public class MarketingHouseInfoController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;

    //跳转到查询房源的页面
    @RequestMapping("listPurchaseMoney")
    public String listPurchaseMoney(){
        return page("listPurchaseMoney");
    }

    //跳转到房源定价的页面
    @RequestMapping("editHouseInfo/{id}")
    public String editHouseInfo(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(id);
        reForm(model, "fdata", ljHouseInfo);
        return page("editHouseInfo");
    }
    //跳转到房源定价的页面
    @RequestMapping("showPriceHouseInfo/{id}")
    public String showPriceHouseInfo(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(id);
        reForm(model, "fdata", ljHouseInfo);
        return page("showPriceHouseInfo");
    }

}
