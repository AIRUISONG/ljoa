package com.jgp.ljoa.marketing.controller;/**
 * Created by Administrator on 2018/7/23.
 */

import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.util.TimeInterval;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.ljoa.marketing.service.HouseReturnService;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/23
 */
@Controller
@RequestMapping("/ljoa/marketing/houseReturnController")
public class HouseReturnController extends JGPController {
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;

    //跳转到查询已售房源的页面
    @RequestMapping("listAllHouseInfoForHouseReturn")
    public String listAllHouseInfoForHouseReturn(){
        return page("listAllHouseInfoForHouseReturn");
    }

    //跳转到添加退房申请的页面
    @RequestMapping("addHouseReturn/{houseId}")
    public String addHouseReturn(@PathVariable String houseId, Model model){
        //LJOA.EXPENSE
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturnByHouseId(houseId);
        if(Objects.isNull(houseReturn)){
            houseReturn = new HouseReturn();
            houseReturn.setHouseUuid(houseId);
            houseReturn.setApprovalStatus("1");
            houseReturn.setMoneyStatus("1");
            houseReturn.setApplyDate(LocalDate.now());
            houseReturn.setInputMan(adminUserService.getCurrentAdmin().getId());//申请人
            //获取客户信息
            CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(houseId);
            houseReturn.setApplyName(customerInfo.getCustomerName());
            houseReturn.setIdentityCode(customerInfo.getCustomerIdentify());
            houseReturn.setTel(customerInfo.getCustomerTel());
            houseReturn.setReturnMoney(customerInfo.getEarnest());
            houseReturn = houseReturnService.saveHouseReturn(houseReturn);
            model.addAttribute("id",houseReturn.getId());
            reForm(model,"fdata", houseReturn);
            return page("addHouseReturn");
        }else {
            model.addAttribute("id",houseReturn.getId());
            if(!("1".equals(houseReturn.getApprovalStatus()) ||
              "8".equals(houseReturn.getApprovalStatus())
            || "9".equals(houseReturn.getApprovalStatus())
            || "10".equals(houseReturn.getApprovalStatus())
            || "11".equals(houseReturn.getApprovalStatus()))){
                model.addAttribute("flag","0");//不可修改
            }else{
                model.addAttribute("flag","1");//可修改
            }
            reForm(model,"fdata", houseReturn);
            return page("editHouseReturn");
        }
    }

    //跳转到查询所有的退房信息页面
    @RequestMapping("listAllHouseReturn")
    public String listAllHouseReturn(){

        return page("listAllHouseReturn");
    }

    //跳转到退房信息详细页面
    @RequestMapping("showHouseReturn/{id}")
    public String showHouseReturn(@PathVariable String id, Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);
        model.addAttribute("id",houseReturn.getId());
        reForm(model,"fdata",houseReturn);
        return page("showHouseReturn");
    }

    //跳转营销总监退房审批页面（未审批）
    @RequestMapping("listHouseReturnSaleDirector")
    public String listHouseReturnSaleDirector(){
        return page("listHouseReturnSaleDirector");
    }
    //跳转营销总监退房审批页面（已审批）
    @RequestMapping("listHouseReturnSaleDirector1")
    public String listHouseReturnSaleDirector1(){
        return page("listHouseReturnSaleDirector1");
    }

    //营销总监审批退房信息
    @RequestMapping("editHouseReturnSaleDirector/{id}")
    public String editHouseReturnSaleDirector(@PathVariable String id,Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(id, "1", "3");
        String currentUserId = adminUserService.getCurrentAdmin().getId();//当前审核人
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(id);
            approval.setCheckContent("3");
            approval.setCheckType("1");
            approval.setCheckMan(currentUserId);
        }else {
            if("2".equals(houseReturn.getApprovalStatus())){
                approval.setCheckOption(null);
                approval.setCheckResult(null);
                approval.setCheckTime(LocalDate.now());
                approval.setCheckMan(currentUserId);
            }
        }
        approval.setCheckTime(LocalDate.now());
        reForm(model,"afdata",approval);
        reForm(model,"fdata",houseReturn);
        model.addAttribute("houseReturnId",houseReturn.getId());
        model.addAttribute("status",houseReturn.getApprovalStatus());
        return page("editHouseReturnSaleDirector");
    }

    //进度查询
    @RequestMapping("showHouseReturnSpeed/{id}")
    public String showHouseReturnSpeed(@PathVariable String id,Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturnByHouseId(id);
        if(Objects.nonNull(houseReturn)){
            if(Objects.isNull(houseReturn.getApprovalStatus())){
                houseReturn.setApprovalStatus("0");
            }
            if(Objects.isNull(houseReturn.getMoneyStatus())){
                houseReturn.setMoneyStatus("0");
            }
        }else{
            houseReturn = new HouseReturn();
            houseReturn.setApprovalStatus("0");
            houseReturn.setMoneyStatus("0");
        }
        Approval approval1 = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "1", "3");
        Approval approval2 = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "2", "3");
        Approval approval4 = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "4", "3");
        //时间间隔
        String s0 = "";
        String s1 = "";
        String s2 = "";

        //时间
        String date0 = "";//提交时间

        if(Objects.nonNull(houseReturn.getCreateDatetime())){
            date0 = houseReturn.getCreateDatetime().toString().replace("T", " ");
        }


        String date1 = "";//部门审核时间
        String date2 = "";//财务审核时间
        String date4 = "";//总经理审核时间
        if(Objects.nonNull(approval4)){
            LocalDateTime modifyDatetime0 = houseReturn.getCreateDatetime();
            LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
            LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
            LocalDateTime modifyDatetime4 = approval4.getModifyDatetime();
            date1 = approval1.getModifyDatetime().toString().replace("T", " ");
            date2 = approval2.getModifyDatetime().toString().replace("T", " ");
            date4 = approval4.getModifyDatetime().toString().replace("T", " ");
            Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
            s0 = TimeInterval.timeInterval(duration0);
            Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
            s1 = TimeInterval.timeInterval(duration1);
            Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime4);
            s2 = TimeInterval.timeInterval(duration2);
        }else {
                if(Objects.nonNull(approval2)){
                    LocalDateTime modifyDatetime0 = houseReturn.getCreateDatetime();
                    LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                    LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
                    date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                    date2 = approval2.getModifyDatetime().toString().replace("T", " ");
                    Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                    s0 = TimeInterval.timeInterval(duration0);
                    Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                    s1 = TimeInterval.timeInterval(duration1);
                }else {
                    if(Objects.nonNull(approval1)){
                        LocalDateTime modifyDatetime0 = houseReturn.getCreateDatetime();
                        LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                        date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                        Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                        s0 = TimeInterval.timeInterval(duration0);
                    }
                }
            }

        List<String> list = new ArrayList<>();
        list.add(s0);
        list.add(s1);
        list.add(s2);
        List<String> dateList = new ArrayList<>();
        dateList.add(date0);
        dateList.add(date1);
        dateList.add(date2);
        dateList.add(date4);
        model.addAttribute("timeList", list.toArray());
        model.addAttribute("dateList", dateList.toArray());
        model.addAttribute("approvalStatus",houseReturn.getApprovalStatus());
        model.addAttribute("moneyStatus",houseReturn.getMoneyStatus());
        return page("showHouseReturnSpeed");

    }

    //进度查询
    @RequestMapping("showHouseReturnSpeedS/{id}")
    public String showHouseReturnSpeedS(@PathVariable String id,Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);
        if(Objects.nonNull(houseReturn)){
            if(Objects.isNull(houseReturn.getApprovalStatus())){
                houseReturn.setApprovalStatus("0");
            }
            if(Objects.isNull(houseReturn.getMoneyStatus())){
                houseReturn.setMoneyStatus("0");
            }
        }else{
            houseReturn = new HouseReturn();
            houseReturn.setApprovalStatus("0");
            houseReturn.setMoneyStatus("0");
        }
        Approval approval1 = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "1", "3");
        Approval approval2 = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "2", "3");
//        Approval approval3 = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "3", "3");
        Approval approval4 = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "4", "3");
        //时间间隔
        String s0 = "";
        String s1 = "";
        String s2 = "";
//        String s3 = "";
        //时间
        String date0 = "";
        if(Objects.nonNull(houseReturn.getCreateDatetime())){
            date0 = houseReturn.getCreateDatetime().toString().replace("T", " ");
        }
        String date1 = "";
        String date2 = "";
//        String date3 = "";
        String date4 = "";
        if(Objects.nonNull(approval4)){
            LocalDateTime modifyDatetime0 = houseReturn.getCreateDatetime();
            LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
            LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
//            LocalDateTime modifyDatetime3 = approval3.getModifyDatetime();
            LocalDateTime modifyDatetime4 = approval4.getModifyDatetime();
            date1 = approval1.getModifyDatetime().toString().replace("T", " ");
            date2 = approval2.getModifyDatetime().toString().replace("T", " ");
//            date3 = approval3.getModifyDatetime().toString().replace("T", " ");
            date4 = approval4.getModifyDatetime().toString().replace("T", " ");
            Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
            s0 = TimeInterval.timeInterval(duration0);
            Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
            s1 = TimeInterval.timeInterval(duration1);
            Duration duration2 = Duration.between(modifyDatetime2,modifyDatetime4);
            s2 = TimeInterval.timeInterval(duration2);
//            Duration duration3 = Duration.between(modifyDatetime3,modifyDatetime4);
//            s3 = TimeInterval.timeInterval(duration3);
        }else {

                if(Objects.nonNull(approval2)){
                    LocalDateTime modifyDatetime0 = houseReturn.getCreateDatetime();
                    LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                    LocalDateTime modifyDatetime2 = approval2.getModifyDatetime();
                    date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                    date2 = approval2.getModifyDatetime().toString().replace("T", " ");
                    Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                    s0 = TimeInterval.timeInterval(duration0);
                    Duration duration1 = Duration.between(modifyDatetime1,modifyDatetime2);
                    s1 = TimeInterval.timeInterval(duration1);
                }else {
                    if(Objects.nonNull(approval1)){
                        LocalDateTime modifyDatetime0 = houseReturn.getCreateDatetime();
                        LocalDateTime modifyDatetime1 = approval1.getModifyDatetime();
                        date1 = approval1.getModifyDatetime().toString().replace("T", " ");
                        Duration duration0 = Duration.between(modifyDatetime0,modifyDatetime1);
                        s0 = TimeInterval.timeInterval(duration0);
                    }
                }

        }
        List<String> list = new ArrayList<>();
        list.add(s0);
        list.add(s1);
        list.add(s2);
//        list.add(s3);
        List<String> dateList = new ArrayList<>();
        dateList.add(date0);
        dateList.add(date1);
        dateList.add(date2);
//        dateList.add(date3);
        dateList.add(date4);
        model.addAttribute("timeList", list);
        model.addAttribute("dateList", dateList);
        model.addAttribute("approvalStatus",houseReturn.getApprovalStatus());
        model.addAttribute("moneyStatus",houseReturn.getMoneyStatus());
        return page("showHouseReturnSpeed");

    }

}
