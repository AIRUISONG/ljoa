package com.jgp.ljoa.common.service;

import com.jgp.ljoa.common.model.LjNoticeInfo;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * Created by Administrator on 2018/9/11.
 */
public interface LjNoticeInfoService {

    //保存
    LjNoticeInfo saveLjNoticeInfo(LjNoticeInfo ljNoticeInfo);
    //删除
    void removeOneLjNoticeInfoById(String id);
    //条件查询
    List<LjNoticeInfo> queryGroupLjNoticeInfo(LjNoticeInfo ljNoticeInfo,Pager pager);
    //单查
    LjNoticeInfo queryOneLjNoticeInfo(String id);
    //条件查询
    List<LjNoticeInfo> queryGroupLjNoticeInfoByType(LjNoticeInfo ljNoticeInfo,List<String> type,Pager pager);

}
