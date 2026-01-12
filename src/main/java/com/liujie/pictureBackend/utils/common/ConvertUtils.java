package com.liujie.pictureBackend.utils.common;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.liujie.pictureBackend.exception.BusinessException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 转换工具类
 */
public class ConvertUtils {

    /**
     * 构造时间列表 时间格式为 yyyy-MM-dd HH:mm:ss
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 0 按秒数 1 按分钟 2 按小时 3 按天数 4 按月份 5 按年份
     * @return
     */
    public static List<String> getDateListByTimeDuration(String startTime,String endTime,int type){
        // 1. 直接解析日期字符串转换为标准形式
        LocalDateTime start;
        LocalDateTime end;
        try{
            start = LocalDateTimeUtil.of(DateUtil.parse(startTime));
            end = LocalDateTimeUtil.of(DateUtil.parse(endTime));
        }
        catch (Exception e)
        {
            throw new BusinessException(503,"日期格式转换失败,原因:"+e.getMessage());
        }
        List<String> dateList=new ArrayList<>();
        int timeLag;
        if(type==0){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime s = start.withNano(0);
            LocalDateTime e = end.withNano(0);
            timeLag= (int) ChronoUnit.SECONDS.between(s,e);
            //构建列表（包括开始时间和结束时间的端点）
            dateList = LongStream.rangeClosed(0,timeLag).
                    mapToObj(i->s.plusSeconds(i).
                            format(dateTimeFormatter)).
                    collect(Collectors.toList());
        }else if(type==1){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime s = start.withNano(0).withSecond(0);
            LocalDateTime e = end.withNano(0).withSecond(0);
            timeLag= (int) ChronoUnit.MINUTES.between(s,e);
            //构建列表（包括开始时间和结束时间的端点）
            dateList = LongStream.rangeClosed(0,timeLag).
                    mapToObj(i->s.plusMinutes(i).
                            format(dateTimeFormatter)).
                    collect(Collectors.toList());
        }else if(type==2){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
            LocalDateTime s = start.withNano(0).withSecond(0).withMinute(0);
            LocalDateTime e = end.withNano(0).withSecond(0).withMinute(0);
            timeLag= (int) ChronoUnit.HOURS.between(s,e);
            //构建列表（包括开始时间和结束时间的端点）
            dateList = LongStream.rangeClosed(0,timeLag).
                    mapToObj(i->s.plusHours(i).
                            format(dateTimeFormatter)).
                    collect(Collectors.toList());
        }else if(type==3){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime s = start.withNano(0).withSecond(0).withMinute(0).withHour(0);
            LocalDateTime e = end.withNano(0).withSecond(0).withMinute(0).withHour(0);
            timeLag= (int) ChronoUnit.DAYS.between(s,e);
            //构建列表（包括开始时间和结束时间的端点）
            dateList = LongStream.rangeClosed(0,timeLag).
                    mapToObj(i->s.plusDays(i).
                            format(dateTimeFormatter)).
                    collect(Collectors.toList());
        }else if(type==4){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            LocalDateTime s = start.withNano(0).withSecond(0).withMinute(0).withHour(0).withDayOfMonth(1);
            LocalDateTime e = end.withNano(0).withSecond(0).withMinute(0).withHour(0).withDayOfMonth(1);
            timeLag= (int) ChronoUnit.MONTHS.between(s,e);
            //构建列表（包括开始时间和结束时间的端点）
            dateList = LongStream.rangeClosed(0,timeLag).
                    mapToObj(i->s.plusMonths(i).
                            format(dateTimeFormatter)).
                    collect(Collectors.toList());
        }else if(type==5){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
            LocalDateTime s = start.withNano(0).withSecond(0).withMinute(0).withHour(0).withDayOfMonth(1).withMonth(1);
            LocalDateTime e = end.withNano(0).withSecond(0).withMinute(0).withHour(0).withDayOfMonth(1).withMonth(1);
            timeLag= (int) ChronoUnit.YEARS.between(s,e);
            //构建列表（包括开始时间和结束时间的端点）
            dateList = LongStream.rangeClosed(0,timeLag).
                    mapToObj(i->s.plusYears(i).
                            format(dateTimeFormatter)).
                    collect(Collectors.toList());
        }
        return dateList;
    }

}
