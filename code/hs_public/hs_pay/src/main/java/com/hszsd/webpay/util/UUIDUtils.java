package com.hszsd.webpay.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * UUID 工具类
 * Created by jiangshikun on 2016/5/5 0005.
 */
public class UUIDUtils {

    private static Date date = new Date();
    private static StringBuilder buf = new StringBuilder();
    private static int seq = 1;
    private static final int ROTATION = 99;
    private static final String DATE_FORMAT_ONE = "%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d";
    private static final String DATE_FORMAT_TWO = "%1$tY%1$tm%1$td%1$tk%1$tM%1$tS";

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    /**
     * 生成 8位 UUID
     * @return
     */
    public static String getShortUUID() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }


    public static synchronized long getBaseNO(String format){
        if (seq > ROTATION) seq = 0;
        buf.delete(0, buf.length());
        date.setTime(System.currentTimeMillis());
        String str = String.format(format, date, seq++);
        return Long.parseLong(str);
    }

    private static void swap(int i, int j, int[] nums) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     *这是典型的随机洗牌算法。
     * 流程是从备选数组中选择一个放入目标数组中，将选取的数组从备选数组移除
     * 放至最后，并缩小选择区域）
     * 算法时间复杂度O(n)
     * @param dataLen  从默认数组中可以选择的部分长度
     * @param needLen 保存的数组长度
     * @return 随机dataLen为不重复数组
     */
    public static String generateNumber(int dataLen ,int needLen) {
        String no="";
        //初始化备选数组
        int[] defaultNums = new int[needLen];
        for (int i = 0; i < defaultNums.length; i++) {
            defaultNums[i] = i;
        }

        Random random = new Random();
        int[] nums = new int[dataLen];
        //默认数组中可以选择的部分长度
        int canBeUsed = needLen;
        //填充目标数组
        for (int i = 0; i < nums.length; i++) {
            //将随机选取的数字存入目标数组
            int index = random.nextInt(canBeUsed);
            nums[i] = defaultNums[index];
            //将已用过的数字扔到备选数组最后，并减小可选区域
            swap(index, canBeUsed - 1, defaultNums);
            canBeUsed--;
        }
        if (nums.length>0) {
            for (int i = 0; i < nums.length; i++) {
                no+=nums[i];
            }
        }

        return no;
    }
    /**
     *这是典型的随机洗牌算法二
     * 流程是从备选数组中选择一个放入目标数组中，将选取的数组从备选数组移除
     * 放至最后，并缩小选择区域）
     * 算法时间复杂度O(n)
     * @param dataLen  从默认数组中可以选择的部分长度
     * @param needLen 保存的数组长度
     * @return 随机dataLen为不重复数组
     */
    public static String generateNumberTwo(int dataLen, int needLen) {
        String no="";
        int num[]=new int[dataLen];
        int c=0;
        for (int i = 0; i < dataLen; i++) {
            num[i] = new Random().nextInt(needLen);
            c = num[i];
            for (int j = 0; j < i; j++) {
                if (num[j] == c) {
                    i--;
                    break;
                }
            }
        }
        if (num.length>0) {
            for (int i = 0; i < num.length; i++) {
                no+=num[i];
            }
        }
        return no;
    }

    /**
     * 获取32位UUID
     * @param isUnderline 是否带下划线
     * @return
     */
    public static String  getUUID(boolean isUnderline){
        String uuid = "";
        if(isUnderline){
            uuid = UUID.randomUUID().toString();
        }else{
            uuid =UUID.randomUUID().toString().replaceAll("-", "");
        }
        return uuid;
    }

    /**
     * 获取纯数字的编号 needLen < baseLen 否则初始化会出错
     * @param needLen 需要的随机数长度
     * @param baseLen 初始化随机数保存的数组长度
     * @return 年月日时分秒 + 随机数
     */
    public static String getId(int needLen, int baseLen){
        return getBaseNO(DATE_FORMAT_TWO) + generateNumber(needLen,baseLen);
    }

    /**
     * 短信验证码 长度由传入的参数决定 needLen <= baseLen
     * @param needLen 需要的验证码长度
     * @param baseLen 生成的验证码保存的数组长度
     * @return
     */
    public static String getVrificationCode(int needLen, int baseLen){
        return generateNumber(needLen,baseLen);
    }

    public static void main(String args[]){
        //System.out.println("短UUID = " + getShortUUID());
        //System.out.println("带下划线 = "+getUUID(true));
        //System.out.println("不带下划线 = " + getUUID(false));
        //System.out.println("产品编码 = " + getId(10,10));
        //System.out.println("用户编码 = " + getId(6,10));
        System.out.println("用户编码 = " + getId(6,6));
        //System.out.println("短信验证码 = " + getVrificationCode(6,6));
    }

}
