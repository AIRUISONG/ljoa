package com.jgp.ljoa.channel.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.common.pojo.LabelValue;
import com.jgp.common.pojo.TreeBean;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
@RestController
@RequestMapping("/ljoa/channel/ljProjectInfoApiController")
public class LjProjectInfoApiController extends JGPController {

    @Autowired
    private LjProjectInfoService service;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private RelationService relationService;

    @RequestMapping("listAllLjProjectInfo")//渠道整合的
    private Result listAllLjProjectInfo(@UIParam("pager") Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();

        List<LjProjectInfo> ljChannelProjects = service.queryGroupLjProjectInfoByProjectTypeByMan("1", currentAdmin.getId() ,pager);
     /*   List<LjProjectInfo> collect=new ArrayList<>();
        collect= ljChannelProjects.stream().filter(lp -> {
            if (currentAdmin.getId().equals(lp.getChiefUuid())) {
                return true;
            } else if (currentAdmin.getId().equals(lp.getPrjDutyMan())) {
                return true;
            } else if (currentAdmin.getId().equals(lp.getManagerUuid())) {
                return true;
            } else if (currentAdmin.getId().equals(lp.getCaseManager())) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());*/
       /* if("admin".equals(currentAdmin.getUsername())){
            collect=ljChannelProjects;
        }*/
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectName"));
        categories.add(new GridResult.Category("省", "province","LJOA.CHANNEL.PROVINCE"));
        categories.add(new GridResult.Category("市", "city","LJOA.CHANNEL.CITY"));
        categories.add(new GridResult.Category("区", "area","LJOA.CHANNEL.AREA"));
        return ajaxReGrid("gdata", categories, ljChannelProjects, pager);
    }

    @RequestMapping("listAllLjProjectInfoCW")//渠道整合的
    private Result listAllLjProjectInfoCW(@UIParam("pager") Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();

        List<LjProjectInfo> ljChannelProjects = service.queryGroupLjProjectInfoByProjectType("1",pager);

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectName"));
        categories.add(new GridResult.Category("省", "province","LJOA.CHANNEL.PROVINCE"));
        categories.add(new GridResult.Category("市", "city","LJOA.CHANNEL.CITY"));
        categories.add(new GridResult.Category("区", "area","LJOA.CHANNEL.AREA"));
        return ajaxReGrid("gdata", categories, ljChannelProjects, pager);
    }
    @RequestMapping("listGroupLjProjectInfo")//营销的
    private Result listGroupLjProjectInfo(@UIParam("pager")Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();

        List<LjProjectInfo> ljChannelProjects = service.queryGroupLjProjectInfoByProjectTypeByMan("2",currentAdmin.getId(),pager);

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectName"));
//        categories.add(new GridResult.Category("销售经理", "managerUuid"));
        categories.add(new GridResult.Category("省", "province","LJOA.CHANNEL.PROVINCE"));
        categories.add(new GridResult.Category("市", "city","LJOA.CHANNEL.CITY"));
        categories.add(new GridResult.Category("区", "area","LJOA.CHANNEL.AREA"));
        return ajaxReGrid("gdata", categories, ljChannelProjects, pager);
    }
    @RequestMapping("listGroupLjProjectInfoCW")//营销的
    private Result listGroupLjProjectInfoCW(@UIParam("pager")Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();

        List<LjProjectInfo> ljChannelProjects = service.queryGroupLjProjectInfoByProjectType("2",pager);

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectName"));
//        categories.add(new GridResult.Category("销售经理", "managerUuid"));
        categories.add(new GridResult.Category("省", "province","LJOA.CHANNEL.PROVINCE"));
        categories.add(new GridResult.Category("市", "city","LJOA.CHANNEL.CITY"));
        categories.add(new GridResult.Category("区", "area","LJOA.CHANNEL.AREA"));
        return ajaxReGrid("gdata", categories, ljChannelProjects, pager);
    }



    @RequestMapping("saveLjProjectInfo")
    private Result saveLjProjectInfo(LjProjectInfo l){
        LjProjectInfo ljChannelProject = service.saveLjProjectInfo(l);
        return ajaxRe(true);
    }
    @RequestMapping("removeLjProjectInfo/{id}")
    private Result removeLjProjectInfo(@PathVariable String id){
        service.removeLjProjectInfo(id);
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupLjHouseInfoByUUID(id);
        if(ljHouseInfos.size() > 0){
            ljHouseInfoService.remoueGroupLjHouseInfo(ljHouseInfos);
        }
        return ajaxRe(true);
    }

    @RequestMapping("listProjectDrop")
    public Result listProjectDrop(){
        List<LjProjectInfo> ljProjectInfos = service.queryGroupLjProjectInfoByProjectType();
        List<LabelValue> labelValues = ljProjectInfos.stream().map(ljProjectInfo -> {
            return new LabelValue(ljProjectInfo.getProjectName(),ljProjectInfo.getId(),null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",labelValues);
    }
    @RequestMapping("listProjectDropByType/{uuid}")
    public Result listProjectDropByType(@PathVariable String uuid){
        String type="";
        if("d4a1dd4ca37749fc9380f8d61454305c".equals(uuid)){
            type="2";
        }else if("3bc4148f06684cf6bebcf80bbbf2971c".equals(uuid)){
            type = "1";
        }else{
            type="3";
        }
        List<LjProjectInfo> ljProjectInfos = service.queryGroupLjProjectInfoByProjectType(type, null);
        List<LabelValue> labelValues =new ArrayList<>();
        labelValues = ljProjectInfos.stream().map(ljProjectInfo -> {
            return new LabelValue(ljProjectInfo.getProjectName(),ljProjectInfo.getId(),null);
        }).collect(Collectors.toList());
        if("3".equals(type)){
            labelValues = ljProjectInfos.stream().map(ljProjectInfo -> {
                if("1".equals(ljProjectInfo.getProjectType())){
                    return new LabelValue(ljProjectInfo.getProjectName() + " (渠道整合)",ljProjectInfo.getId(),null);
                }else if("2".equals(ljProjectInfo.getProjectType())){
                    return new LabelValue(ljProjectInfo.getProjectName() + " (营销)",ljProjectInfo.getId(),null);
                }else{
                    return new LabelValue(ljProjectInfo.getProjectName(),ljProjectInfo.getId(),null);
                }

            }).collect(Collectors.toList());
        }

        return ajaxReData("ldata",labelValues);
    }


    //渠道项目
    @RequestMapping("listQDProjectName")
    public Result listQDProjectName(){
        List<LjProjectInfo> ljProjectInfos = service.queryGroupLjProjectInfoByProjectTypeQD();
        List<LabelValue> labelValues = ljProjectInfos.stream().map(ljProjectInfo -> {
            return new LabelValue(ljProjectInfo.getProjectName(),ljProjectInfo.getId(),null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",labelValues);
    }

    //营销项目
    @RequestMapping("listYXProjectName")
    public Result listYXProjectName(){
        List<LjProjectInfo> ljProjectInfos = service.queryGroupLjProjectInfoByProjectTypeYX();
        List<LabelValue> labelValues = ljProjectInfos.stream().map(ljProjectInfo -> {
            return new LabelValue(ljProjectInfo.getProjectName(),ljProjectInfo.getId(),null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",labelValues);
    }

    @RequestMapping("listTestTree")//所有项目树
    public List<TreeBean> listTestTree(Model model){

        List<LjProjectInfo> ljProjectInfos = service.queryAllLjProjectInfo();
        List<TreeBean> collect = ljProjectInfos.stream().map(labelValue -> {
            TreeBean treeBean = new TreeBean();
            treeBean.setKey(labelValue.getId());//把链接设置成tree的key
//            treeBean.setIcon((String)labelValue.getExtMap().get("ext2"));
            treeBean.setTitle(labelValue.getProjectName()); //把链接名称设置成tree的title
            treeBean.setBean(labelValue);
            return treeBean;
        }).collect(Collectors.toList());
        return collect;

    }

    @RequestMapping({"exportInfo/{projectId}"})
    @ResponseBody
    public ResponseEntity<byte[]> exportInfo(@PathVariable("projectId") String projectId) throws Exception {
        return service.exportSoreInfo(projectId);
    }
}
