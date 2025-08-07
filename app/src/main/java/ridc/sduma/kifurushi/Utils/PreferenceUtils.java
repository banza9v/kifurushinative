package ridc.sduma.kifurushi.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    public static void saveLanguagePreference(Context context, String lang){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("kifurushiLanguage", lang);
        editor.apply();
    }

    public static String getLanguagePreference(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getString("kifurushiLanguage", null);
    }

    public static void saveIsSkipPressed(boolean status, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isSkipPressed", status);
        editor.apply();
    }

    public static boolean isSkipPressed(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("isSkipPressed", false);
    }


/*================================== Cellular threshold preferences ==============================*/

    public static void saveThresholdStatus(boolean status, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isThresholdValueSet", status);
        editor.apply();
    }

    public static boolean isThresholdValueSet(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("isThresholdValueSet", false);
    }

    public static void saveThresholdStartTimePref(long time, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("thresholdStartTime", time);
        editor.apply();
    }

    public static long getThresholdStartTimePref(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getLong("thresholdStartTime", 0);
    }

    public static void clearThresholdStartTime(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("thresholdStartTime");
    }

    public static void saveThresholdValuePref(long time, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("thresholdValue", time);
        editor.apply();
    }

    public static long getThresholdValuePref(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getLong("thresholdValue", 0);
    }

    public static void clearThresholdValuePrefs(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("thresholdValue");
    }

/*=========================== End of cellular threshold preferences ==============================*/



/*================================== Wi-Fi threshold preferences ==============================*/


    public static void saveWifiThresholdStatus(boolean status, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isWifiThresholdValueSet", status);
        editor.apply();
    }

    public static boolean isWifiThresholdValueSet(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("isWifiThresholdValueSet", false);
    }



    public static void saveWifiThresholdStartTimePref(long time, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("wifiThresholdStartTime", time);
        editor.apply();
    }

    public static long getWifiThresholdStartTimePref(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getLong("wifiThresholdStartTime", 0);
    }

    public static void clearWifiThresholdStartTime(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("wifiThresholdStartTime");
    }

    public static void saveWifiThresholdValuePref(long time, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("wifiThresholdValue", time);
        editor.apply();
    }

    public static long getWifiThresholdValuePref(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getLong("wifiThresholdValue", 0);
    }

    public static void clearWifiThresholdValuePrefs(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("wifiThresholdValue");
    }

/*=========================== End of cellular threshold preferences ==============================*/



    public static void saveIsDialogAlreadyShown(boolean status, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDialogAlreadyShown", status);
        editor.apply();
    }
    
    public static boolean isDialogAlreadyShown(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("isDialogAlreadyShown", false);
    }

    public static void saveIsOnBoardingShown(boolean status, Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isOnBoardingShown", status);
        editor.apply();
    }

    public static boolean isOnBoardingShown(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("isOnBoardingShown", false);
    }

    public static void savePermissionPrefs(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("allPermissionsGranted", true);
        editor.apply();
    }

    public static boolean allPermissionsAreGranted(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("allPermissionsGranted", false);
    }



/*============================ Start of Selected wifi position Pres ==============================*/

    public static void saveIsWifiDurationSelected(Context context, boolean status){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isWifiDurationSelected", status);
        editor.apply();
    }

    public static boolean isWifiDurationSelected(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("isWifiDurationSelected", false);
    }

    public static void saveWifiDurationSelectedPosition(Context context, int pos){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("wifiSelectedPosition", pos);
        editor.apply();
    }

    public static int getWifiDurationSelectedPosition(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getInt("wifiSelectedPosition", 0);
    }

/*============================== End of Selected Wifi position Pres ===============================*/



/*========================== Start of Selected cellular position Pres ============================*/

    public static void saveIsCellularDurationSelected(Context context, boolean status){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isCellularDurationSelected", status);
        editor.apply();
    }

    public static boolean isCellularDurationSelected(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("isCellularDurationSelected", false);
    }

    public static void saveCellularDurationSelectedPosition(Context context, int pos){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("cellularSelectedPosition", pos);
        editor.apply();
    }

    public static int getCellularDurationSelectedPosition(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getInt("cellularSelectedPosition", 0);
    }

/*============================ End of Selected cellular position Pres =============================*/

    public static void saveIsStartDayOfWeekChanged(Context context, boolean status){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("startDayOfWeekChanged", status);
        editor.apply();
    }

    public static boolean isStartDayOfWeekChanged(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getBoolean("startDayOfWeekChanged", false);
    }

    public static void saveSelectedStartDayOfWeek(Context context, int day){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("startDayOfWeek", day);
        editor.apply();
    }

    public static int getFirstDayOfWeek(Context context){
        SharedPreferences preferences = context.getSharedPreferences("sduma_kifurushi", Context.MODE_PRIVATE);
        return preferences.getInt("startDayOfWeek", 1);
    }

    
}
