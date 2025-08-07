package ridc.sduma.kifurushi.Utils;

import static android.app.usage.NetworkStats.Bucket.UID_REMOVED;
import static android.app.usage.NetworkStats.Bucket.UID_TETHERING;
import static android.os.Process.SYSTEM_UID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ridc.sduma.kifurushi.R;

public class AppUtils {

    public List<AppDetails> getListOfAppsAndTheirCellularUsage(Context context, NetStatsUtil statsUtil, long startTime, long endTime) {

        List<AppDetails> appDetailsList = new ArrayList<>();

        PackageManager pm = context.getApplicationContext().getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            int UID = packageInfo.uid;
            String package_name = packageInfo.packageName;
            ApplicationInfo app = null;

            try {
                app = pm.getApplicationInfo(package_name, 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.d("PACKAGE_ERR", e.getMessage());
            }

            String appName = (String) pm.getApplicationLabel(app);
            Drawable icon = pm.getApplicationIcon(app);

            long downloads;
            long uploads;
            if (appName.contains("Tethering") || appName.contains("tethering") || package_name.contains("Tethering") || package_name.contains("tethering")) {
                downloads = statsUtil.getPackageRxBytesMobile(context, startTime, endTime, UID_TETHERING);
                uploads = statsUtil.getPackageTxBytesMobile(context, startTime, endTime, UID_TETHERING);
            } else {
                downloads = statsUtil.getPackageRxBytesMobile(context, startTime, endTime, UID);
                uploads = statsUtil.getPackageTxBytesMobile(context, startTime, endTime, UID);
            }

            if ((downloads + uploads) > 0) {

                AppDetails details = new AppDetails(
                        UID,
                        appName,
                        icon,
                        getUnit(downloads),
                        getUnit(uploads),
                        getTotalUnit(downloads + uploads),
                        bytesConverter(downloads),
                        bytesConverter(uploads),
                        bytesConverter((downloads + uploads)),
                        downloads + uploads,
                        downloads,
                        uploads
                );
                appDetailsList.add(details);
            }

        }

        long downloads = statsUtil.getPackageRxBytesMobile(context, startTime, endTime, UID_REMOVED);
        long uploads = statsUtil.getPackageTxBytesMobile(context, startTime, endTime, UID_REMOVED);

        if ((downloads + uploads) > 0){
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.delete);

            AppDetails details = new AppDetails(
                    UID_REMOVED,
                    "Uninstalled",
                    drawable,
                    getUnit(downloads),
                    getUnit(uploads),
                    getTotalUnit(downloads + uploads),
                    bytesConverter(downloads),
                    bytesConverter(uploads),
                    bytesConverter((downloads + uploads)),
                    (downloads + uploads),
                    downloads,
                    uploads
            );

            appDetailsList.add(details);
        }

        return appDetailsList;
    }

    public List<AppDetails> getListOfAppsAndTheirWifiUsage(Context context, NetStatsUtil statsUtil, long startTime, long endTime) {

        List<AppDetails> appDetailsList = new ArrayList<>();

        PackageManager pm = context.getApplicationContext().getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);

        for (ApplicationInfo packageInfo : packages) {
            int UID = packageInfo.uid;
            String package_name = packageInfo.packageName;
            ApplicationInfo app = null;
            try {
                app = pm.getApplicationInfo(package_name, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

            }

            String appName = (String) pm.getApplicationLabel(app);
            Drawable icon = pm.getApplicationIcon(app);

            long downloads;
            long uploads;
            if (appName.contains("Tethering") || appName.contains("tethering") || package_name.contains("Tethering") || package_name.contains("tethering")) {
                downloads = statsUtil.getPackageRxBytesWifi(UID_TETHERING, startTime, endTime);
                uploads = statsUtil.getPackageRxBytesWifi(UID_TETHERING, startTime, endTime);
            } else {
                downloads = statsUtil.getPackageRxBytesWifi(UID, startTime, endTime);
                uploads = statsUtil.getPackageTxBytesWifi(UID, startTime, endTime);
            }
            if ((downloads + uploads) > 0) {

                AppDetails details = new AppDetails(
                        UID,
                        appName,
                        icon,
                        getUnit(downloads),
                        getUnit(uploads),
                        getTotalUnit(downloads + uploads),
                        bytesConverter(downloads),
                        bytesConverter(uploads),
                        bytesConverter((downloads + uploads)),
                        (downloads + uploads),
                        downloads,
                        uploads
                );

                appDetailsList.add(details);

            }

        }

        long downloads = statsUtil.getPackageRxBytesWifi(UID_REMOVED, startTime, endTime);
        long uploads = statsUtil.getPackageRxBytesWifi(UID_REMOVED, startTime, endTime);

        if ((downloads + uploads) > 0){
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.delete);

            AppDetails details = new AppDetails(
                    UID_REMOVED,
                    "Uninstalled",
                    drawable,
                    getUnit(downloads),
                    getUnit(uploads),
                    getTotalUnit(downloads + uploads),
                    bytesConverter(downloads),
                    bytesConverter(uploads),
                    bytesConverter((downloads + uploads)),
                    (downloads + uploads),
                    downloads,
                    uploads
            );

            appDetailsList.add(details);
        }

        return appDetailsList;
    }


    public static double bytesConverter(long bytes) {

        if (bytes < 1024 * 1024 * 1024) {
            double val = (double) bytes / (1024 * 1024);
            BigDecimal number = new BigDecimal(val);
            BigDecimal decimal = number.setScale(3, RoundingMode.HALF_EVEN);
            return decimal.doubleValue();
        }
        if (bytes > 1024 * 1024 * 1024) {
            double val = (double) bytes / (1024 * 1024 * 1024);
            BigDecimal number = new BigDecimal(val);
            BigDecimal decimal = number.setScale(3, RoundingMode.HALF_EVEN);
            return decimal.doubleValue();
        }

        return bytes;
    }

    public static String getUnit(long volume) {

        if (volume < 1024 * 1024 * 1024) {
            return "MB";
        }
        if (volume > 1024 * 1024 * 1024) {
            return "GB";
        }

        return "B";
    }

    public static double totalBytesConverter(long bytes) {

        if (bytes < 1024 * 1024 * 1024) {
            double val = (double) bytes / (1024 * 1024);
            BigDecimal number = new BigDecimal(val);
            BigDecimal decimal = number.setScale(2, RoundingMode.HALF_EVEN);
            return decimal.doubleValue();
        }
        if (bytes > 1024 * 1024 * 1024) {
            double val = (double) bytes / (1024 * 1024 * 1024);
            BigDecimal number = new BigDecimal(val);
            BigDecimal decimal = number.setScale(2, RoundingMode.HALF_EVEN);
            return decimal.doubleValue();
        }

        return bytes;
    }

    public String getTotalUnit(long volume) {

        if (volume < 1024 * 1024 * 1024) {
            return "MB";
        }
        if (volume > 1024 * 1024 * 1024) {
            return "GB";
        }

        return "B";
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public double getTotalVolume(List<AppDetails> appDetailsList) {
        double volume = 0;

        for (AppDetails appDetails : appDetailsList) {
            volume = volume + appDetails.getTotalBytes();
        }
        return volume;
    }

    public long getTotalBytes(List<AppDetails> appDetailsList) {
        long total = 0L;
        for (AppDetails appDetails : appDetailsList)
            total = total + appDetails.getTotalInBytes();
        return total;
    }

    public double getTotalDownloads(List<AppDetails> appDetailsList) {
        double volume = 0;

        for (AppDetails appDetails : appDetailsList) {
            volume = volume + appDetails.getDownloads();
        }
        return volume;
    }

    public double getTotalUploads(List<AppDetails> appDetailsList) {
        double volume = 0;

        for (AppDetails appDetails : appDetailsList) {
            volume = volume + appDetails.getUploads();
        }
        return volume;
    }

    public long getTotalUploadsInBytes(List<AppDetails> appDetailsList) {
        long volume = 0L;

        for (AppDetails appDetails : appDetailsList) {
            volume = volume + appDetails.getUploadsInBytes();

        }
        return volume;
    }

    public long getTotalDownloadsInBytes(List<AppDetails> appDetailsList) {
        long volume = 0L;

        for (AppDetails appDetails : appDetailsList) {
            volume = volume + appDetails.getDownloadsInBytes();
        }
        return volume;
    }

    public static int getDifferenceInTime(long startTime, long endTime) {

        long diff = endTime - startTime;

        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    public static int getDifferenceInHours(long startTime, long endTime) {

        long diff = endTime - startTime;

        return (int) (diff / (1000 * 60 * 60));
    }

    public static void showInfoToast(Context ctx, String message) {
        Toasty.info(ctx, message).show();
    }

    public static void showSuccessToast(Context ctx, String message) {
        Toasty.success(ctx, message).show();
    }

    public static void showErrorToast(Context ctx, String message) {
        Toasty.error(ctx, message).show();
    }

    public static double roundOffToAnySignificantFigure(int size, double val) {
        BigDecimal number = new BigDecimal(val);
        BigDecimal decimal = number.setScale(size, RoundingMode.HALF_EVEN);
        return decimal.doubleValue();
    }


}
