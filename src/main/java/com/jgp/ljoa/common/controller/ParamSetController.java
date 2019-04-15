package com.jgp.ljoa.common.controller;/**
 * Created by Administrator on 2018/7/9.
 */

import com.jgp.ljoa.common.model.ParamSet;
import com.jgp.ljoa.common.service.ParamSetService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/9
 */
@Controller
@RequestMapping("ljoa/common/paramSetController")
public class ParamSetController extends JGPController {

    @Autowired
    private ParamSetService paramSetService;

    //跳转到查询所有通用参数的页面
    @RequestMapping("listAllParamSet")
    public String  listAllParamSet(Model model){
        return page("listAllParamSet");
    }

    //跳转到添加通用参数的页面
    @RequestMapping("addParamSet")
    public  String addParamSet( Model model){
        return page("addParamSet");
    }

    //跳转到编辑通用参数的页面
    @RequestMapping("editParamSet/{id}")
    public  String editParamSet(@PathVariable("id")String id, Model model){
        ParamSet paramSet = paramSetService.queryOneParamSet(id);
        reForm(model,"fdata",paramSet);
        return page("editParamSet");
    }
}
