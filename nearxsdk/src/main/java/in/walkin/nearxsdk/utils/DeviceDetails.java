package in.walkin.nearxsdk.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class DeviceDetails {

    private static final String TAG = "DeviceDetails";
    private String osVersion;
    private String model;
    private String deviceId;
    private String brand;
    private Context mContext;
    private String deviceName;

    public DeviceDetails(Context mContext) {
        this.mContext = mContext;
    }


    public String getOsVersion() {
        return Build.VERSION.RELEASE;
    }


    public String getDeviceId() {
        return Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getBrand() {
        return Build.BRAND;
    }


    public String getDeviceName() {
        return getBrand()+" "+getModel();
    }

}
