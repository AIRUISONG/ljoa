package com.jgp.ljoa.channel.service;

import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.sys.ui.Pager;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
//渠道整合项目信息
public interface LjProjectInfoService {

    //项目类别查询所有
    List<LjProjectInfo> queryGroupLjProjectInfoByProjectType(String projectType,Pager pager);
    //项目类别登录人查
    List<LjProjectInfo> queryGroupLjProjectInfoByProjectTypeByMan(String projectType,String uuid,Pager pager);
    //查询主键单查
    LjProjectInfo queryOneLjProjectInfoById(String id);
    //保存
    LjProjectInfo saveLjProjectInfo(LjProjectInfo l);
    //删除
    void removeLjProjectInfo(String id);
    //查询所有项目名称，做下拉
    List<LjProjectInfo> queryGroupLjProjectInfoByProjectType();

    /*
    * 查询所有渠道项目
    * */
    List<LjProjectInfo> queryGroupLjProjectInfoByProjectTypeQD();

    /*
    * 查询所有营销项目
    * */
    List<LjProjectInfo> queryGroupLjProjectInfoByProjectTypeYX();

    /**
     * 根据项目名查询
     * @return
     */
    public LjProjectInfo queryByProjectName(String projectName);
    /**
     * 根据项目名查询
     * @return
     */
    public List<LjProjectInfo> queryByProjectNameTo(String projectName);
    /*
       * 查询所有项目
       * */
    List<LjProjectInfo> queryAllLjProjectInfo();
    //条件查找项目
    List<LjProjectInfo> queryGroupProject(LjProjectInfo ljProjectInfo);

    /**
     * 导出信息
     * @param projecUuid
     * @return
     */
    public ResponseEntity<byte[]> exportMarkingInfo(String projecUuid);
    /**
     * 导出渠道信息
     * @param projecUuid
     * @return
     */
    public ResponseEntity<byte[]> exportSoreInfo(String projecUuid);
}
