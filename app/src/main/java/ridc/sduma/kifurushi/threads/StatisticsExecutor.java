package ridc.sduma.kifurushi.threads;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ridc.sduma.kifurushi.models.Results;
import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import java.util.List;
import java.util.concurrent.Executors;
public class StatisticsExecutor {

    private static StatisticsExecutor instance;
    private final MutableLiveData<List<AppDetails>> cellularResults;
    private final MutableLiveData<List<AppDetails>> wifiResults;
    private final MutableLiveData<List<AppDetails>> mDailyCellular;
    private final MutableLiveData<List<AppDetails>> mDailyWifi;
    private final MutableLiveData<List<AppDetails>> mCellularRangeResults;
    private final MutableLiveData<List<AppDetails>> mWifiRangeResults;
    private final MutableLiveData<Results> mDailyCellularStats;
    private final MutableLiveData<Results> mDailyWifiStats;
    private final MutableLiveData<Results> mWeeklyCellularStats;
    private final MutableLiveData<Results> mWeeklyWifiStats;
    private GetCellularStatisticsRunnable getCellularStatisticsRunnable;


    private GetWifiStatisticsRunnable getWifiStatisticsRunnable;
    private GetDailyCellularStatisticsRunnable getDailyCellularStatisticsRunnable;
    private GetDailyWifiStatisticsRunnable getDailyWifiStatisticsRunnable;
    private GetDailyShortCellularStatisticsRunnable getDailyShortCellularStatisticsRunnable;
    private GetDailyShortWifiStatisticsRunnable getDailyShortWifiStatisticsRunnable;
    private GetWeeklyShortCellularStatisticsRunnable getWeeklyShortCellularStatisticsRunnable;
    private GetWeeklyShortWifiStatisticsRunnable getWeeklyShortWifiStatisticsRunnable;
    private GetCellularDataForSpecificRangeRunnable getCellularDataForSpecificRangeRunnable;
    private GetWifiDataForSpecificRangeRunnable getWifiDataForSpecificRangeRunnable;

    public static StatisticsExecutor getInstance(){
        if (instance == null){
            instance = new StatisticsExecutor();
        }

        return instance;
    }

    private StatisticsExecutor(){
        cellularResults = new MutableLiveData<>();
        wifiResults = new MutableLiveData<>();
        mDailyCellular = new MutableLiveData<>();
        mDailyWifi = new MutableLiveData<>();
        mDailyCellularStats = new MutableLiveData<>();
        mDailyWifiStats = new MutableLiveData<>();
        mWeeklyCellularStats = new MutableLiveData<>();
        mWeeklyWifiStats = new MutableLiveData<>();
        mCellularRangeResults = new MutableLiveData<>();
        mWifiRangeResults = new MutableLiveData<>();
    }


    public void fetchCellularStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){
        if (getCellularStatisticsRunnable != null)
            getCellularStatisticsRunnable = null;
        getCellularStatisticsRunnable = new GetCellularStatisticsRunnable(startDate, endDate, netStatsUtil, appUtils, context);
        Executors.newSingleThreadScheduledExecutor().execute(getCellularStatisticsRunnable);

    }

    public void fetchDailyCellularStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){
        if (getDailyCellularStatisticsRunnable != null)
            getDailyCellularStatisticsRunnable = null;
        getDailyCellularStatisticsRunnable = new GetDailyCellularStatisticsRunnable(startDate, endDate, netStatsUtil, appUtils, context);

        Executors.newSingleThreadExecutor().execute(getDailyCellularStatisticsRunnable);

    }

    public void fetchDailyCellularShortStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context){
        if (getDailyShortCellularStatisticsRunnable != null)
            getDailyShortCellularStatisticsRunnable = null;
        getDailyShortCellularStatisticsRunnable = new GetDailyShortCellularStatisticsRunnable(startDate, endDate, netStatsUtil, context);

        Executors.newSingleThreadExecutor().execute(getDailyShortCellularStatisticsRunnable);

    }

    public void fetchWeeklyCellularShortStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context){
        if (getWeeklyShortCellularStatisticsRunnable != null)
            getWeeklyShortCellularStatisticsRunnable = null;
        getWeeklyShortCellularStatisticsRunnable = new GetWeeklyShortCellularStatisticsRunnable(startDate, endDate, netStatsUtil, context);

        Executors.newSingleThreadExecutor().execute(getWeeklyShortCellularStatisticsRunnable);

    }

    public void fetchWifiStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){
        if (getWifiStatisticsRunnable != null)
            getWifiStatisticsRunnable = null;
        getWifiStatisticsRunnable = new GetWifiStatisticsRunnable(startDate, endDate, netStatsUtil, appUtils, context);
        Executors.newSingleThreadExecutor().execute(getWifiStatisticsRunnable);
    }

    public void fetchDailyWifiStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils appUtils, Context context){
        if (getDailyWifiStatisticsRunnable != null)
            getDailyWifiStatisticsRunnable = null;
        getDailyWifiStatisticsRunnable = new GetDailyWifiStatisticsRunnable(startDate, endDate, netStatsUtil, appUtils, context);
        Executors.newSingleThreadExecutor().execute(getDailyWifiStatisticsRunnable);
    }

    public void fetchDailyShortWifiStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil){
        if (getDailyShortWifiStatisticsRunnable != null)
            getDailyShortWifiStatisticsRunnable = null;
        getDailyShortWifiStatisticsRunnable = new GetDailyShortWifiStatisticsRunnable(startDate, endDate, netStatsUtil);
        Executors.newSingleThreadExecutor().execute(getDailyShortWifiStatisticsRunnable);
    }

    public void fetchWeeklyShortWifiStatistics(long startDate, long endDate, NetStatsUtil netStatsUtil){
        if (getWeeklyShortWifiStatisticsRunnable != null)
            getWeeklyShortWifiStatisticsRunnable = null;
        getWeeklyShortWifiStatisticsRunnable = new GetWeeklyShortWifiStatisticsRunnable(startDate, endDate, netStatsUtil);
        Executors.newSingleThreadExecutor().execute(getWeeklyShortWifiStatisticsRunnable);
    }

    public void fetchCellularUsageInTimeRange(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context, AppUtils utils){
        if (getCellularDataForSpecificRangeRunnable != null)
            getCellularDataForSpecificRangeRunnable = null;
        getCellularDataForSpecificRangeRunnable = new GetCellularDataForSpecificRangeRunnable(startDate, endDate, netStatsUtil, context, utils);
        Executors.newSingleThreadExecutor().execute(getCellularDataForSpecificRangeRunnable);
    }

    public void fetchWifiUsageInTimeRange(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context, AppUtils utils){
        if (getWifiDataForSpecificRangeRunnable != null)
            getWifiDataForSpecificRangeRunnable = null;
        getWifiDataForSpecificRangeRunnable = new GetWifiDataForSpecificRangeRunnable(startDate, endDate, netStatsUtil, context, utils);
        Executors.newSingleThreadExecutor().execute(getWifiDataForSpecificRangeRunnable);
    }








    public LiveData<List<AppDetails>> getCellularUsage(){
        return cellularResults;
    }
    public LiveData<List<AppDetails>> getDailyCellularUsage(){
        return mDailyCellular;
    }
    public LiveData<Results> getDailyShortCellularUsage(){
        return mDailyCellularStats;
    }
    public LiveData<Results> getWeeklyShortCellularUsage(){
        return mWeeklyCellularStats;
    }
    public LiveData<List<AppDetails>> getCellularUsageInRange(){
        return mCellularRangeResults;
    }


    public LiveData<List<AppDetails>> getWifiUsage(){
        return wifiResults;
    }
    public LiveData<List<AppDetails>> getDailyWifiUsage(){
        return mDailyWifi;
    }
    public LiveData<Results> getDailyShortWifiUsage(){
        return mDailyWifiStats;
    }
    public LiveData<Results> getWeeklyShortWifiUsage(){
        return mWeeklyWifiStats;
    }
    public LiveData<List<AppDetails>> getWifiUsageInRange(){
        return mWifiRangeResults;
    }





    private class GetCellularStatisticsRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;
        private final AppUtils utils;
        private final Context context;

        public GetCellularStatisticsRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils utils, Context context) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.utils = utils;
            this.context = context;
        }

        @Override
        public void run() {
            List<AppDetails> cellularUsage = utils.getListOfAppsAndTheirCellularUsage(context, netStatsUtil, startDate, endDate);
            cellularResults.postValue(cellularUsage);
        }
    }

    private class GetDailyCellularStatisticsRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;
        private final AppUtils utils;
        private final Context context;

        public GetDailyCellularStatisticsRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils utils, Context context) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.utils = utils;
            this.context = context;
        }
        @Override
        public void run() {
            List<AppDetails> cellularUsage = utils.getListOfAppsAndTheirCellularUsage(context, netStatsUtil, startDate, endDate);
            mDailyCellular.postValue(cellularUsage);
        }
    }

    private class GetWifiStatisticsRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;
        private final AppUtils utils;
        private final Context context;

        public GetWifiStatisticsRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils utils, Context context) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.utils = utils;
            this.context = context;
        }
        @Override
        public void run() {
            List<AppDetails> wifiUsage = utils.getListOfAppsAndTheirWifiUsage(context, netStatsUtil, startDate, endDate);
            wifiResults.postValue(wifiUsage);
        }
    }

    private class GetDailyWifiStatisticsRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;
        private final AppUtils utils;
        private final Context context;

        public GetDailyWifiStatisticsRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil, AppUtils utils, Context context) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.utils = utils;
            this.context = context;
        }
        @Override
        public void run() {
            List<AppDetails> wifiUsage = utils.getListOfAppsAndTheirWifiUsage(context, netStatsUtil, startDate, endDate);
            mDailyWifi.postValue(wifiUsage);
        }
    }

    private class GetDailyShortWifiStatisticsRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;

        public GetDailyShortWifiStatisticsRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
        }
        @Override
        public void run() {
            long received = netStatsUtil.getAllRxBytesWifi(startDate, endDate);
            long sent = netStatsUtil.getAllTxBytesWifi(startDate, endDate);
            long total = received+sent;
            Results results = new Results(received, sent, total);
            mDailyWifiStats.postValue(results);

        }
    }

    private class GetWeeklyShortWifiStatisticsRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;

        public GetWeeklyShortWifiStatisticsRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
        }
        @Override
        public void run() {
            long received = netStatsUtil.getAllRxBytesWifi(startDate, endDate);
            long sent = netStatsUtil.getAllTxBytesWifi(startDate, endDate);
            long total = received+sent;
            Results results = new Results(received, sent, total);
            mWeeklyWifiStats.postValue(results);

        }
    }

    private class GetDailyShortCellularStatisticsRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;
        private final Context context;

        public GetDailyShortCellularStatisticsRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.context = context;
        }
        @Override
        public void run() {
            long received = netStatsUtil.getAllRxBytesMobile(context, startDate, endDate);
            long sent = netStatsUtil.getAllTxBytesMobile(context, startDate, endDate);
            long total = received+sent;
            Results results = new Results(received, sent, total);
            mDailyCellularStats.postValue(results);


        }
    }

    private class GetWeeklyShortCellularStatisticsRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;
        private final Context context;

        public GetWeeklyShortCellularStatisticsRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.context = context;
        }
        @Override
        public void run() {
            long received = netStatsUtil.getAllRxBytesMobile(context, startDate, endDate);
            long sent = netStatsUtil.getAllTxBytesMobile(context, startDate, endDate);
            long total = received+sent;
            Results results = new Results(received, sent, total);
            mWeeklyCellularStats.postValue(results);

        }
    }

    class GetCellularDataForSpecificRangeRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;
        private final Context context;
        private final AppUtils utils;

        public GetCellularDataForSpecificRangeRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context, AppUtils utils) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.context = context;
            this.utils = utils;
        }
        @Override
        public void run() {

            List<AppDetails> cellularUsage = utils.getListOfAppsAndTheirCellularUsage(context, netStatsUtil, startDate, endDate);
            mCellularRangeResults.postValue(cellularUsage);

        }
    }

    class GetWifiDataForSpecificRangeRunnable implements Runnable{

        private final long startDate;
        private final long endDate;
        private final NetStatsUtil netStatsUtil;
        private final Context context;
        private final AppUtils utils;

        public GetWifiDataForSpecificRangeRunnable(long startDate, long endDate, NetStatsUtil netStatsUtil, Context context, AppUtils utils) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.netStatsUtil = netStatsUtil;
            this.context = context;
            this.utils = utils;
        }
        @Override
        public void run() {

            List<AppDetails> wifiUsage = utils.getListOfAppsAndTheirWifiUsage(context, netStatsUtil, startDate, endDate);
            mWifiRangeResults.postValue(wifiUsage);

        }
    }





}
