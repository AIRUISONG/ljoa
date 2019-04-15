package com.jgp.ljoa.marketing.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.common.pojo.LabelValue;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/11 0011.
 */
@RestController
@RequestMapping("/ljoa/marketing/ljProjectInfoYXApiController")
public class LjProjectInfoYXApiController extends JGPController {
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private RelationService relationService;//关系表
    @Autowired
    private EmployeeService employeeService;//员工
    @RequestMapping("listProjectDrop")
    public Result listProjectDrop(){
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectTypeYX();
        List<LabelValue> labelValues = ljProjectInfos.stream().map(ljProjectInfo -> {
            return new LabelValue(ljProjectInfo.getProjectName(),ljProjectInfo.getId(),null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",labelValues);
    }
    //项目对应置业顾问
    @RequestMapping("listGroupPrjDutyMan/{id}")
    public Result listGroupPrjDutyMan(@PathVariable String id,@UIParam("pager") Pager pager){
        List<Relation> relations = relationService.queryGroupRelation(id, "4", pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目","mainUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("置业顾问", "subUuid", Employee.class,"personName"));
        return ajaxReGrid("gdata",categories,relations,pager);
    }

    @RequestMapping("addPrjDutyMan")//添加置业顾问
    public Result addPrjDutyMan(Relation relation){
        Relation relation1 = relationService.saveRelation(relation);
        return ajaxRe(true);
    }
    @RequestMapping("removePrjDutyMan/{id}")//删除置业顾问
    public Result removePrjDutyMan(@PathVariable String id){
        relationService.removeRelation(id);
        return ajaxRe(true);
    }

    @RequestMapping("listEmployeeDrop")//项目顾问对应drop
    public Result listEmployeeDrop(@UIParam("pager") Pager pager){
        List<Employee> employees = employeeService.queryAllEmployee();
        List<LabelValue> labelValues = employees.stream().map(emp -> {
            return new LabelValue(emp.getPersonName(),emp.getId(),null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",labelValues);
    }

    @RequestMapping({"exportInfo/{projectId}"})
    @ResponseBody
    public ResponseEntity<byte[]> exportInfo(@PathVariable("projectId") String projectId) throws Exception {
        return ljProjectInfoService.exportMarkingInfo(projectId);
    }
}
