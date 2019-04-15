package com.jgp.ljoa.channel.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.util.TimeInterval;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
@Controller
@RequestMapping("/ljoa/channel/ljHouseSaleInfoController")
public class LjHouseSaleInfoController extends JGPController {
    @Autowired
    private LjHouseSaleInfoService service;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;

    //跳转展示页
    @RequestMapping("listAllLjHouseSaleInfo")
    public String listAllLjHouseSaleInfo(){

        return page("listAllLjHouseSaleInfo");
    }
    //跳转案场录入页
    @RequestMapping("listGroupLjHouseSaleInfoByCourtCase")
    public String listGroupLjHouseSaleInfoByCourtCase(){

        return page("listGroupLjHouseSaleInfoByCourtCase");
    }

    //跳转添加页
    @RequestMapping("addLjHouseSaleInfo/{id}")
    public String addLjHouseSaleInfo(@PathVariable String id, Model model){
        //房源主键
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);//房源
        LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljHouseInfo.getProjectUuid());
        LjHouseSaleInfo ljHouseSaleInfo = service.queryOneLjHouseSaleInfoBuUUid(id);//销售信息
        if(Objects.nonNull(ljHouseSaleInfo.getId())){
            reForm(model,"fdata",ljHouseSaleInfo);
        }else{
            ljHouseSaleInfo.setHouseUuid(id);
            ljHouseSaleInfo.setChargeStatus("0");
            ljHouseSaleInfo.setCaseManager(ljProjectInfo.getCaseManager());
            ljHouseSaleInfo.setAreaManage(ljProjectInfo.getPrjDutyMan());
            ljHouseSaleInfo.setChannelChief(ljProjectInfo.getChiefUuid());
            ljHouseSaleInfo = service.saveLjHouseSaleInfo(ljHouseSaleInfo);
            reForm(model,"fdata",ljHouseSaleInfo);
        }
        model.addAttribute("uuid",ljHouseSaleInfo.getId());
        model.addAttribute("ChargeStatus",ljHouseSaleInfo.getChargeStatus());
        model.addAttribute("companyChargeMoney",ljHouseInfo.getCompanyChargeMoney());//定额
        model.addAttribute("companyChargeScale",ljHouseInfo.getCompanyChargeScale());//比例（十进制）
        return page("addLjHouseSaleInfo");
    }

    //跳转添加页
    @RequestMapping("addLjHouseSaleInfoByCourtCase/{id}")
    public String addLjHouseSaleInfoByCourtCase(@PathVariable String id, Model model){
        //房源主键
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);//房源
        LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljHouseInfo.getProjectUuid());
        LjHouseSaleInfo ljHouseSaleInfo = service.queryOneLjHouseSaleInfoBuUUid(id);//销售信息
        if(Objects.nonNull(ljHouseSaleInfo.getId())){
            reForm(model,"fdata",ljHouseSaleInfo);
        }else{
            ljHouseSaleInfo.setHouseUuid(id);
            ljHouseSaleInfo.setChargeStatus("0");
            ljHouseSaleInfo.setCaseManager(ljProjectInfo.getCaseManager());
            ljHouseSaleInfo.setAreaManage(ljProjectInfo.getPrjDutyMan());
            ljHouseSaleInfo.setChannelChief(ljProjectInfo.getChiefUuid());
            reForm(model,"fdata",ljHouseSaleInfo);
        }
        model.addAttribute("ChargeStatus",ljHouseSaleInfo.getChargeStatus());
        model.addAttribute("companyChargeMoney",ljHouseInfo.getCompanyChargeMoney());//定额
        model.addAttribute("companyChargeScale",ljHouseInfo.getCompanyChargeScale());//比例（十进制）
        return page("addLjHouseSaleInfoByCourtCase");
    }

    @RequestMapping("showLjHouseSaleInfoSpeed/{id}")
    public String showLjHouseSaleInfoSpeed(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(id);

        String webName="";
        //渠道
        if("1".equals(ljHouseInfo.getHouseType())){
            LjHouseSaleInfo ljHouseSaleInfo = service.queryOneLjHouseSaleInfoBuUUid(id);//房源销售信息
            model.addAttribute("chargeStatus",ljHouseSaleInfo.getChargeStatus());//佣金状态
//            Approval approval1 = approvalService.queryOneApprovalByUuidAndCheckType(ljHouseSaleInfo.getId(), "1", "2");
            Approval approval2 = approvalService.queryOneApprovalByUuidAndCheckType(ljHouseSaleInfo.getId(), "2", "2");
            Approval approval3 = approvalService.queryOneApprovalByUuidAndCheckType(ljHouseSaleInfo.getId(), "3", "2");
            Approval approval4 = approvalService.queryOneApprovalByUuidAndCheckType(ljHouseSaleInfo.getId(), "4", "2");
            //时间间隔
            String s0 = "";
            String s1 = "";
            String s2 = "";
            String s3 = "";
            //审核日期
            String date0 = ljHouseSaleInfo.getCreateDatetime().toString().replace("T", " ");
            String date1 = "";
            String date2 = "";
            String date3 = "";
            String date4 = "";
            //打款日期
            String date5 = "";
            if(Objects.nonNull(approval4)){
                LocalDateTime modifyDatetime0 = ljHouseSaleInfo.getCreateDatetime();
                LocalDateTime modifyDatetime1 = ljHouseSaleInfo.getModifyDatetime();
                LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
//                LocalDateTime modifyDatetime3 = approval3.getModifyDatetime();
                LocalDateTime modifyDatetime4 = approval4.getModifyDatetime();
                date1 = modifyDatetime1.toString().replace("T", " ");
                date2 = modifyDatetime2.toString().replace("T", " ");
//                date3 = modifyDatetime3.toString().replace("T", " ");
                date4 = modifyDatetime4.toString().replace("T", " ");
                Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                s0 = TimeInterval.timeInterval(duration0);
                Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                s1 = TimeInterval.timeInterval(duration1);
                Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime4);
                s2 = TimeInterval.timeInterval(duration2);
//                Duration duration3 = Duration.between(modifyDatetime3,modifyDatetime4);
//                s3 = TimeInterval.timeInterval(duration3);
//                if("Y".equals(approval4.getCheckResult())){
//                }
            }else {
                if(Objects.nonNull(approval3)){
                    LocalDateTime modifyDatetime0 = ljHouseSaleInfo.getCreateDatetime();
                    LocalDateTime modifyDatetime1 = ljHouseSaleInfo.getModifyDatetime();
                    LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
//                    LocalDateTime modifyDatetime3 = approval3.getModifyDatetime();
                    date1 = modifyDatetime1.toString().replace("T", " ");
                    date2 = modifyDatetime2.toString().replace("T", " ");
//                    date3 = modifyDatetime3.toString().replace("T", " ");
                    Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                    s0 = TimeInterval.timeInterval(duration0);
                    Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                    s1 = TimeInterval.timeInterval(duration1);
//                    Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime3);
//                    s2 = TimeInterval.timeInterval(duration2);
                }else {
                    if(Objects.nonNull(approval2)){
                        LocalDateTime modifyDatetime0 = ljHouseSaleInfo.getCreateDatetime();
                        LocalDateTime modifyDatetime1 = ljHouseSaleInfo.getModifyDatetime();
                        LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
                        date1 = modifyDatetime1.toString().replace("T", " ");
                        date2 = modifyDatetime2.toString().replace("T", " ");
                        Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                        s0 = TimeInterval.timeInterval(duration0);
                        Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                        s1 = TimeInterval.timeInterval(duration1);
                    }else {
                            LocalDateTime modifyDatetime0 = ljHouseSaleInfo.getCreateDatetime();
                            LocalDateTime modifyDatetime1 = ljHouseSaleInfo.getModifyDatetime();
                            date1 = modifyDatetime1.toString().replace("T", " ");
                            Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                            s0 = TimeInterval.timeInterval(duration0);
                    }
                }
            }
            List<String> list = new ArrayList<>();
            list.add(s0);
            list.add(s1);
            list.add(s2);
            list.add(s3);
            List<String> dateList = new ArrayList<>();
            dateList.add(date0);
            dateList.add(date1);
            dateList.add(date2);
//            dateList.add(date3);
            dateList.add(date4);
            model.addAttribute("timeList", list);
            model.addAttribute("dateList", dateList);
            webName="showLjHouseSaleInfoSpeed";
        }
        //营销
        if("2".equals(ljHouseInfo.getHouseType())){
            MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(id);
//            Approval approval1 = approvalService.queryOneApprovalByUuidAndCheckType(marketingSaleInfo.getId(), "1", "2");
            Approval approval2 = approvalService.queryOneApprovalByUuidAndCheckType(marketingSaleInfo.getId(), "2", "2");
            Approval approval3 = approvalService.queryOneApprovalByUuidAndCheckType(marketingSaleInfo.getId(), "3", "2");
            Approval approval4 = approvalService.queryOneApprovalByUuidAndCheckType(marketingSaleInfo.getId(), "4", "2");
            //时间间隔
            String s0 = "";
            String s1 = "";
            String s2 = "";
            String s3 = "";
            //审批日期
            String date0 = marketingSaleInfo.getCreateDatetime().toString().replace("T", " ");
            String date1 = "";
            String date2 = "";
//            String date3 = "";
            String date4 = "";
            if(Objects.nonNull(approval4)){
                LocalDateTime modifyDatetime0 = marketingSaleInfo.getCreateDatetime();
                LocalDateTime modifyDatetime1 = marketingSaleInfo.getModifyDatetime();
                LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
//                LocalDateTime modifyDatetime3 = approval3.getModifyDatetime();
                LocalDateTime modifyDatetime4 = approval4.getModifyDatetime();
                date1 = marketingSaleInfo.getModifyDatetime().toString().replace("T", " ");
                date2 = approval2.getModifyDatetime().toString().replace("T", " ");
//                date3 = approval3.getModifyDatetime().toString().replace("T", " ");
                date4 = approval4.getModifyDatetime().toString().replace("T", " ");
                Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                s0 = TimeInterval.timeInterval(duration0);
                Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                s1 = TimeInterval.timeInterval(duration1);
                Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime4);
                s2 = TimeInterval.timeInterval(duration2);
          /*      Duration duration3 = Duration.between(modifyDatetime3,modifyDatetime4);
                s3 = TimeInterval.timeInterval(duration3);*/
            }else {
                if(Objects.nonNull(approval3)){
                    LocalDateTime modifyDatetime0 = marketingSaleInfo.getCreateDatetime();
                    LocalDateTime modifyDatetime1 = marketingSaleInfo.getModifyDatetime();
                    LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
                    LocalDateTime modifyDatetime3 = approval3.getModifyDatetime();
                    date1 = marketingSaleInfo.getModifyDatetime().toString().replace("T", " ");
                    date2 = approval2.getModifyDatetime().toString().replace("T", " ");
//                    date3 = approval3.getModifyDatetime().toString().replace("T", " ");
                    Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                    s0 = TimeInterval.timeInterval(duration0);
                    Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                    s1 = TimeInterval.timeInterval(duration1);
                    Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime3);
                    s2 = TimeInterval.timeInterval(duration2);
                }else {
                    if(Objects.nonNull(approval2)){
                        LocalDateTime modifyDatetime0 = marketingSaleInfo.getCreateDatetime();
                        LocalDateTime modifyDatetime1 = marketingSaleInfo.getModifyDatetime();
                        LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
                        date1 = marketingSaleInfo.getModifyDatetime().toString().replace("T", " ");
                        date2 = approval2.getModifyDatetime().toString().replace("T", " ");
                        Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                        s0 = TimeInterval.timeInterval(duration0);
                        Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                        s1 = TimeInterval.timeInterval(duration1);
                    }else {
                            LocalDateTime modifyDatetime0 = marketingSaleInfo.getCreateDatetime();
                            LocalDateTime modifyDatetime1 = marketingSaleInfo.getModifyDatetime();
                            date1 = marketingSaleInfo.getModifyDatetime().toString().replace("T", " ");
                            Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                            s0 = TimeInterval.timeInterval(duration0);
                    }
                }
            }
            List<String> list = new ArrayList<>();
            list.add(s0);
            list.add(s1);
            list.add(s2);
            list.add(s3);
            List<String> dateList = new ArrayList<>();
            dateList.add(date0);
            dateList.add(date1);
            dateList.add(date2);
//            dateList.add(date3);
            dateList.add(date4);
            model.addAttribute("timeList", list);
            model.addAttribute("dateList", dateList);
            if(marketingSaleInfo.getChanneChargeStatus().equals(marketingSaleInfo.getInsideChargeStatus())){
                model.addAttribute("insideChargeStatus",marketingSaleInfo.getInsideChargeStatus());
            }else{
                model.addAttribute("insideChargeStatus",marketingSaleInfo.getInsideChargeStatus());
            }
            webName="showLjHouseSaleInfoSpeedYX";
        }
        return page(webName);
    }







}
