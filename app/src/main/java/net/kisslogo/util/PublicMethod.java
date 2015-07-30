package net.kisslogo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共的静态方法
 * Created by yz1309 on 2015/3/20.
 */
public class PublicMethod {
    /*判断SD卡是否存在*/
    public static boolean judageSDIsExist() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 格式化 数据
     *
     * @param size 原始数据
     * @return
     */
    public static String formatSize(long size) {
        String suffix = null;
        float fSize = 0;
        if (size >= 1024) {
            suffix = "KB";
            fSize = size / 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
            }
            if (fSize >= 1024) {
                suffix = "GB";
                fSize /= 1024;
            }
        } else {
            fSize = size;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }



    /**
     * String convert int
     *
     * @param str String value
     * @param res default value
     * @return
     */
    public static int stringConvertInt(String str, int res) {
        int num = res;
        try {
            if (str != "" && str != null)
                num = Integer.parseInt(str);
        } catch (Exception ex) {
        } finally {
            return num;
        }

    }

    /**
     * String convert long
     *
     * @param str String value
     * @param res default value
     * @return
     */
    public static long stringConvertLong(String str, long res) {
        long num = res;
        try {
            if (str != "" && str != null)
                num = Integer.parseInt(str);
        } catch (Exception ex) {
        } finally {
            return num;
        }
    }

    /**
     * 获取字符串中的数字
     *
     * @param str 源字符串
     * @return
     */
    public static String getIntFromString(String str) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(str);
        str = m.replaceAll("");
        return str;
    }

}
