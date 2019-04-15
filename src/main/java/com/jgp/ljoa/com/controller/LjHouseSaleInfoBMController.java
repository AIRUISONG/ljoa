package com.jgp.ljoa.com.controller;

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
 * Created by Administrator on 2018/7/17.
 */
/*
作者  SSF
时间   2018/7/17
*/
@Controller
@RequestMapping("/ljoa/com/ljHouseSaleInfoBMController")
public class LjHouseSaleInfoBMController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService service;

    @RequestMapping("addLjHouseSaleInfoBM/{id}")
    public String addLjHouseSaleInfoBM(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);//房源
        LjHouseSaleInfo ljHouseSaleInfo = service.queryOneLjHouseSaleInfoBuUUid(id);//销售信息
        if(Objects.nonNull(ljHouseSaleInfo.getId())){
            reForm(model,"fdata",ljHouseSaleInfo);
        }else{
            ljHouseSaleInfo.setHouseUuid(id);
            ljHouseSaleInfo.setChargeStatus("0");
            reForm(model,"fdata",ljHouseSaleInfo);
        }

        model.addAttribute("uuid",ljHouseSaleInfo.getId());
        model.addAttribute("ChargeStatus",ljHouseSaleInfo.getChargeStatus());
        model.addAttribute("companyChargeMoney",ljHouseInfo.getCompanyChargeMoney());//定额
        model.addAttribute("companyChargeScale",ljHouseInfo.getCompanyChargeScale());//比例（十进制）
        return page("addLjHouseSaleInfoBM");
    }
}
