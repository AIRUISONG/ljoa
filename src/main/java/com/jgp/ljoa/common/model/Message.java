package com.jgp.ljoa.common.model;/**
 * Created by Administrator on 2018/10/11.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jgp.common.annotation.UI;
import com.jgp.common.persistence.UUIDModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 项目   bank
 * 作者   liujinxu
 * 时间   2018/10/11
 */
@UI
@Entity
@Table(name = "LJ_MESSAGE")//消息
public class Message extends UUIDModel {

    public final static String MESSAGE_TYPE_1 = "1";//佣金反馈
    public final static String MESSAGE_TYPE_2 = "2";//佣金审批
    public final static String MESSAGE_TYPE_3 = "3";//退房反馈
    public final static String MESSAGE_TYPE_4 = "4";//退房审批
    public final static String MESSAGE_TYPE_5 = "5";//报销反馈
    public final static String MESSAGE_TYPE_6 = "6";//报销审批
    public final static String MESSAGE_TYPE_7 = "7";//佣金发放
    public final static String MESSAGE_TYPE_8 = "8";//备用金审批

    //退房反馈
    public final static String LINK_URL_1 = "/ljoa/marketing/houseReturnController/listAllHouseInfoForHouseReturn";
    //退房总监审批 & 反馈
    public final static String LINK_URL_2 = "/ljoa/marketing/houseReturnController/listHouseReturnSaleDirector";
    //退房财务/会计审批 & 反馈
    public final static String LINK_URL_3 = "/ljoa/expense/ljExpenseCWController/listHouseReturnFinanceNO";
    //退房副总审批 & 反馈
    public final static String LINK_URL_4 = "/ljoa/com/houseReturnFZController/listGroupHouseReturnFZ";
    //退房总经理审批
    public final static String LINK_URL_5 = "/ljoa/com/houseReturnZJLController/listGroupHouseReturnZJLNO";
    //退房打款
    public final static String LINK_URL_6 = "/ljoa/expense/houseReturnMakeMoneyController/listAllHouseReturnMakeMoney";

    //报销反馈
    public final static String LINK_URL_7 = "/ljoa/expense/ljExpenseController/listAllLjExpenseByAdminUserId";
    //报销营销总监审批 & 反馈
    public final static String LINK_URL_8 = "/ljoa/com/ljExpenseBMController/listAllLjExpenseBMYX";
    //报销渠道总监审批 & 反馈
    public final static String LINK_URL_9 = "/ljoa/com/ljExpenseBMController/listAllLjExpenseBMQDNO";
    //报销财务审批 & 反馈
    public final static String LINK_URL_10 = "/ljoa/expense/ljExpenseCWController/listAllLjExpenseCWNO";
    //报销副总审批 & 反馈
    public final static String LINK_URL_11 = "/ljoa/com/approvalController/listDeputyGeneralManagerQueryExpense";
    //报销总经理审批
    public final static String LINK_URL_12 = "/ljoa/com/approvalController/listGeneralManagerQueryExpenseNO";
    //报销打款
    public final static String LINK_URL_13 = "/ljoa/expense/ljExpenseMakeMoneyController/listAllLjExpenseMakeMoney";
    //报销策划总监审批
    public final static String LINK_URL_21 = "/ljoa/expense/LjExpenseDesignController/listGroupLjExpenseOriginalityNO";
    //报销设计总监审批
    public final static String LINK_URL_22 = "/ljoa/expense/LjExpenseDesignController/listGroupLjExpenseDesignNo";

    //佣金反馈
    public final static String LINK_URL_14 = "";
    //佣金营销总监审批 & 反馈
    public final static String LINK_URL_15 = "/ljoa/marketing/marketingSaleInfoController/listLjHouseInfoByStatusInfoSoldChiefInspector";
    //佣金渠道总监审批 & 反馈
    public final static String LINK_URL_16 = "/ljoa/com/ljHouseSaleInfoBMYJController/listGroupLjHouseSaleInfoBMYJQDNO";
    //佣金财务审批 & 反馈
    public final static String LINK_URL_17 = "/ljoa/expense/ljExpenseYJSPController/listGroupLjExpenseYJSPNO";
    //佣金副总审批 & 反馈
    public final static String LINK_URL_18 = "/ljoa/com/ljHouseSaleInfoFZYJController/listGroupLjHouseSaleInfoFZYJ";
    //佣金总经理审批
    public final static String LINK_URL_19 = "/ljoa/com/approvalController/listGeneralManagerQueryHouseSaleInfoNO";
    //佣金结算(修改佣金状态)
    public final static String LINK_URL_20 = "/ljoa/expense/maidController/listExpenseQueryMaidHouseInfo";
    //综合管理经理报销审批
    public  final  static  String LINK_URL_23="/ljoa/expense/LjExpenseRSController/listGroupLjExpenseByRSNO";
    //总监备用金审批
    public final  static  String LINK_URL_24="/ljoa/expense/LjMoneyBorrowController/listLjMoneyBorrowByOrgUuidNO";
    //财务经理备用金审批
    public final  static  String LINK_URL_25="/ljoa/expense/LjMoneyBorrowController/listLjMoneyBorrowByFinanceNO";
    //副总经理备用金审批
    public final  static  String LINK_URL_26="/ljoa/expense/LjMoneyBorrowController/listLjMoneyBorrowByFZNO";
    //总经理备用金审批
    public final  static  String LINK_URL_27="/ljoa/expense/LjMoneyBorrowController/listLjMoneyBorrowByZNO";
    //备用金打款
    public final  static  String LINK_URL_28="/ljoa/expense/LjMoneyBorrowController/listLjMoneyBorrowMakeMoney";
    //新房事业部报销
    public final  static  String LINK_URL_29="/ljoa/com/ljExpenseBMController/listAllLjExpenseBMXFNO";
    //新房事业部报销
    public final  static  String LINK_URL_30="/ljoa/channel/ljHouseSaleInfoController/listAllLjHouseSaleInfo";
    //二手房事业部报销
    public final static String LINK_URL_31="/ljoa/com/ljExpenseBMController/listAllLjExpenseBMERSNO";


    @Column(name = "SEND_MAN", length = 32)
    private String sendMan;//发送人 adminUserId
    @Column(name = "ACCEPT_MAN", length = 32)
    private String acceptMan;//收信人  adminUserId
    @Column(name = "SEND_MAN_NAME", length = 20)
    private String sendManName;//发送人姓名
    @Column(name = "ACCEPT_MAN_NAME", length = 20)
    private String acceptManName;//收信人姓名
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "MSG_TIME")
    private LocalDateTime msgTime;//发送日期
    @Column(name = "MSG_TITLE", length = 50)
    private String msgTitle;//标题
    @Column(name = "MSG_CONTENT", length = 500)
    private String msgContent;//内容
    @Column(name = "LINK_URL", length = 100)
    private String linkUrl;//链接
    @Column(name = "MSG_TYPE", length = 10)
    private String msgType;//消息类型
    @Column(name = "IS_READ", length = 1)
    private String isRead;//是否已读 0:未读 1:已读  关系类型：5

    public String getSendMan() {
        return sendMan;
    }

    public void setSendMan(String sendMan) {
        this.sendMan = sendMan;
    }

    public String getAcceptMan() {
        return acceptMan;
    }

    public void setAcceptMan(String acceptMan) {
        this.acceptMan = acceptMan;
    }

    public LocalDateTime getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(LocalDateTime msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getSendManName() {
        return sendManName;
    }

    public void setSendManName(String sendManName) {
        this.sendManName = sendManName;
    }

    public String getAcceptManName() {
        return acceptManName;
    }

    public void setAcceptManName(String acceptManName) {
        this.acceptManName = acceptManName;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
