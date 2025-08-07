package ridc.sduma.kifurushi;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.models.Results;
import ridc.sduma.kifurushi.threads.StatisticsExecutor;

import java.util.List;

public class StatisticsViewModel extends ViewModel {

 private final StatisticsExecutor executor;

 public StatisticsViewModel(){

     executor = StatisticsExecutor.getInstance();
 }

    public void fetchCellularStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){
        executor.fetchCellularStatistics(startDate, endDate, netStatsUtil, appUtils, context);
    }

    public void fetchWifiStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){
        executor.fetchWifiStatistics(startDate, endDate, netStatsUtil, appUtils, context);
    }

    public void fetchDailyCellularStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){
        executor.fetchDailyCellularStatistics(startDate, endDate, netStatsUtil, appUtils, context);

    }

    public void fetchDailyWifiStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){
        executor.fetchDailyWifiStatistics(startDate, endDate, netStatsUtil, appUtils, context);
    }

    public void fetchDailyCellularShortStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context){
        executor.fetchDailyCellularShortStatistics(startDate, endDate, netStatsUtil, context);

    }

    public void fetchDailyShortWifiStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil){
        executor.fetchDailyShortWifiStatistics(startDate, endDate, netStatsUtil);
    }

    public void fetchWeeklyCellularShortStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context){
        executor.fetchWeeklyCellularShortStatistics(startDate, endDate, netStatsUtil, context);

    }

    public void fetchWeeklyShortWifiStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil){
        executor.fetchWeeklyShortWifiStatistics(startDate, endDate, netStatsUtil);

    }

    public void fetchCellularUsageInTimeRange(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context, AppUtils utils){
        executor.fetchCellularUsageInTimeRange(startDate, endDate, netStatsUtil, context, utils);
    }

    public void fetchWifiUsageInTimeRange(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context, AppUtils utils){
        executor.fetchWifiUsageInTimeRange(startDate, endDate, netStatsUtil, context, utils);
    }







    public LiveData<List<AppDetails>> getCellularUsage(){
        return executor.getCellularUsage();
    }

    public LiveData<List<AppDetails>> getWifiUsage(){
        return executor.getWifiUsage();
    }

    public LiveData<List<AppDetails>> getDailyCellularUsage(){
        return executor.getDailyCellularUsage();
    }

    public LiveData<List<AppDetails>> getDailyWifiUsage(){
        return executor.getDailyWifiUsage();
    }

    public LiveData<Results> getDailyShortCellularUsage(){
        return executor.getDailyShortCellularUsage();
    }

    public LiveData<Results> getDailyShortWifiUsage(){
        return executor.getDailyShortWifiUsage();
    }

    public LiveData<Results> getWeeklyShortCellularUsage(){
        return executor.getWeeklyShortCellularUsage();
    }
    public LiveData<Results> getWeeklyShortWifiUsage(){
        return executor.getWeeklyShortWifiUsage();
    }

    public LiveData<List<AppDetails>> getCellularUsageInRange(){
        return executor.getCellularUsageInRange();
    }

    public LiveData<List<AppDetails>> getWifiUsageInRange(){
        return executor.getWifiUsageInRange();
    }




}
