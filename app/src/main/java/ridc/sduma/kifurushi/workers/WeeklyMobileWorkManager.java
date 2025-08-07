package ridc.sduma.kifurushi.workers;

import android.app.usage.NetworkStatsManager;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.threads.StatisticsExecutor;

public class WeeklyMobileWorkManager extends Worker {

    public WeeklyMobileWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        long startDate;
        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(firstDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        startDate = cal.getTimeInMillis();

        executor.fetchCellularStatistics(startDate, System.currentTimeMillis(), statsUtil, appUtils, getApplicationContext());
        return Result.success();
    }
}
