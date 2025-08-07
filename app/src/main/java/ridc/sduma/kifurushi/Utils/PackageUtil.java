package ridc.sduma.kifurushi.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import ridc.sduma.kifurushi.activities.MainActivity;
import ridc.sduma.kifurushi.adapters.AppInfoAdapter;

public class PackageUtil {

    public static boolean isPackage(Context context, CharSequence s) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(s.toString(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public static int getPackageUid(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        int uid = -1;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            Log.d(MainActivity.class.getSimpleName(), packageInfo.packageName);
            uid = packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(AppInfoAdapter.class.getSimpleName(), e.getMessage());
        }
        return uid;
    }

    public static Drawable getPackageDrawable(Context context, ApplicationInfo applicationInfo){
        return context.getPackageManager().getApplicationIcon(applicationInfo);
    }

    public static String getAppName(Context context, ApplicationInfo applicationInfo){
        return (String) context.getPackageManager().getApplicationLabel(applicationInfo);
    }


}
