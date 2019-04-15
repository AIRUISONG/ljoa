package com.jgp.ljoa.reporter.controller;


import com.jgp.attachment.model.FileInfo;
import com.jgp.attachment.service.DocService;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.ljoa.marketing.service.HouseReturnService;
import com.jgp.ljoa.reporter.bean.MoneyReturn;
import com.jgp.ljoa.reporter.common.Num2Rmb;
import com.jgp.ljoa.reporter.common.PathUtil;
import com.jgp.reporter.ReporterFormat;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/13 0013.
 */
@Controller
@RequestMapping("/ljoa/moneyReturnReportController/report")
public class MoneyReturnReportController extends JGPController {
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private DocService docService;

    @RequestMapping("moneyReturnReport/{id}")
    public String moneyReturnReport(Model model, @PathVariable String id) throws IOException {

        Map map = new HashMap();
        List<MoneyReturn> list = new ArrayList<>();
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);
//        for(HouseReturn houseReturn:houseReturns){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(houseReturn.getHouseUuid());
        LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljHouseInfo.getProjectUuid());
        CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(ljHouseInfo.getId());
        MoneyReturn moneyReturn = new MoneyReturn();
        moneyReturn.setProjectName(ljProjectInfo.getProjectName());
        if(customerInfo!=null&&customerInfo.getEarnestDate()!=null){
            moneyReturn.setTime(customerInfo.getEarnestDate());
        }else{
            moneyReturn.setTime(null);
        }
        moneyReturn.setName(houseReturn.getApplyName());
        moneyReturn.setRoomNo(ljHouseInfo.getBuildingNo()+"-"+ljHouseInfo.getUnitNo()+"-"+ljHouseInfo.getRoomNo());
        moneyReturn.setRoomArea(ljHouseInfo.getRoomArea());
        moneyReturn.setRefundsMoney(houseReturn.getReturnMoney());
        moneyReturn.setTrueRefundsMoney(houseReturn.getActuaLReturnMoney());
        moneyReturn.setTrueMoney(customerInfo.getEarnest());
        moneyReturn.setRemarks(houseReturn.getRemark());
        moneyReturn.setAllHouseMoney(.0f);
        moneyReturn.setSource(houseReturn.getSource());
        list.add(moneyReturn);
//        }

        Resource resource = new ClassPathResource("/static/images/ljlogo.jpg");
        InputStream i = resource.getInputStream();
        try {

            map.put("image", i);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }


        Resource resource2 = new ClassPathResource("/static/images/ysh.png");
        InputStream i2 = resource2.getInputStream();
        try {
            map.put("backgroup",i2);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileUrl1 = new PathUtil().getWebInfPath() + "classes/jasper/";
        map.put("subPath",fileUrl1);
        map.put("year", LocalDate.now().getYear());
        map.put("month", LocalDate.now().getMonth().getValue());
        map.put("day", LocalDate.now().getDayOfMonth());
        List<FileInfo> fileInfos = docService.queryFiles(houseReturn.getId(),"com.jgp.ljoa.marketing.model.HouseReturn");//对应附件数量
        map.put("fileNum", fileInfos.size());//附件
        map.put("organization", "营销");
        Num2Rmb num = new Num2Rmb();
        map.put("uppercase", num.toHanStr(houseReturn.getReturnMoney()));
        map.put("numMoney", houseReturn.getReturnMoney());
        map.put("name", houseReturn.getApplyName());
        map.put("accountNumber", houseReturn.getBankCard());
        map.put("openingBank", houseReturn.getBank());
        map.put("generalManager", "");
//        map.put("viceGeneralManager", "");
        map.put("finance", "");
        map.put("departmentHead", "");
        map.put("applicant", "");
        map.put("remarks", "");
        map.put("totalTrueMoney", moneyReturn.getTrueMoney());
        map.put("totalRefundsMoney", moneyReturn.getRefundsMoney());
        map.put("totalTrueRefundsMoney", moneyReturn.getTrueRefundsMoney());
//        map.put("list", list);
        return jasperReporter(model, list, map, "moneyReturnReport", ReporterFormat.PDF);
    }
}
