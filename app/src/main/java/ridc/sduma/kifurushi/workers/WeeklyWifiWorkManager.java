package ridc.sduma.kifurushi.workers;

import android.app.usage.NetworkStatsManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.List;

import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.threads.StatisticsExecutor;

public class WeeklyWifiWorkManager extends Worker {

    public WeeklyWifiWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Calendar cal;

        StatisticsExecutor executor = StatisticsExecutor.getInstance();

        NetworkStatsManager manager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);

        NetStatsUtil statsUtil = new NetStatsUtil(manager);
        AppUtils appUtils = new AppUtils();

        int firstDay = PreferenceUtils.getFirstDayOfWeek(getApplicationContext());
        System.out.println("FIRST_DAY_OF_WEEK "+firstDay);
        long startDate, endDate;
        cal = Calendar.getInstance();
        endDate = cal.getTimeInMillis();
        cal.setFirstDayOfWeek(firstDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        startDate = cal.getTimeInMillis();
        System.out.println("START_DATE ================> "+startDate);
        System.out.println("END_DATE ================> "+endDate);

        long allRxBytesWifi = statsUtil.getAllRxBytesWifi(startDate, endDate);
        long allTxBytesWifi = statsUtil.getAllTxBytesWifi(startDate, endDate);

        double downloads = AppUtils.bytesConverter(allRxBytesWifi);
        double uploads = AppUtils.bytesConverter(allTxBytesWifi);
        String rUnit = AppUtils.getUnit(allRxBytesWifi);
        String tUnit = AppUtils.getUnit(allTxBytesWifi);

        System.out.println("RxWifi ============================> "+downloads+rUnit);
        System.out.println("TxWifi ============================> "+uploads+tUnit);

        List<AppDetails> appDetails = appUtils.getListOfAppsAndTheirWifiUsage(getApplicationContext(), statsUtil, 1651305845L, endDate);

        double totalDownloads = appUtils.getTotalDownloads(appDetails);
        long totalBytes = appUtils.getTotalBytes(appDetails);
        String totalUnit = appUtils.getTotalUnit(totalBytes);

        System.out.println("Total "+totalDownloads+" "+totalUnit);

        return Result.success();
    }

}
