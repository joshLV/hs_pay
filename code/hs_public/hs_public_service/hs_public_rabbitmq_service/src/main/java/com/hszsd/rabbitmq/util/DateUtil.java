package com.hszsd.rabbitmq.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * @author yangwejian
 * @since 2016-7-22
 * @version V1.0.0
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);


    /**
     * 验证时间格式是否为yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static boolean DateTimeCheck(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String format = dateFormat.format(date);
            System.out.println(format);
        } catch (Exception e) {
            logger.error("rabbitMQ producer : sendTime format is error:", e);
            return false;
        }
        return true;
    }
}
