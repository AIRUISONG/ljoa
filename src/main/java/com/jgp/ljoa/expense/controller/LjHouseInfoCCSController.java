package com.jgp.ljoa.expense.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * Created by Administrator on 2018/7/9.
 */
/*
作者  SSF
时间   2018/7/9
*/
@Controller
@RequestMapping("/ljoa/expense/ljHouseInfoCCSController")//房源信息佣金状态(CompanyChargeStatus)Controller
public class LjHouseInfoCCSController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;

    //跳转已售房源列表页
    @RequestMapping("listGroupLjHouseInfoCCS")
    public String listGroupLjHouseInfoCCS(){

        return page("listGroupLjHouseInfoCCS");
    }
    @RequestMapping("editLjHouseInfoCCS/{id}")
    public String editLjHouseInfoCCS(@PathVariable String id,Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        LjHouseSaleInfo ljHouseSaleInfo = new LjHouseSaleInfo();
        if(Objects.nonNull(ljHouseInfo)){
           ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(ljHouseInfo.getId());
        }
        model.addAttribute("uuid",id);
        reForm(model,"fdata",ljHouseInfo);
        reForm(model,"saleData",ljHouseSaleInfo);
        return page("editLjHouseInfoCCS");
    }
}
