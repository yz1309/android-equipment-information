package net.kisslogo.util;


import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * 设备基本信息
 * Created by yz1309 on 2015/3/20.
 */
public class GetEquipInfo {


    /*系统版本系想你
    * [0] 内核版本
    * [1] 系统版本
    * [2] 手机型号
    * [3] 固件版本
    * [4] 品牌
    * */
    public static String[] getVersion() throws Exception{
        String[] version={"","","","",""};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0]=arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// system version
        version[2] = Build.MODEL;//model
        version[3] = Build.DISPLAY;//firmware version
        version[4] = Build.BRAND;//ping pai
        return version;
    }

    /*CPU
    * [0] CPU型号
    * [1] CPU硬件
    * */
    public static String[] getCpuInfo()
    {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = { "", "" };
        try
        {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ( 1==1 ) {
                str2 = localBufferedReader.readLine();
                if (str2.indexOf("Processor\t:") >= 0) {
                /*手机型号*/
                    str2 = str2.substring(("Processor\t:").length() + 1);
                    cpuInfo[0] = str2;
                }
                if (str2.indexOf("Hardware\t:") >= 0) {
                /*手机型号*/
                    str2 = str2.substring(("Hardware\t:").length() + 1);
                    cpuInfo[1] = str2;
                    localBufferedReader.close();
                    break;
                }
                /*str2 = localBufferedReader.readLine();*/
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return cpuInfo;
    }

    /**
     * 获取CPU核心数
     * @return
     */
    public static int getNumCores()
    {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter
        {
            @Override
            public boolean accept(File pathname)
            {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName()))
                {
                    return true;
                }
                return false;
            }
        }
        try
        {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // Default to return 1 core
            return 1;
        }
    }


    /*主频*/
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    /*获取RAM空间大小*/
    public static String getTotalRAMMemory() {
        String str1 = "/proc/meminfo";
        String str2="";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if(str2.indexOf("MemTotal:") >= 0) {
                    return PublicMethod.getIntFromString(str2);
                }
            }
        } catch (IOException e) {}
        return str2;
    }

    /*获取ROM空间
    * [0] total
    * [1] rest
    * */
    public static long[] getRomMemroy() throws Exception {
        long[] romInfo = new long[2];
        //Total rom memory
        romInfo[0] = getTotalInternalMemorySize();
        //Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfo[1] = blockSize * availableBlocks;
        getVersion();
        return romInfo;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /*获取SD空间
    * [0] total
    * [1] rest
    * */
    public static long[] getSDCardMemory() {
        long[] sdCardInfo=new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return sdCardInfo;
    }


    /*
    * 获取手机屏幕信息
    * [0] 宽
    * [1] 高
    * [2] 密度
    * [3] 像素密度
    * */
    public static String[] getDisplayMetrics(Context cx) {
        String str = "";
        String[] res= new String[4];
        DisplayMetrics dm = new DisplayMetrics();
        dm = cx.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        float dpi = dm.densityDpi;
        res[0] = String.valueOf(screenWidth);
        res[1] = String.valueOf(screenHeight);
        res[2] = String.valueOf(density);
        res[3] = String.valueOf(dpi);
        return res;
    }


    /**
     * 获取电池容量
     * @param context
     * @return
     */
    public static double getBatteryCapacity(Context context) {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(context);
            double batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
            return batteryCapacity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  0d;
    }
}
