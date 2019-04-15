package com.jgp.ljoa.marketing.service;/**
 * Created by Administrator on 2018/7/23.
 */

import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/23
 */
public interface HouseReturnService {

    /*
    * 保存退房申请
    * */
    HouseReturn saveHouseReturn(HouseReturn houseReturn);

    /*
    * 查询单个退房申请
    * */
    HouseReturn queryOneHouseReturn(String id);

    /*
    * 根据房源id查询退房申请
    * */
    HouseReturn queryOneHouseReturnByHouseId(String houseId);

    /*
    * 条件查询退房申请
    * */
    List<HouseReturn> queryGroupHouseReturn(HouseReturn houseReturn, Pager pager);

    /*
    * 删除单个退房申请
    * */
    void removeOneHouseReturn(String id);

    //条件查询退房申请（多个状态）
    List<HouseReturn> queryGroupHouseReturnInfo(List<String> list,HouseReturn houseReturn, Pager pager);

    //条件查询退房申请（对应状态）
    List<HouseReturn> queryGroupHouseReturnInfoByHouseUuid(List<String> list,HouseReturn houseReturn, Pager pager);

    //多id查询
    List<HouseReturn> queryGroupHouseReturnInfoById(List<String> id,Pager pager);
}
