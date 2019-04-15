package com.jgp.ljoa.channel.controller;

import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
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
@RequestMapping("/ljoa/channel/ljProjectInfoController")
public class LjProjectInfoController extends JGPController {
    @Autowired
    private LjProjectInfoService service;
    @RequestMapping("listAllLjProjectInfo")
    private String listAllLjProjectInfo(){
        return page("listAllLjProjectInfo");
    }
    @RequestMapping("addLjProjectInfo")
    private String addLjProjectInfo(Model model){
        LjProjectInfo ljProjectInfo=new LjProjectInfo();
        ljProjectInfo.setProjectType("1");
        reForm(model,"fdata",ljProjectInfo);
        return page("addLjProjectInfo");
    }
    @RequestMapping("showLjProjectInfo/{id}")
    private String showLjProjectInfo(@PathVariable String id,Model model){
        LjProjectInfo ljChannelProject = service.queryOneLjProjectInfoById(id);
        reForm(model,"fdata",ljChannelProject);
        return page("showLjProjectInfo");
    }
    @RequestMapping("editLjProjectInfo/{id}")
    private String editLjProjectInfo(@PathVariable String id,Model model){
        LjProjectInfo ljChannelProject = service.queryOneLjProjectInfoById(id);
        reForm(model,"fdata",ljChannelProject);
        return page("editLjProjectInfo");
    }
    //跳转财务渠道房源信息
    @RequestMapping("listAllHouseSaleInfoFinance")
    private String listAllHouseSaleInfoFinance(){

        return page("listAllHouseSaleInfoFinance");
    }
    //跳转财务营销房源信息
    @RequestMapping("listAllHouseSaleInfoFinanceYX")
    private String listAllHouseSaleInfoFinanceYX(){

        return page("listAllHouseSaleInfoFinanceYX");
    }
    //跳转渠道导出
    @RequestMapping("listAllQDProjectExport")
    private String listAllQDProjectExport(){
        return page("listAllQDProjectExport");
    }
}
