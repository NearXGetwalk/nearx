package in.walkin.nearxsdk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

import in.walkin.nearxsdk.R;
import in.walkin.nearxsdk.app.NearXPref;

/**
 * Created by Anvesh on 04/08/19.
 */

public class Utils {


    public void checkHandset(final Context mContext, final Activity mActivity) {

        if (!NearXPref.getHandsetCheck().equals("CHECKED")) {

            try {
                final Intent intent = new Intent();
                String manufacturer = android.os.Build.MANUFACTURER;
                if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                } else if ("oneplus".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity"));
                } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                } else if ("huawei".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                } else if ("asus".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity"));
                } else {
                    Log.e("ot her phone ", "===>");
                }
                List<ResolveInfo> list = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() > 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setCancelable(false)
                            .setTitle("Requires Additional Permission")
                            .setMessage("Please enable the app in settings for make it work properly")
                            .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button

                                    mActivity.startActivity(intent);
                                    NearXPref.saveHandsetCheck("CHECKED");


                                }
                            });
//                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User cancelled the dialog
//                            }
//                        });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    public static String getDeviceMeta(Context context){
        // deviceId;devicePlatform;version;model;make
        try {
            return getUniqueId(context) +  ";" +
                    getOperatingSystem() + ";" +
                    getOperatingSystemVersion() + ";" +
                    getDeviceModel() +  ";" +
                    getVendorName();
        }catch(Exception e){
            return "";
        }
    }

    public static String getUniqueId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * Method to get the Operating System name
     *
     * @return
     */
    public static String getOperatingSystem() {
        return "Android";
    }


    /**
     * Method to get the OS version
     *
     * @return
     */
    public static String getOperatingSystemVersion() {
        int sdkVersion;
        sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion == 0) {
            return "";
        }
        return "" + sdkVersion;
    }

    /**
     * Method to get the Device Model
     *
     * @return
     */
    public static String getDeviceModel() {
        String model;
        model = android.os.Build.MODEL;
        if (model == null) {
            model = "";
        }
        return model;
    }


    /**
     * Method to get the Vendor Name
     *
     * @return
     */

    public static String getVendorName() {
        String manufacturer;
        manufacturer = android.os.Build.MANUFACTURER;
        if (manufacturer == null || android.os.Build.MANUFACTURER
                .equalsIgnoreCase(android.os.Build.UNKNOWN)) {
            manufacturer = android.os.Build.UNKNOWN;
        }
        return manufacturer;
    }
}
