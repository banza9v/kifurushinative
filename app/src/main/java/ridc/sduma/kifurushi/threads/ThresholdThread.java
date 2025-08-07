package ridc.sduma.kifurushi.threads;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;

import java.util.List;
import java.util.concurrent.Executors;

public class ThresholdThread {
    private static ThresholdThread instance;
    private final MutableLiveData<List<AppDetails>> mApps;

    public static ThresholdThread getInstance(){
        if (instance == null){
            instance = new ThresholdThread();
        }

        return instance;
    }

    private ThresholdThread(){
        mApps = new MutableLiveData<>();
    }


    GetAppsDataFroThresholdRunnable getAppsDataFroThresholdRunnable;


    public void fetchAppsThresholdUsage(long startData, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){

        if (getAppsDataFroThresholdRunnable != null)
            getAppsDataFroThresholdRunnable = null;

        getAppsDataFroThresholdRunnable = new GetAppsDataFroThresholdRunnable(startData, endDate, netStatsUtil, appUtils, context);

        Executors.newCachedThreadPool().execute(getAppsDataFroThresholdRunnable);

    }



    public LiveData<List<AppDetails>> getAppUsage(){
        return mApps;
    }








    class GetAppsDataFroThresholdRunnable implements Runnable{
        long startData;
        long endDate;
        NetStatsUtil netStatsUtil;
        AppUtils appUtils;
        Context context;

        public GetAppsDataFroThresholdRunnable(long startData, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context) {
            this.startData = startData;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.appUtils = appUtils;
            this.context = context;
        }

        @Override
        public void run() {

            List<AppDetails> usage = appUtils.getListOfAppsAndTheirCellularUsage(context, netStatsUtil, startData, endDate);
            System.out.println(usage);
            mApps.postValue(usage);

        }
    }

}
