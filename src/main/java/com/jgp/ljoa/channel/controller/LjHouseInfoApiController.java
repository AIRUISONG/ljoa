package com.jgp.ljoa.channel.controller;



//import com.jgp.doc.utils.DocUtil;

import com.jgp.attachment.utils.DocUtil;
import com.jgp.common.annotation.UIParam;
import com.jgp.common.pojo.TreeBean;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.repository.LjChannelCompanyRepository;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
@RestController
@RequestMapping("/ljoa/channel/ljHouseInfoApiController")
public class LjHouseInfoApiController extends JGPController {
    @Autowired
    private LjHouseInfoService service;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("listAllLjHouseInfo/{id}")//整合
    public Result listAllLjHouseInfo(@PathVariable String id,Model model,LjHouseInfo ljHouseInfo,@UIParam("pager") Pager pager){
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoByUUId(id,ljHouseInfo,"1",pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("房源归属", "houseType","LJOA.CHANNEL.HOUSETYPE"));
//        categories.add(new GridResult.Category("销售价格", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));
        model.addAttribute("uuid",id);
        return ajaxReGrid("gdata",categories,ljHouseInfos,pager);
    }
    @RequestMapping("listGroupLjHouseInfo/{id}")//营销
    public Result listGroupLjHouseInfo(@PathVariable String id,LjHouseInfo ljHouseInfo,Model model,@UIParam("pager") Pager pager){
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoByUUId(id,ljHouseInfo,"2",pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
//        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));

        return ajaxReGrid("gdata", categories, ljHouseInfos, pager);
    }
    @RequestMapping("saveLjHouseInfo")//保存
    public Result saveLjHouseInfo(LjHouseInfo ljHouseInfo){
        LjHouseInfo ljHouseInfo1 = service.saveLjHouseInfo(ljHouseInfo);
        return ajaxRe(true);
    }
/*    //定价
    @RequestMapping("editLjHouseInfoCommission")//
    public Result editLjHouseInfoCommission( LjHouseInfo ljHouseInfo){

    }*/
    @RequestMapping("addLjHouseInfo")//添加(唯一性判定)
    public Result addLjHouseInfo(LjHouseInfo ljHouseInfo){
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoByUUID(ljHouseInfo.getProjectUuid());//同项目下的房源
        boolean flag=true;
        for (LjHouseInfo l:ljHouseInfos) {
            //楼号 单元 房号相同的
            if(l.getBuildingNo().equals(ljHouseInfo.getBuildingNo())
                    &&l.getUnitNo().equals(ljHouseInfo.getUnitNo())
                    &&l.getRoomNo().equals(ljHouseInfo.getRoomNo())){
                flag=false;
            }
        }
        if(flag){
            LjHouseInfo ljHouseInfo1 = service.saveLjHouseInfo(ljHouseInfo);//添加房源
            LjHouseSaleInfo ljHouseSaleInfo=new LjHouseSaleInfo();//对应房源销售信息
            LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljHouseInfo1.getProjectUuid());//对应项目
            ljHouseSaleInfo.setHouseUuid(ljHouseInfo1.getId());//对应房源主键
            ljHouseSaleInfo.setChargeStatus("0");
            ljHouseSaleInfo.setCaseManager(ljProjectInfo.getCaseManager());//案场经理
            ljHouseSaleInfo.setChannelManager(ljProjectInfo.getManagerUuid());//渠道经理
            ljHouseSaleInfo.setChannelChief(ljProjectInfo.getChiefUuid());//渠道总监
            ljHouseSaleInfo.setAreaManage(ljProjectInfo.getPrjDutyMan());//区域经理
            ljHouseSaleInfoService.saveLjHouseSaleInfo(ljHouseSaleInfo);//添加房源销售信息
        }
        return ajaxRe(flag);
    }
    @RequestMapping("removeLjHouseInfo/{id}")//删除
    private Result removeLjHouseInfo(@PathVariable String id){
        service.removeLjHouseInfo(id);
        return ajaxRe(true);
    }
    @RequestMapping("listTestTree")//树
    public List<TreeBean> listTestTree(Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectType("1",null);
        List<LjProjectInfo> collect = ljProjectInfos.stream().filter(lp -> {
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
        if("admin".equals(currentAdmin.getUsername())){
            collect=ljProjectInfos;
        }
        List<TreeBean> collect1 = collect.stream().map(labelValue -> {
            if(labelValue.getProjectName() != null){
                TreeBean treeBean = new TreeBean();
                treeBean.setKey(labelValue.getId());//把链接设置成tree的key
//            treeBean.setIcon((String)labelValue.getExtMap().get("ext2"));
                treeBean.setTitle(labelValue.getProjectName()); //把链接名称设置成tree的title
                treeBean.setBean(labelValue);
                return treeBean;
            }else{
                return null;
            }

        }).collect(Collectors.toList());
        return collect1;

    }

    @RequestMapping("listTestTreeMarking")//树
    public List<TreeBean> listTestTreeMarking(Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectType("2",null);

        List<TreeBean> collect1 = ljProjectInfos.stream().map(labelValue -> {
            TreeBean treeBean = new TreeBean();
            treeBean.setKey(labelValue.getId());//把链接设置成tree的key
//            treeBean.setIcon((String)labelValue.getExtMap().get("ext2"));
            treeBean.setTitle(labelValue.getProjectName()); //把链接名称设置成tree的title
            treeBean.setBean(labelValue);
            return treeBean;
        }).collect(Collectors.toList());
        return collect1;

    }
    @RequestMapping("listTestTreeCW")//树
    public List<TreeBean> listTestTreeCW(Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectType("1",null);

        List<TreeBean> collect1 = ljProjectInfos.stream().filter(lp -> {
            if(StringUtils.isNotEmpty(lp.getProjectName())){
                return true;
            }else{
                return false;
            }
        }).map(labelValue -> {
            TreeBean treeBean = new TreeBean();
            treeBean.setKey(labelValue.getId());//把链接设置成tree的key
//            treeBean.setIcon((String)labelValue.getExtMap().get("ext2"));
            treeBean.setTitle(labelValue.getProjectName()); //把链接名称设置成tree的title
            treeBean.setBean(labelValue);
            return treeBean;
        }).collect(Collectors.toList());
        return collect1;

    }
    @RequestMapping("listTestTreeBoss")//树
    public List<TreeBean> listTestTreeBoss(Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectType("1",null);

        List<TreeBean> collect1 = ljProjectInfos.stream().filter(lp -> {
            if(StringUtils.isNotEmpty(lp.getProjectName())){
                return true;
            }else{
                return false;
            }
        }).map(labelValue -> {
            TreeBean treeBean = new TreeBean();
            treeBean.setKey(labelValue.getId());//把链接设置成tree的key
//            treeBean.setIcon((String)labelValue.getExtMap().get("ext2"));
            treeBean.setTitle(labelValue.getProjectName()); //把链接名称设置成tree的title
            treeBean.setBean(labelValue);
            return treeBean;
        }).collect(Collectors.toList());
        return collect1;

    }
    //查询已售、渠道整合房源
    @RequestMapping("listHouseInfoSold")
    public Result listHouseInfoSold(@UIParam("pager") Pager pager,LjHouseInfo ljHouseInfo) {
        ljHouseInfo.setHouseType("1");
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
//        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("房源归属", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("销售价格", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfoList, pager);
    }
    //查询房源
    @RequestMapping("listGroupLjHouseInfoByStatus/{id}")
    public Result listGroupLjHouseInfoByStatus(@PathVariable String id,Model model,LjHouseInfo ljHouseInfo,@UIParam("pager") Pager pager){
        List<LjHouseInfo> ljHouseInfos = service.queryGroupHouseInfoSoldByDateTime(id,ljHouseInfo,"1",pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
//        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        model.addAttribute("uuid",id);
        return ajaxReGrid("gdata",categories,ljHouseInfos,pager);
    }


    @RequestMapping("importHouse")//渠道批量导入
    private Result importHouse(String fileId) throws Exception {
        List<String> fileIds = new ArrayList<String>();
        fileIds.add(fileId);
        List<File> fileList = DocUtil.getIOFiles(fileIds);
        for (File file : fileList) {
            service.importData(file);
        }

        return ajaxRe(true);
    }

    @RequestMapping("importMarketingHouse")//营销批量导入
    private Result importMarketingHouse(String fileId) throws Exception {
        List<String> fileIds = new ArrayList<String>();
        fileIds.add(fileId);
        List<File> fileList = DocUtil.getIOFiles(fileIds);
        for (File file : fileList) {
            service.importMarketingHouse(file);
        }
        return ajaxRe(true);
    }
    @RequestMapping("importMarketingHouseMoney")//营销批量定价
    private Result importMarketingHouseMoney(String fileId) throws Exception {
        List<String> fileIds = new ArrayList<String>();
        fileIds.add(fileId);
        List<File> fileList = DocUtil.getIOFiles(fileIds);
        for (File file : fileList) {
            service.importMarketingHouseMoney(file);
        }
        return ajaxRe(true);
    }


    @RequestMapping("testDownload")
    @ResponseBody
    public ResponseEntity<byte[]> testDownload(HttpServletResponse res) {

        String fileName = "营销模板.xls";
        byte[] body=null;
        InputStream in=getClass().getClassLoader().getResourceAsStream("static/text/" + fileName);
        HttpHeaders headers = new HttpHeaders();
        try {
            body=new byte[in.available()];
            in.read(body);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName + "", "UTF-8"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpStatus statusCode=HttpStatus.OK;

        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body,headers,statusCode);
        return response;

    }

    @RequestMapping("testDownloadQD")
    @ResponseBody
    public ResponseEntity<byte[]> testDownloadQD(HttpServletResponse res) {

        String fileName = "渠道整合.xls";
        byte[] body=null;
        InputStream in=getClass().getClassLoader().getResourceAsStream("static/text/" + fileName);
        HttpHeaders headers = new HttpHeaders();
        try {
            body=new byte[in.available()];
            in.read(body);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName + "", "UTF-8"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpStatus statusCode=HttpStatus.OK;

        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body,headers,statusCode);
        return response;

    }
    @RequestMapping("testDownloadYG")
    @ResponseBody
    public ResponseEntity<byte[]> testDownloadYG(HttpServletResponse res) {

        String fileName = "员工模板.xls";
        byte[] body=null;
        InputStream in=getClass().getClassLoader().getResourceAsStream("static/text/" + fileName);
        HttpHeaders headers = new HttpHeaders();
        try {
            body=new byte[in.available()];
            in.read(body);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName + "", "UTF-8"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpStatus statusCode=HttpStatus.OK;

        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body,headers,statusCode);
        return response;

    }
    @RequestMapping("testDownloadThere")
    @ResponseBody
    public ResponseEntity<byte[]> testDownloadThere(HttpServletResponse res) {

        String fileName = "第三方公司.xls";
        byte[] body=null;
        InputStream in=getClass().getClassLoader().getResourceAsStream("static/text/" + fileName);
        HttpHeaders headers = new HttpHeaders();
        try {
            body=new byte[in.available()];
            in.read(body);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName + "", "UTF-8"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpStatus statusCode=HttpStatus.OK;

        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body,headers,statusCode);
        return response;

    }

    @RequestMapping("listAllLjHouseInfoCW/{id}")//财务渠道第三方结款信息
    public Result listAllLjHouseInfoCW(@PathVariable String id,LjHouseSaleInfo ljH,Model model,@UIParam("pager") Pager pager){
        LjHouseInfo ljHouseInfo=new LjHouseInfo();
        ljHouseInfo.setStatus("2");//已售房源
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoByUUId(id,ljHouseInfo,"1",pager);
        //已售的拥有第三方渠道的渠道房源
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(lh -> {
            LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(lh.getProjectUuid());//项目
            if ("1".equals(ljProjectInfo.getProjectType())) {//整合
                LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(lh.getId());
                if (StringUtils.isNotEmpty(ljHouseSaleInfo.getChannelCompany())) {//渠道公司不为null
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());

        List<LjHouseSaleInfo> ljHouseSaleInfos=new ArrayList<>();
        for ( LjHouseInfo hh:collect) {
            LjHouseSaleInfo ljHouseSaleInfo= ljHouseSaleInfoService.queryOneLjHouseSaleInfoByType(hh.getId(),ljH,pager);
            if(Objects.nonNull(ljHouseSaleInfo.getId())){
                ljHouseSaleInfos.add(ljHouseSaleInfo);
            }
        }
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("渠道公司", "channelCompany", LjChannelCompanyRepository.class,"companyName"));
        categories.add(new GridResult.Category("渠道佣金金额", "channelChargeMoney"));
        categories.add(new GridResult.Category("付款方式", "payType","LJOA.CHANNEL.PAY_TYPE"));
        categories.add(new GridResult.Category("渠道税后佣金金额", "channelAfterTaxMoney"));
        categories.add(new GridResult.Category("渠道佣金状态", "channelChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));

        return ajaxReGrid("gdata", categories,ljHouseSaleInfos, pager);
    }

    @RequestMapping("listAllLjHouseInfoCWYX/{id}")//财务营销第三方结款信息
    public Result listAllLjHouseInfoCWYX(@PathVariable String id,MarketingChargeInfo mark,@UIParam("pager") Pager pager) {
        LjHouseInfo ljHouseInfo = new LjHouseInfo();
        ljHouseInfo.setStatus("2");//已售房源
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoByUUId(id, ljHouseInfo, "2", pager);
        //已售的拥有第三方渠道的营销房源
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(lh -> {
            LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(lh.getProjectUuid());//项目
            if ("2".equals(ljProjectInfo.getProjectType())) {//营销
                MarketingChargeInfo marketingChargeInfo = marketingChargeInfoService.queryOneMarketingChargeInfoByChargeType("5", lh.getId());
                if (Objects.nonNull(marketingChargeInfo.getId())) {//渠道公司不为null
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());

        List<MarketingChargeInfo> marketingChargeInfos=new ArrayList<>();
        for ( LjHouseInfo hh:collect) {
            MarketingChargeInfo marketingChargeInfo = marketingChargeInfoService.queryMarketingChargeInfoByChargeTarget(hh.getId(),mark,pager);
            if(Objects.nonNull(marketingChargeInfo.getId())){
                marketingChargeInfos.add(marketingChargeInfo);
            }
        }
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("渠道公司", "chargeTargetName"));
        categories.add(new GridResult.Category("渠道佣金金额", "chargeMoney"));
        categories.add(new GridResult.Category("佣金状态", "chargeStatus","LJ_CUSTOMER_INFO.CHARGESTATUS"));
        return ajaxReGrid("gdata", categories,marketingChargeInfos, pager);
    }


    @RequestMapping("listGroudHouseInfo")//已售已结佣（已收款）
    public Result listGroudHouseInfo(Model model,LjHouseInfo ljHouseInfo,@UIParam("pager") Pager pager){
        ljHouseInfo.setCompanyChargeStatus("2");//已结佣
        ljHouseInfo.setHouseType("1");//渠道房源
        List<LjHouseInfo> ljHouseInfo1 = service.queryGroupLjHouseInfoINFO(ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("公司佣金金额", "companyChargeMoney"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("房源归属", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));
        return ajaxReGrid("gdata",categories,ljHouseInfo1,pager);
    }

    @RequestMapping("listGroudHouseInfoNO")//已售已结佣（应收款）
    public Result listGroudHouseInfoNO(Model model,LjHouseInfo ljHouseInfo,@UIParam("pager") Pager pager){
        ljHouseInfo.setStatus("2");//已售
        ljHouseInfo.setHouseType("1");//渠道房源
        List<LjHouseInfo> ljHouseInfo1 = service.queryGroupLjHouseInfoINFO(ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("公司佣金金额", "companyChargeMoney"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("房源归属", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));

        return ajaxReGrid("gdata",categories,ljHouseInfo1,pager);
    }

    @RequestMapping("listGroudHouseInfoByProjectUuid/{id}")//对应项目房源信息
    public Result listGroudHouseInfoByProjectUuid(@PathVariable String id, LjHouseInfo ljHouseInfo, Model model, @UIParam("pager") Pager pager){
        ljHouseInfo.setProjectUuid(id);

        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoByUUID(id,ljHouseInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("公司佣金金额", "companyChargeMoney"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("房源归属", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));
        return ajaxReGrid("gdata",categories,ljHouseInfos,pager);
    }
}
