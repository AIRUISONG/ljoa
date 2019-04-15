package com.jgp.ljoa.com.controller;/**
 * Created by Administrator on 2018/7/20.
 */

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/20
 */
@RestController
@RequestMapping("/ljoa/com/dataApiController")
public class DataApiController extends JGPController {

    @Autowired
    private LjHouseInfoService ljHouseInfoService;


    /*
    * 查询总经理页面的静态数据
    * */
    @RequestMapping("listGeneralManagerStaticData")
    public Result queryStaticData(){
        Map<String, Object> mapData = new HashMap<>();
        //渠道整合
        Map<String, Object> channelMap = new HashMap<>();
        //营销
        Map<String, Object> marketingMap = new HashMap<>();
        //渠道整合收入
        Float channelIncome = 0f;
        //营销收入
        Float marketingIncome = 0f;

        LjHouseInfo ljHouseInfo = new LjHouseInfo();
        ljHouseInfo.setCompanyChargeTime(LocalDate.now());
        ljHouseInfo.setCompanyChargeStatus("2");//已结佣
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupHouseInfo(ljHouseInfo);
        for (LjHouseInfo l : ljHouseInfos){
            if("1".equals(l.getHouseType())){
                channelIncome += l.getCompanyChargeMoney();
            }else if("2".equals(l.getHouseType())){
                marketingIncome += l.getCompanyChargeMoney();
            }
        }
        //精确到小数点后两位
        channelMap.put("income",String.format("%.2f",channelIncome));
        marketingMap.put("income",String.format("%.2f",marketingIncome));
        //1.收入


        mapData.put("channel",channelMap);
        mapData.put("marketing",marketingMap);
        return ajaxReData("mapListData",null);
    }
}
