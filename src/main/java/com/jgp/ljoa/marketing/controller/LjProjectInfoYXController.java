package com.jgp.ljoa.marketing.controller;

import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.hr.model.Relation;
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
@RequestMapping("/ljoa/marketing/ljProjectInfoYXController")
public class LjProjectInfoYXController extends JGPController {
    @Autowired
    private LjProjectInfoService service;


    //项目展示
    @RequestMapping("listAllLjProjectInfo")
    private String listAllLjProjectInfo(){

        return page("listAllLjProjectInfo");
    }
    //添加项目
    @RequestMapping("addLjProjectInfo")
    private String addLjProjectInfo(Model model){
        LjProjectInfo ljProjectInfo=new LjProjectInfo();
        ljProjectInfo.setProjectType("2");
        reForm(model,"fdata",ljProjectInfo);
        return page("addLjProjectInfo");
    }
    //项目详情
    @RequestMapping("showLjProjectInfo/{id}")
    private String showLjProjectInfo(@PathVariable String id,Model model){
        LjProjectInfo ljChannelProject = service.queryOneLjProjectInfoById(id);
        reForm(model,"fdata",ljChannelProject);
        return page("showLjProjectInfo");

    }
    //修改项目
    @RequestMapping("editLjProjectInfo/{id}")
    private String editLjProjectInfo(@PathVariable String id,Model model){
        LjProjectInfo ljChannelProject = service.queryOneLjProjectInfoById(id);
        reForm(model,"fdata",ljChannelProject);
        return page("editLjProjectInfo");
    }
    //项目对应置业顾问展示
    @RequestMapping("listGroupPrjDutyMan/{id}")
    private String listGroupPrjDutyMan(@PathVariable String id,Model model){
        model.addAttribute("mainUuid",id);//项目主键
        return page("listGroupPrjDutyMan");
    }
    //添加置业顾问
    @RequestMapping("addPrjDutyMan/{id}")
    private String addPrjDutyMan(@PathVariable String id,Model model){
        LjProjectInfo ljProjectInfo = service.queryOneLjProjectInfoById(id);
        Relation relation=new Relation();
        relation.setMainUuid(id);
        relation.setRelationType("4");
        model.addAttribute("projectName",ljProjectInfo.getProjectName());//项目名
        reForm(model,"fdata",relation);
        return page("addPrjDutyMan");
    }

    @RequestMapping("listAllYXProjectExport")
    private String listAllYXProjectExport(){
        return page("listAllYXProjectExport");
    }
}
