package com.jgp.ljoa.channel.service;

import com.jgp.ljoa.channel.model.LjChannelCompany;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/8/14.
 */
public interface LjChannelCompanyService {

    //查询所有
    List<LjChannelCompany> queryAllLjChannelCompany();

    //单查
    LjChannelCompany queryOneLjChannelCompany(String id);
    LjChannelCompany queryOneLjChannelCompanyByName(String Name);

    //保存
    LjChannelCompany saveLjChannelCompany(LjChannelCompany ljChannelCompany);


    //删除
    void removeLjChannelCompany(String id);

    //批量导入
    void importData(File file);
}
