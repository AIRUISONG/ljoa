package com.jgp.ljoa.marketing.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.common.pojo.TreeBean;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/ljoa/marketing/ljHouseInfoApiYXController")
public class LjHouseInfoApiYXController extends JGPController {
    @Autowired
    private LjHouseInfoService service;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("listTestTree")//树
    public List<TreeBean> listTestTree(){
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectType("2",null);
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();

//        List<LjProjectInfo> ljChannelProjects = service.queryGroupLjProjectInfoByProjectType("2",pager);
        List<LjProjectInfo> collect=new ArrayList<>();
        collect= ljProjectInfos.stream().filter(lp -> {
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
        }).collect(Collectors.toList());

        return collect.stream().map(labelValue -> {
            TreeBean treeBean =  new TreeBean();
            treeBean.setKey(labelValue.getId());//把链接设置成tree的key
//            treeBean.setIcon((String)labelValue.getExtMap().get("ext2"));
            treeBean.setTitle(labelValue.getProjectName()); //把链接名称设置成tree的title
            treeBean.setBean(labelValue);
            return treeBean;
        }).collect(Collectors.toList());
    }

    @RequestMapping("listTestTreeBoss")//树
    public List<TreeBean> listTestTreeBoss(){
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectType("2",null);
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();

//        List<LjProjectInfo> ljChannelProjects = service.queryGroupLjProjectInfoByProjectType("2",pager);
    /*    List<LjProjectInfo> collect=new ArrayList<>();
        collect= ljProjectInfos.stream().filter(lp -> {
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

        return ljProjectInfos.stream().map(labelValue -> {
            TreeBean treeBean =  new TreeBean();
            treeBean.setKey(labelValue.getId());//把链接设置成tree的key
//            treeBean.setIcon((String)labelValue.getExtMap().get("ext2"));
            treeBean.setTitle(labelValue.getProjectName()); //把链接名称设置成tree的title
            treeBean.setBean(labelValue);
            return treeBean;
        }).collect(Collectors.toList());
    }
    //查询已售、营销房源
    @RequestMapping("listHouseInfoSold")
    public Result listHouseInfoSold(@UIParam("pager") Pager pager, LjHouseInfo ljHouseInfo) {
        ljHouseInfo.setHouseType("2");
        List<LjHouseInfo> ljHouseInfoList = service.queryHouseInfoSold(ljHouseInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
//        categories.add(new GridResult.Category("溢价金额(元)", "premium"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfoList, pager);
    }

    //已结佣营销房源
    @RequestMapping("listHouseInfoSoldYES")
    public Result listHouseInfoSoldYES(@UIParam("pager") Pager pager, LjHouseInfo ljHouseInfo) {
        ljHouseInfo.setHouseType("2");
        ljHouseInfo.setCompanyChargeStatus("2");//已结佣
        List<LjHouseInfo> ljHouseInfoList = service.queryHouseInfoSold(ljHouseInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
//        categories.add(new GridResult.Category("溢价金额(元)", "premium"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfoList, pager);
    }



}
