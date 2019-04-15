package com.jgp.ljoa.com.controller;/**
 * Created by Administrator on 2018/7/8.
 */

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/8
 */
@RestController
@RequestMapping("/ljoa/com/marketingHouseInfoApiController")
public class MarketingHouseInfoApiController extends JGPController {
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;

    /*
    * 查询营销的房源（未售  销售底价为空 定价）
    * */
    @RequestMapping("listMarketingHouseInfo/{id}")
    private Result listMarketingHouseInfo(@PathVariable String id,LjHouseInfo ljHouseInfo, @UIParam("pager")Pager pager){
        ljHouseInfo.setProjectUuid(id);
        List<LjHouseInfo> ljHouseInfos = marketingSaleInfoService.queryMarketingDutyManQueryHouseSaleInfo(ljHouseInfo,pager);
        List<LjHouseInfo> ljHouseInfos1 = new ArrayList<>(ljHouseInfos);
//        ljHouseInfos1.sort(new Comparator<LjHouseInfo>() {
//            @Override
//            public int compare(LjHouseInfo o1, LjHouseInfo o2) {
//                if(Integer.valueOf(o1.getBuildingNo())>Integer.valueOf(o2.getBuildingNo())){
//                    return 1;
//                }else if(Integer.valueOf(o1.getBuildingNo()) == Integer.valueOf(o2.getBuildingNo())){
//                    if(Integer.valueOf(o1.getUnitNo()) > Integer.valueOf(o2.getUnitNo())){
//                        return 1;
//                    }else if(Integer.valueOf(o1.getUnitNo()) == Integer.valueOf(o2.getUnitNo())){
//                        if(Integer.valueOf(o1.getRoomNo()) > Integer.valueOf(o2.getRoomNo())){
//                            return 1;
//                        }else{
//                            return 0;
//                        }
//                    }else{
//                        return 0;
//                    }
//                }else{
//                    return -1;
//                }
//            }
//        });


        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos, pager);
    }
}
