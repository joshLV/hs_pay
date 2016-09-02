package com.hszsd.common.util.Properties;

import com.hszsd.common.util.logger.Logger;
import com.hszsd.common.util.logger.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * 读取配置文件实体类.
 * @author  yangwenjian
 * @version 1.0.0
 */
public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);


    private static final String PROPERTIES_SUFFIX = ".properties";

    private static final String shpot = ".";

    private static Properties properties;


    /**
     * 设置配置文件路径
     *
     * @param properties 文件相对路径
     */
    public void setProperties(String properties) {
        //从0开始计算该字符串存在的位置
        int isFile = properties.lastIndexOf(PROPERTIES_SUFFIX);
        //创建读取数据流对象
        InputStream inputStream = null;
        if (isFile > 0) {
            //此类所在的包下取资源
            inputStream = this.getClass().getClassLoader().getResourceAsStream(properties);
        } else {
            String language = Locale.getDefault().toString();
            inputStream = this.getClass().getClassLoader().getResourceAsStream(
                    properties + language + PROPERTIES_SUFFIX);
        }
        try {
            Properties p = new Properties();
            p.load(inputStream);
            this.properties = p;
        } catch (IOException e) {
            e.printStackTrace();
            logger.trace(e);
        }
    }

    /**
     * 获取配置文件内容<br/>
     * 如果code为空则只根据classpath获取
     * @param classpath 文件类路径
     * @param code 文件code编码
     * @return String 消息内筒
     */
    public static String getProperty(String classpath,String code){

        String msg= "";
        if(StringUtils.isNotEmpty(code)){
            msg=StringUtils.join(classpath,shpot,code);
        }else {
            msg = classpath;
        }
        try {
            return properties.getProperty(msg);
        }catch (Exception e){
            e.printStackTrace();
            logger.trace(e);
            return null;
        }

    }


}
