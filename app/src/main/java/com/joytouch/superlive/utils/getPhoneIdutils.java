package com.joytouch.superlive.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.joytouch.superlive.app.Preference;

/**
 * 获取手机设备号
 * Created by Administrator on 4/28 0028.
 */
public class getPhoneIdutils {
    private static final String TELEPHONY_SERVICE = "phone";
    private static String phoneId;
    private  Context context;
    private static SharedPreferences sp;

    public getPhoneIdutils(Context context) {
        this.context=context;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    public void getid(){
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        String m_szDevIDShort = "50" + Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
        String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String m_szBTMAC = m_BluetoothAdapter.getAddress();
        String m_szLongID = szImei + m_szDevIDShort
                + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
        phoneId = MD5Util.md5(m_szLongID);//用于标记用的唯一标志
        sp.edit()
                .putString(Preference.phone_id,phoneId)
                .commit();
    }
}
