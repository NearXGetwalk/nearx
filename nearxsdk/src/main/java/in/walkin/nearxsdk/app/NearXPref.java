package in.walkin.nearxsdk.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;

import in.walkin.nearxsdk.NearX;

import static android.content.Context.MODE_PRIVATE;


public class NearXPref {


    public static  SharedPreferences preferences;

    public static void setPreferences (Context mContext, String key, String value ){
        preferences = mContext.getApplicationContext().getSharedPreferences(Constants.NEARX_PREF, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        preferencesEditor.putString(key,value);
        preferencesEditor.apply();
    }

    public static String getPreferenceValue(Context mContext, String key){
        SharedPreferences preferences = mContext.getSharedPreferences(Constants.NEARX_PREF, MODE_PRIVATE);
        return preferences.getString(key,"");
    }

    public static void saveMobileNum(String mobileNum) {
        setPreferences(NearX.getContect(),Constants.MOBILE_NUMBER, mobileNum);
    }

    public static String getMobileNum() {
        return getPreferenceValue(NearX.getContect(),Constants.MOBILE_NUMBER);
    }

    public static void saveAuthKey(String authkey) {
        setPreferences(NearX.getContect(),Constants.AUTH_KEY, authkey);
    }

    public static String getAuthKey() {
        return getPreferenceValue(NearX.getContect(),Constants.AUTH_KEY);
    }

    public static void saveUserName(String mobileNum) {
        setPreferences(NearX.getContect(),Constants.USER_NAME, mobileNum);
    }

    public static String getUserName() {
        return getPreferenceValue(NearX.getContect(),Constants.USER_NAME);
    }

    public static void saveHandsetCheck(String check) {
        setPreferences(NearX.getContect(),"MODEL_CHECK",check);
    }

    public static String getHandsetCheck() {
        return getPreferenceValue(NearX.getContect(),"MODEL_CHECK");
    }



}
