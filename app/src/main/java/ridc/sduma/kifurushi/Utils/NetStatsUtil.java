package ridc.sduma.kifurushi.Utils;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class NetStatsUtil {
    NetworkStatsManager networkStatsManager;

    public NetStatsUtil(NetworkStatsManager networkStatsManager) {
        this.networkStatsManager = networkStatsManager;
    }

    public long getAllRxBytesMobile(Context context, long startTime, long endTime) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    startTime,
                    endTime);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesMobile(Context context, long startTime, long endTime) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    startTime,
                    endTime);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getAllRxBytesWifi(long startTime, long endTime) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    startTime,
                    endTime);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesWifi(long startTime, long endTime) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    startTime,
                    endTime);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public AppDetails getUsageStatsForUid(Context context, AppDetails appDetails, int netWorkType, long startTime, long endTime){

        try {

            NetworkStats networkStats = networkStatsManager.querySummary(netWorkType, getSubscriberId(context, netWorkType), startTime, endTime);

            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            long rxBytes = 0L;
            long txBytes = 0L;

            while (networkStats.hasNextBucket()){
                networkStats.getNextBucket(bucket);
                if (bucket.getUid() == appDetails.getUID()){
                    rxBytes = rxBytes + bucket.getRxBytes();
                    txBytes = txBytes + bucket.getTxBytes();
                }
            }

            appDetails.setDownloads(AppUtils.bytesConverter(rxBytes));
            appDetails.setUploads(AppUtils.bytesConverter(txBytes));
            appDetails.setDownloadsInBytes(rxBytes);
            appDetails.setUploadsInBytes(txBytes);
            appDetails.setDownloadUnit(AppUtils.getUnit(rxBytes));
            appDetails.setUploadUnit(AppUtils.getUnit(txBytes));
            appDetails.setTotalBytes(AppUtils.bytesConverter((rxBytes+txBytes)));
            appDetails.setTotalInBytes((rxBytes + txBytes));

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return appDetails;
    }

    public long getPackageRxBytesMobile(Context context, long startTime, long endTime, int packageUid) {
        NetworkStats networkStats;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                startTime,
                endTime,
                packageUid);

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    public UsageStats getTotalBytesByMobile(Context context, long startTime, long endTime){
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForUser(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),startTime, endTime);
            long rxBytes;
            long txBytes;

            rxBytes = bucket.getRxBytes();
            txBytes = bucket.getTxBytes();

            return new UsageStats((rxBytes+txBytes), rxBytes, txBytes);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new UsageStats(0, 0, 0);
    }

    public UsageStats getTotalBytesByWifi(Context context, long startTime, long endTime){
        NetworkStats.Bucket networkStats;
        try {
            networkStats = networkStatsManager.querySummaryForUser(ConnectivityManager.TYPE_WIFI, getSubscriberId(context, ConnectivityManager.TYPE_WIFI),startTime, endTime);
            long rxBytes;
            long txBytes;

            rxBytes = networkStats.getRxBytes();
            txBytes = networkStats.getTxBytes();

            return new UsageStats((rxBytes+txBytes), rxBytes, txBytes);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new UsageStats(0, 0, 0);
    }

    public long getPackageTxBytesMobile(Context context, long startTime, long endTime, int packageUid) {
        NetworkStats networkStats;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                startTime,
                endTime,
                packageUid
                );

        long txBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
            System.out.println(txBytes + "MATUMIZ===>" + packageUid);
        }
        networkStats.close();
        return txBytes;
    }

    public long getPackageRxBytesWifi(int packageUid, long startTime, long endTime) {
        NetworkStats networkStats;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_WIFI,
                "",
                startTime,
                endTime,
                packageUid);

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    public long getPackageTxBytesWifi(int packageUid, long startTime, long endTime) {
        NetworkStats networkStats;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_WIFI,
                "",
                startTime,
                endTime,
                packageUid);

        long txBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
        }
        networkStats.close();
        return txBytes;
    }

    @SuppressLint("HardwareIds")
    private String getSubscriberId(Context context, int networkType) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            if (ConnectivityManager.TYPE_MOBILE == networkType) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return tm.getSubscriberId();
            }
        }
        return null;
    }

    public static class UsageStats{

        private long total;
        private long downloads;
        private long uploads;

        public UsageStats(long total, long downloads, long uploads) {
            this.total = total;
            this.downloads = downloads;
            this.uploads = uploads;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public long getDownloads() {
            return downloads;
        }

        public void setDownloads(long downloads) {
            this.downloads = downloads;
        }

        public long getUploads() {
            return uploads;
        }

        public void setUploads(long uploads) {
            this.uploads = uploads;
        }
    }

}
