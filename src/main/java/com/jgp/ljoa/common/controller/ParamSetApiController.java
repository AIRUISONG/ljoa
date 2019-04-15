package com.jgp.ljoa.common.controller;/**
 * Created by Administrator on 2018/7/9.
 */

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.common.model.ParamSet;
import com.jgp.ljoa.common.service.ParamSetService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/9
 */
@RestController
@RequestMapping("ljoa/common/paramSetApiController")
public class ParamSetApiController extends JGPController {

    @Autowired
    private ParamSetService paramSetService;

    /*
    * 查询所有通用参数
    * */
    @RequestMapping("listAllParamSet")
    public Result listAllParamSet(Model model, @UIParam("pager") Pager pager){
        List<ParamSet> paramSets = paramSetService.queryAllParamSet(pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("参数名称", "paramName"));
        categories.add(new GridResult.Category("参数代码", "paramCode"));
        categories.add(new GridResult.Category("字符参数值1", "strContent1"));
        categories.add(new GridResult.Category("字符参数值2", "strContent2"));
        categories.add(new GridResult.Category("字符参数值3", "strContent3"));
        categories.add(new GridResult.Category("数值参数值1", "numContent1"));
        categories.add(new GridResult.Category("数值参数值2", "numContent2"));
        categories.add(new GridResult.Category("数值参数值3", "numContent3"));
        categories.add(new GridResult.Category("日期参数值1", "dateContent1"));
        categories.add(new GridResult.Category("日期参数值2", "dateContent2"));
        categories.add(new GridResult.Category("日期参数值3", "dateContent3"));
        return ajaxReGrid("gdata",categories,paramSets,pager);
    }

    /*
    * 添加通用参数
    * */
    @RequestMapping("addParamSet")
    public Result addParamSet(ParamSet paramSet) {
        paramSet=paramSetService.saveParamSet(paramSet);
        boolean flag = false;
        if (Objects.nonNull(paramSet)) {
            flag = true;
        }
        return ajaxRe(flag);
    }

    /*
    * 编辑通用参数
    * */
    @RequestMapping("editParamSet")
    public Result editParamSet(ParamSet paramSet) {
        paramSet=paramSetService.saveParamSet(paramSet);
        boolean flag = false;
        if (Objects.nonNull(paramSet)) {
            flag = true;
        }
        return ajaxRe(flag);
    }

    /*
    * 删除单个通用参数
    * */
    @RequestMapping("removeParamSet")
    public Result removeParamSet(String id) {
        boolean flag = false;
        try {
            flag = true;
            paramSetService.removeParamSet(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajaxRe(flag);
    }

    /*
    * 批量删除通用参数
    * */
    @RequestMapping("removeSelectedParamSet")
    public Result removeSelectedParamSet(String[] array) {
        paramSetService.removeParamSets(array);
        return ajaxRe(true);
    }
}
