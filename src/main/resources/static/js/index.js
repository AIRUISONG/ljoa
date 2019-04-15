/*获取当前日期*/
function getCurrentDateTime() {
    var d = new Date();
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
    var week = d.getDay();
    /*时分秒*/
    /*var hours = d.getHours();
    var minutes = d.getMinutes();
    var seconds = d.getSeconds();
    var ms = d.getMilliseconds();*/
    var curDateTime = month;
    // if (month > 9)
    //     curDateTime = curDateTime + "年" + month;
    // else
    //     curDateTime = curDateTime + "年0" + month;
    if (date > 9)
        curDateTime = curDateTime + "月" + date + "日";
    else
        curDateTime = curDateTime + "月0" + date + "日";
    /*if (hours > 9)
    curDateTime = curDateTime + " " + hours;
    else
    curDateTime = curDateTime + " 0" + hours;
    if (minutes > 9)
    curDateTime = curDateTime + ":" + minutes;
    else
    curDateTime = curDateTime + ":0" + minutes;
    if (seconds > 9)
    curDateTime = curDateTime + ":" + seconds;
    else
    curDateTime = curDateTime + ":0" + seconds;*/
    var weekday = "";
    if (week == 0)
        weekday = "星期日";
    else if (week == 1)
        weekday = "星期一";
    else if (week == 2)
        weekday = "星期二";
    else if (week == 3)
        weekday = "星期三";
    else if (week == 4)
        weekday = "星期四";
    else if (week == 5)
        weekday = "星期五";
    else if (week == 6)
        weekday = "星期六";
    curDateTime = curDateTime + " " + weekday;
    return curDateTime;
}
$(function(){
    // 显示日期
    var date = getCurrentDateTime();
    $("#currentDate").text(date);
});
