package com.jgp.ljoa.common.controller;

import com.jgp.ljoa.common.model.LjNoticeInfo;
import com.jgp.ljoa.common.service.LjNoticeInfoService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * Created by Administrator on 2018/9/11.
 */
/*
作者  SSF
时间   2018/9/11
*/
@Controller
@RequestMapping("/ljoa/common/LjNoticeInfoController")
public class LjNoticeInfoController extends JGPController {


    @Autowired
    private LjNoticeInfoService ljNoticeInfoService;

    @RequestMapping("listGroupsLjNoticeInfo")//跳转展示
    public String listGroupsLjNoticeInfo(){

        return page("listGroupsLjNoticeInfo");
    }

    @RequestMapping("addLjNoticeInfo")//添加
    public String addLjNoticeInfo(Model model){
        LjNoticeInfo ljNoticeInfo=new LjNoticeInfo();
////        LjNoticeInfo ljNoticeInfo1 = ljNoticeInfoService.saveLjNoticeInfo(ljNoticeInfo);
//        model.addAttribute("id",ljNoticeInfo1.getId());
//        reForm(model,"fdata",ljNoticeInfo1);

        return page("addLjNoticeInfo");
    }

    @RequestMapping("editLjNoticeInfo/{id}")//修改
    public String editLjNoticeInfo(@PathVariable String id,Model model){
        LjNoticeInfo ljNoticeInfo = ljNoticeInfoService.queryOneLjNoticeInfo(id);
        model.addAttribute("id",ljNoticeInfo.getId());
        reForm(model,"fdata",ljNoticeInfo);

        return page("editLjNoticeInfo");
    }

    @RequestMapping("showLjNoticeInfo/{id}")//详情
    public String showLjNoticeInfo(@PathVariable("id") String id,Model model){
        LjNoticeInfo ljNoticeInfo = ljNoticeInfoService.queryOneLjNoticeInfo(id);
        if(Objects.nonNull(ljNoticeInfo.getId())){
            model.addAttribute("id",ljNoticeInfo.getId());
            reForm(model,"fdata",ljNoticeInfo);
        }else{
            ljNoticeInfo.setId(id);
            model.addAttribute("id",id);
            reForm(model,"fdata",ljNoticeInfo);
        }

        return page("showLjNoticeInfo");
    }


    @RequestMapping("listGroupLjNoticeInfoNotice")   //已发布公告
    public String listGroupLjNoticeInfoNotice(){

        return page("listGroupLjNoticeInfoNotice");
    }

    @RequestMapping("listGroupLjNoticeInfoJournalism")   //已发布新闻
    public String listGroupLjNoticeInfoJournalism(){

        return page("listGroupLjNoticeInfoJournalism");
    }

}
