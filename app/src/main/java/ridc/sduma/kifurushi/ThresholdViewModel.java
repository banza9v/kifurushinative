package ridc.sduma.kifurushi;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.threads.ThresholdThread;

import java.util.List;

public class ThresholdViewModel extends ViewModel {

    private ThresholdThread thread;

    public ThresholdViewModel(){
        thread = ThresholdThread.getInstance();
    }

    public void fetchAppsThresholdUsage(long startData, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){

        thread.fetchAppsThresholdUsage(startData, endDate, netStatsUtil, appUtils, context);

    }



    public LiveData<List<AppDetails>> getAppUsage(){
        return thread.getAppUsage();
    }

}
