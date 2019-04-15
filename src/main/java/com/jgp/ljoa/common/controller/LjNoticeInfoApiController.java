package com.jgp.ljoa.common.controller;

import com.jgp.attachment.model.ArticleContent;
import com.jgp.attachment.service.ArticleContentService;
import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.common.model.LjNoticeInfo;
import com.jgp.ljoa.common.service.LjNoticeInfoService;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/9/11.
 */
/*
作者  SSF
时间   2018/9/11
*/

@RestController
@RequestMapping("/ljoa/common/LjNoticeInfoApiController")
public class LjNoticeInfoApiController extends JGPController {

    @Autowired
    private LjNoticeInfoService ljNoticeInfoService;
    @Autowired
    private ArticleContentService articleContentService;

    @RequestMapping("/saveLjNoticeInfo")//保存修改
    public Result saveLjNoticeInfo( LjNoticeInfo  ljNoticeInfo){
        ljNoticeInfo=ljNoticeInfoService.saveLjNoticeInfo(ljNoticeInfo);
        boolean flag = false;
        if (Objects.nonNull(ljNoticeInfo)) {
            flag = true;
        }
        return ajaxRe(flag).addData("id",ljNoticeInfo.getId());
    }
    @RequestMapping("/removeljNoticeInfoById/{id}")//删除
    public Result removeljNoticeInfoById(@PathVariable("id") String id){
        boolean flag = false;
        try {
            flag = true;
            ljNoticeInfoService.removeOneLjNoticeInfoById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajaxRe(flag);
    }

    @RequestMapping("/editToVoidljNoticeInfo/{id}")//作废数据
    public Result editToVoidljNoticeInfo(@PathVariable("id") String id){
        LjNoticeInfo ljNoticeInfo = ljNoticeInfoService.queryOneLjNoticeInfo(id);
        ljNoticeInfo.setBoticeStatus("3");//作废
        ljNoticeInfo=ljNoticeInfoService.saveLjNoticeInfo(ljNoticeInfo);
        boolean flag = false;
        if (Objects.nonNull(ljNoticeInfo)) {
            flag = true;
        }
        return ajaxRe(flag);
    }
    @RequestMapping("/editReleaseljNoticeInfo/{id}")//发布数据
    public Result editReleaseljNoticeInfo(@PathVariable("id") String id){
        LjNoticeInfo ljNoticeInfo = ljNoticeInfoService.queryOneLjNoticeInfo(id);
        ljNoticeInfo.setBoticeStatus("2");//已发布
        ljNoticeInfo=ljNoticeInfoService.saveLjNoticeInfo(ljNoticeInfo);
        boolean flag = false;
        if (Objects.nonNull(ljNoticeInfo)) {
            flag = true;
        }
        return ajaxRe(flag);
    }
    @RequestMapping("/listGroupLjNoticeInfo")//查询
    public Result listGroupLjNoticeInfo(LjNoticeInfo LjNoticeInfo,@UIParam("pager") Pager pager){
        List<LjNoticeInfo> ljNoticeInfos = ljNoticeInfoService.queryGroupLjNoticeInfo(LjNoticeInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("标题", "title"));
        categories.add(new GridResult.Category("信息类别", "noticeType","LJ_NOTICE_INFO.NOTICE_TYPE"));
        categories.add(new GridResult.Category("信息状态", "boticeStatus","LJ_NOTICE_INFO.NOTICE_STATUS"));
        categories.add(new GridResult.Category("发布日期", "pushTime"));
        return ajaxReGrid("gdata",categories,ljNoticeInfos,pager);
    }

    @RequestMapping("/listGroupLjNoticeInfoJournalism")//查询新闻已发布
    public Result listGroupLjNoticeInfoJournalism(){
        LjNoticeInfo LjNoticeInfo=new LjNoticeInfo();
        LjNoticeInfo.setNoticeType("1");//新闻类别
        LjNoticeInfo.setBoticeStatus("2");//已发布
        List<LjNoticeInfo> ljNoticeInfos = ljNoticeInfoService.queryGroupLjNoticeInfo(LjNoticeInfo,null);
        ljNoticeInfos = ljNoticeInfos.stream().map(ljNoticeInfo -> {
            ArticleContent articleContent = articleContentService.queryContentById(ljNoticeInfo.getContent());
            String content = articleContent.getContent();
            String rep = content.replaceAll("\\[[^\\]]*?\\]","");
            String replace = rep.replaceAll("\r|\n", "");
            String s = replace.replaceAll("\\<[^\\>]*?\\>", "");
            ljNoticeInfo.setContent(s);
            return ljNoticeInfo;
        }).collect(Collectors.toList());
        return ajaxReData("Journalism",ljNoticeInfos);
    }

    @RequestMapping("/listGroupLjNoticeInfoNotice")//查询公告已发布
    public Result listGroupLjNoticeInfoNotice(){
        LjNoticeInfo LjNoticeInfo=new LjNoticeInfo();
//        LjNoticeInfo.setNoticeType("2");//公告类别
        LjNoticeInfo.setBoticeStatus("2");//已发布
        List<String> type=new ArrayList<>();
        type.add("2");
        type.add("3");
        List<LjNoticeInfo> ljNoticeInfos = ljNoticeInfoService.queryGroupLjNoticeInfoByType(LjNoticeInfo,type,null);
        ljNoticeInfos = ljNoticeInfos.stream().map(ljNoticeInfo -> {
            ArticleContent articleContent = articleContentService.queryContentById(ljNoticeInfo.getContent());
            String content = articleContent.getContent();
            String rep = content.replaceAll("\\[[^\\]]*?\\]","");
            String replace = rep.replaceAll("\r|\n", "");
            String s = replace.replaceAll("\\<[^\\>]*?\\>", "");
            ljNoticeInfo.setContent(s);
            return ljNoticeInfo;
        }).collect(Collectors.toList());
        return ajaxReData("Notice",ljNoticeInfos);
    }



    @RequestMapping("/listGroupLjNoticeInfoNoticeY")//查询公告已发布LIST
    public Result listGroupLjNoticeInfoNoticeY(@UIParam("pager")Pager pager){
        LjNoticeInfo LjNoticeInfo=new LjNoticeInfo();
//        LjNoticeInfo.setNoticeType("2");//公告类别
        LjNoticeInfo.setBoticeStatus("2");//已发布
        List<String> type=new ArrayList<>();
        type.add("2");
        type.add("3");
        List<LjNoticeInfo> ljNoticeInfos = ljNoticeInfoService.queryGroupLjNoticeInfoByType(LjNoticeInfo,type,null);
//        List<LjNoticeInfo> ljNoticeInfos = ljNoticeInfoService.queryGroupLjNoticeInfo(LjNoticeInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("标题", "title"));
        categories.add(new GridResult.Category("信息类别", "noticeType","LJ_NOTICE_INFO.NOTICE_TYPE"));
        categories.add(new GridResult.Category("信息状态", "boticeStatus","LJ_NOTICE_INFO.NOTICE_STATUS"));
        categories.add(new GridResult.Category("发布日期", "pushTime"));
        return ajaxReGrid("gdata",categories,ljNoticeInfos,pager);
    }
    @RequestMapping("/listGroupLjNoticeInfoJournalismY")//查询新闻已发布LIST
    public Result listGroupLjNoticeInfoJournalismY(@UIParam("pager")Pager pager){
        LjNoticeInfo LjNoticeInfo=new LjNoticeInfo();
        LjNoticeInfo.setNoticeType("1");//新闻类别
        LjNoticeInfo.setBoticeStatus("2");//已发布
        List<LjNoticeInfo> ljNoticeInfos = ljNoticeInfoService.queryGroupLjNoticeInfo(LjNoticeInfo,pager);

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("标题", "title"));
        categories.add(new GridResult.Category("信息类别", "noticeType","LJ_NOTICE_INFO.NOTICE_TYPE"));
        categories.add(new GridResult.Category("信息状态", "boticeStatus","LJ_NOTICE_INFO.NOTICE_STATUS"));
        categories.add(new GridResult.Category("发布日期", "pushTime"));
        return ajaxReGrid("gdata",categories,ljNoticeInfos,pager);
    }

}
