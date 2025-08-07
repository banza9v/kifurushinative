package ridc.sduma.kifurushi.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Comparator;

public class AppComparator implements Comparator<ApplicationInfo> {
    private final Context context;
    private final NetStatsUtil statsUtil;
    private final long startTime;
    private final long endTime;

    public AppComparator(Context context, NetStatsUtil statsUtil, long startTime, long endTime) {
        this.context = context;
        this.statsUtil = statsUtil;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public int compare(ApplicationInfo appInfo1, ApplicationInfo appInfo2) {

        return appInfo1.loadLabel(context.getPackageManager())
                .toString().compareTo(appInfo2.loadLabel(context.getPackageManager()).toString());
    }

    @Override
    public Comparator<ApplicationInfo> reversed() {
        return Comparator.super.reversed();
    }
}
