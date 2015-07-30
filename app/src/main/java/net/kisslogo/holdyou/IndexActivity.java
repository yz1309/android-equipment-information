package net.kisslogo.holdyou;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.kisslogo.util.CameraUtil;
import net.kisslogo.util.GetEquipInfo;
import net.kisslogo.util.NetUtils;
import net.kisslogo.util.PublicMethod;

public class IndexActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_index);
        initView();
        initEvent();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rlRoot = (LinearLayout) findViewById(R.id.rlRoot);
        tvEquipName = (TextView) findViewById(R.id.tvEquipName);
        tvPhoneModel = (TextView) findViewById(R.id.tvPhoneModel);
        tvOSVersion = (TextView) findViewById(R.id.tvOSVersion);
        tvGuJianVersion = (TextView) findViewById(R.id.tvGuJianVersion);
        tvCoreVersion = (TextView) findViewById(R.id.tvCoreVersion);
        tvIMEI = (TextView) findViewById(R.id.tvIMEI);
        tvMac = (TextView) findViewById(R.id.tvMac);
        tvCPUModel = (TextView) findViewById(R.id.tvCPUModel);
        tvCPUHardWare = (TextView) findViewById(R.id.tvCPUHardWare);
        tvCPUNum = (TextView) findViewById(R.id.tvCPUNum);
        tvCPUZhuPin = (TextView) findViewById(R.id.tvCPUZhuPin);
        tvStorageRAM = (TextView) findViewById(R.id.tvStorageRAM);
        tvStorageROM = (TextView) findViewById(R.id.tvStorageROM);
        tvStorageSD = (TextView) findViewById(R.id.tvStorageSD);
        tvScreenFenBianLv = (TextView) findViewById(R.id.tvScreenFenBianLv);
        tvScreenDXMiDu = (TextView) findViewById(R.id.tvScreenDXMiDu);
        tvCameraBack = (TextView) findViewById(R.id.tvCameraBack);
        tvCameraHead = (TextView) findViewById(R.id.tvCameraHead);
        tvBatteryNum = (TextView) findViewById(R.id.tvBatteryNum);
        tvBatteryTemp = (TextView) findViewById(R.id.tvBatteryTemp);
        tvBatteryType = (TextView) findViewById(R.id.tvBatteryType);

        tvTuoLuoYi = (TextView) findViewById(R.id.tvTuoLuoYi);
        tvGuangXian = (TextView) findViewById(R.id.tvGuangXian);
        tvJiaSu = (TextView) findViewById(R.id.tvJiaSu);
        tvCiChang = (TextView) findViewById(R.id.tvCiChang);
        tvYaLi = (TextView) findViewById(R.id.tvYaLi);
        tvJuLi = (TextView) findViewById(R.id.tvJuLi);
        tvWenDu = (TextView) findViewById(R.id.tvWenDu);
        tvXianXing = (TextView) findViewById(R.id.tvXianXing);
        tvXuanZhuan = (TextView) findViewById(R.id.tvXuanZhuan);
        tvZhongLi = (TextView) findViewById(R.id.tvZhongLi);

    }

    private void initEvent() {

        batteryReceiver = new BatteryReceiver();
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, batteryFilter);
    }

    /*结婚生子*/
    private void init() throws Exception {
        String[] versions = GetEquipInfo.getVersion();
        tvEquipName.setText(versions[4]);
        tvPhoneModel.setText(versions[2]);
        tvOSVersion.setText(versions[1]);
        tvGuJianVersion.setText(versions[3]);
        tvCoreVersion.setText(versions[0]);
        tvIMEI.setText(((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                .getDeviceId());
        tvMac.setText(NetUtils.getMac(this));
        String[] cpuinfo = GetEquipInfo.getCpuInfo();
        tvCPUModel.setText(cpuinfo[0]);
        tvCPUHardWare.setText(cpuinfo[1]);
        tvCPUNum.setText(Integer.toString(GetEquipInfo.getNumCores()) + "核");
        int maxCpuFreq = PublicMethod.stringConvertInt(GetEquipInfo.getMaxCpuFreq(), 1);
        maxCpuFreq = maxCpuFreq / 1000;
        tvCPUZhuPin.setText(Integer.toString(maxCpuFreq) + "MHZ");


        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        long totalMemory = PublicMethod.stringConvertLong(GetEquipInfo.getTotalRAMMemory(), 1);


        tvStorageRAM.setText("(" + PublicMethod.formatSize(mi.availMem) +
                "/" + PublicMethod.formatSize(totalMemory * 1024) + ")");

        long[] rom = GetEquipInfo.getRomMemroy();
            tvStorageROM.setText("(" + PublicMethod.formatSize(  rom[1]) + ")/("
                    + PublicMethod.formatSize(  rom[0]) + ")");

        long[] sd = GetEquipInfo.getSDCardMemory();
        if (PublicMethod.judageSDIsExist()) {
            tvStorageSD.setText("(" + PublicMethod.formatSize((Long) sd[1])
                    + ")/(" + PublicMethod.formatSize((Long) sd[0]) + ")");
        } else {
            tvStorageSD.setText("SD卡不存在");
        }
        String[] screen = GetEquipInfo.getDisplayMetrics(this);
            tvScreenFenBianLv.setText(screen[0] + "*" + screen[1]);
            tvScreenDXMiDu.setText(screen[3] + "dpi");
        tvCameraBack.setText(CameraUtil.getCameraPixels(CameraUtil.HasBackCamera()));
        tvCameraHead.setText(CameraUtil.getCameraPixels(CameraUtil.HasFrontCamera()));
        tvBatteryNum.setText(Double.toString(GetEquipInfo.getBatteryCapacity(this)) + "mAh");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor tuoluoyi = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (tuoluoyi == null)
            tvTuoLuoYi.setText("不支持");
        Sensor guangxian = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (guangxian == null)
            tvGuangXian.setText("不支持");
        Sensor jiasu = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (jiasu == null)
            tvJiaSu.setText("不支持");
        Sensor cichang = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (cichang == null)
            tvCiChang.setText("不支持");
        Sensor yali = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (yali == null)
            tvYaLi.setText("不支持");
        Sensor juli = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (juli == null)
            tvJuLi.setText("不支持");
        Sensor wendu = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        if (wendu == null)
            tvWenDu.setText("不支持");

        Sensor xianxing = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (xianxing == null)
            tvXianXing.setText("不支持");
        Sensor xuanzhuan = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (xuanzhuan == null)
            tvXuanZhuan.setText("不支持");
        Sensor zhongli = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (zhongli == null)
            tvZhongLi.setText("不支持");
    }


    /*通过监听获取电池信息*/
    private BatteryReceiver batteryReceiver;

    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int temperature = arg1.getIntExtra("temperature", 0);
            String tech = arg1.getStringExtra("technology");
            float fl = ((float) temperature) / 10;
            tvBatteryTemp.setText(String.valueOf(fl) + " C");
            tvBatteryType.setText(tech);
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(batteryReceiver);
        super.onDestroy();
    }


    /*设备*/
    /*设备名称*/
    TextView tvEquipName;
    /*手机型号*/
    TextView tvPhoneModel;
    /*系统版本号*/
    TextView tvOSVersion;
    /*固件版本*/
    TextView tvGuJianVersion;
    /*内核版本*/
    TextView tvCoreVersion;
    /*IMEi*/
    TextView tvIMEI;
    /*Mac地址*/
    TextView tvMac;
    /*CPU*/
    /*CPU型号*/
    TextView tvCPUModel;
    /*CPU硬件*/
    TextView tvCPUHardWare;
    /*CPU核心数*/
    TextView tvCPUNum;
    /*最高主频*/
    TextView tvCPUZhuPin;
    /*存储*/
    /*存储Ram*/
    TextView tvStorageRAM;
    /*存储Rom*/
    TextView tvStorageROM;
    /*SD空间*/
    TextView tvStorageSD;
    /*屏幕*/
    /*屏幕分辨率*/
    TextView tvScreenFenBianLv;
    /*屏幕像素密度*/
    TextView tvScreenDXMiDu;
    /*摄像头*/
    /*摄像头后置*/
    TextView tvCameraBack;
    /*摄像头前置*/
    TextView tvCameraHead;
    /*电池*/
    /*电池容量*/
    TextView tvBatteryNum;
    /*电池温度*/
    TextView tvBatteryTemp;
    /*电池类型*/
    TextView tvBatteryType;
    LinearLayout rlRoot;
    //&&传感器
    SensorManager sensorManager;
    /*陀螺仪*/
    TextView tvTuoLuoYi;
    /*光线传感器*/
    TextView tvGuangXian;
    /*加速传感器*/
    TextView tvJiaSu;
    /*磁场传感器*/
    TextView tvCiChang;
    /*压力传感器*/
    TextView tvYaLi;
    /*距离传感器*/
    TextView tvJuLi;
    /*温度传感器*/
    TextView tvWenDu;
    /*线形加速传感器*/
    TextView tvXianXing;
    /*旋转矢量传感器*/
    TextView tvXuanZhuan;
    /*重力传感器*/
    TextView tvZhongLi;
}
