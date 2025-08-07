package ridc.sduma.kifurushi.fragments;

import static android.app.usage.NetworkStats.Bucket.UID_ALL;

import android.app.Activity;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ridc.sduma.kifurushi.Params;
import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.StatisticsViewModel;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.Constants;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.activities.CellularWeeklyActivity;
import ridc.sduma.kifurushi.activities.DailyCellularActivity;
import ridc.sduma.kifurushi.activities.DailyWifiActivity;
import ridc.sduma.kifurushi.activities.WifiActivity;
import ridc.sduma.kifurushi.activities.WifiRangeActivity;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class WiFiFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    NetworkStatsManager statsManager;
    Calendar cal;
    PieChart mChart;
    LinearLayout noDataUsed;
    TextView dailyDownloadsTv;
    TextView dailyUploadsTv;
    TextView subDailyTotalTv;
    TextView errorMsgTv;
    TextView startDateTv;
    TextView endDateTv;
    TextView weekDate;
    Spinner durationSpinner;
    AppCompatButton getStatisticsBtn;
    ProgressBar progressBarWeekly, progressBarDaily;
    LinearLayout durationLayout;
    View view;

    StatisticsViewModel viewModel;
    AppUtils utils;
    NetStatsUtil netStatsUtil;

    private long startMillis, endMillis, startTimeMillisToNextScreen, endTimeMillisToNextScreen;
    private long weekRxBytes, weekTxBytes, weekTotalBytes, durationRxBytes, durationTxBytes, durationTotalBytes;
    SimpleDateFormat sdf;

    Date date1, date2;
//    date2

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate1() {
        return date1;
    }

    public Date getDate2() {
        return date2;
    }


    public WiFiFragment() {
        // Required empty public constructor
    }

    public static WiFiFragment newInstance() {
        return new WiFiFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wifi, container, false);
        startDateTv = view.findViewById(R.id.start_date);
        endDateTv = view.findViewById(R.id.end_date);
        mChart = view.findViewById(R.id.weeklyChart);
        dailyDownloadsTv = view.findViewById(R.id.day_downloads);
        dailyUploadsTv = view.findViewById(R.id.day_uploads);
        subDailyTotalTv = view.findViewById(R.id.day_total);
        errorMsgTv = view.findViewById(R.id.errorMsg);
        weekDate = view.findViewById(R.id.weekDate);
        getStatisticsBtn = view.findViewById(R.id.get_statistic_btn);
        durationSpinner = view.findViewById(R.id.durationSpinner);
        noDataUsed = view.findViewById(R.id.noDataChartDisplay);
        progressBarDaily = view.findViewById(R.id.durationProgressBar);
        progressBarWeekly = view.findViewById(R.id.weekProgressBar);
        durationLayout = view.findViewById(R.id.durationLayout);

        statsManager = (NetworkStatsManager) view.getContext().getSystemService(Context.NETWORK_STATS_SERVICE);
        netStatsUtil = new NetStatsUtil(statsManager);

        utils = new AppUtils();

        sdf = new SimpleDateFormat("EEEE dd, HH:mm", new Locale(PreferenceUtils.getLanguagePreference(view.getContext())));

        ArrayList<String> spinnerItems = new ArrayList<>();
        spinnerItems.add(getString(R.string.hrs_4));
        spinnerItems.add(getString(R.string.hrs_8));
        spinnerItems.add(getString(R.string.hrs_12));
        spinnerItems.add(getString(R.string.day_1));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.duration_layout_item, R.id.durationText, spinnerItems);
        durationSpinner.setAdapter(adapter);
        durationSpinner.setOnItemSelectedListener(this);


        if (getActivity() != null) {
            viewModel = new ViewModelProvider(getViewModelStore(), (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                    .get(StatisticsViewModel.class);

            fetchDataBasedOnSelectedDuration(view.getContext());

        }


        startDateTv.setOnClickListener(v -> new SingleDateAndTimePickerDialog.Builder(getContext())
                .bottomSheet()
                .curved()
                .displayHours(true)
                .displayMinutes(true)
                //.todayText(getString(R.string.today))
                .minutesStep(1)
                .displayYears(false)
                .displayListener(picker -> {
                    // Retrieve the SingleDateAndTimePicker
                    picker.setCurved(true);
                    picker.setTextColor(ContextCompat.getColor(view.getContext(), R.color.secondaryText));
                    picker.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.cardBackground));
                    picker.setSelectedTextColor(ContextCompat.getColor(view.getContext(), R.color.primaryText));
                    picker.setAnimationCacheEnabled(true);
                    picker.setDefaultDate(new Date());
                })
                .title(getString(R.string.pick_time))
                .listener(date -> {
                    startMillis = date.getTime();
                    setDate1(date);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm", Locale.getDefault());
                    String dateFormated = simpleDateFormat.format(date);
                    startDateTv.setText(dateFormated);
                }).display());


        endDateTv.setOnClickListener(v -> new SingleDateAndTimePickerDialog.Builder(getContext())
                .bottomSheet()
                .curved()
                .displayHours(true)
                .displayMinutes(true)
                //.todayText(getString(R.string.today))
                .minutesStep(1)
                .displayYears(false)
                .displayListener(picker -> {
                    // Retrieve the SingleDateAndTimePicker
                    picker.setCurved(true);
                    picker.setTextColor(ContextCompat.getColor(view.getContext(), R.color.secondaryText));
                    picker.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.cardBackground));
                    picker.setSelectedTextColor(ContextCompat.getColor(view.getContext(), R.color.primaryText));
                    picker.setAnimationCacheEnabled(true);
                    picker.setDefaultDate(new Date());
                })
                .title(getString(R.string.pick_time))
                .listener(date -> {
                    endMillis = date.getTime();
                    setDate2(date);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm", Locale.getDefault());
                    String dateFormat = simpleDateFormat.format(date);
                    endDateTv.setText(dateFormat);
                }).display());

        getStatisticsBtn.setOnClickListener(v -> fetchUsageData());

        fetchWeeklyWifiUsage(viewModel, statsManager, utils, view.getContext());

        return view;
    }

    private void fetchDataBasedOnSelectedDuration(Context context) {
        if (PreferenceUtils.isWifiDurationSelected(context)){
            int pos = PreferenceUtils.getWifiDurationSelectedPosition(view.getContext());
            durationSpinner.setSelection(pos, true);

            Calendar now = Calendar.getInstance();
            long endTime = now.getTimeInMillis();
            long startTime;
            if (pos == 0){
                now.add(Calendar.HOUR, -4);
            }else if (pos == 1){
                now.add(Calendar.HOUR, -8);
            }else if (pos == 2){
                now.add(Calendar.HOUR, -12);
            }else {
                now.add(Calendar.HOUR, -23);
            }
            startTime = now.getTimeInMillis();
            viewModel.fetchDailyShortWifiStatistics(startTime, endTime, netStatsUtil);
            viewModel.fetchDailyWifiStatistics(startTime, endTime, netStatsUtil, utils, getContext());

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int firstDay = PreferenceUtils.getFirstDayOfWeek(view.getContext());
        long startDate, endDate;

        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(firstDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        endDate = System.currentTimeMillis();

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        startDate = cal.getTimeInMillis();

        String displayDate = getString(R.string.data_used_this_week)+"\n("+sdf.format(new Date(startDate))+" - "+sdf.format(new Date(endDate))+")";
        weekDate.setText(displayDate);

        fetchAllSentAndReceivedBytesWifiWeekly(netStatsUtil, startDate, endDate);

        view.findViewById(R.id.nextWeek).setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), WifiActivity.class);
            intent.putExtra(WifiActivity.EXTRA_START_TIME_MILLIS, startDate);
            intent.putExtra(WifiActivity.EXTRA_END_TIME_MILLIS, endDate);
            intent.putExtra(WifiActivity.EXTRA_RxBYTES, weekRxBytes);
            intent.putExtra(WifiActivity.EXTRA_TxBYTES, weekTxBytes);
            intent.putExtra(WifiActivity.EXTRA_TOTAL_BYTES, weekTotalBytes);
            startActivity(intent);
        });

        view.findViewById(R.id.todayCard).setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), DailyWifiActivity.class);
            intent.putExtra(DailyWifiActivity.EXTRA_START_TIME_MILLIS, startTimeMillisToNextScreen);
            intent.putExtra(DailyWifiActivity.EXTRA_END_TIME_MILLIS, endTimeMillisToNextScreen);
            intent.putExtra(DailyWifiActivity.EXTRA_RxBYTES, durationRxBytes);
            intent.putExtra(DailyWifiActivity.EXTRA_TxBYTES, durationTxBytes);
            intent.putExtra(DailyWifiActivity.EXTRA_TOTAL_BYTES, durationTotalBytes);
            startActivity(intent);
        });



    }

    private void fetchAllSentAndReceivedBytesWifiWeekly(NetStatsUtil statsUtil, long starTime, long endTime){
        weekRxBytes = statsUtil.getAllRxBytesWifi(starTime, endTime);
        weekTxBytes = statsUtil.getAllTxBytesWifi(starTime, endTime);
        weekTotalBytes = weekRxBytes+weekTxBytes;
        boolean isAppsEmpty = weekTotalBytes <= 0;
        setupViews(weekRxBytes, weekTxBytes, weekTotalBytes, isAppsEmpty);
    }

    private void fetchAllSentAndReceivedBytesWifiByDuration(NetStatsUtil statsUtil, long starTime, long endTime){
        NetStatsUtil.UsageStats usageStats = statsUtil.getTotalBytesByWifi(requireContext(), starTime, endTime);
        durationRxBytes = usageStats.getDownloads();
        durationTxBytes = usageStats.getUploads();
        durationTotalBytes = usageStats.getTotal();
        setupDailyViews(durationRxBytes, durationTxBytes, durationTotalBytes);
    }

    private void setupViews(long received, long sent, long total, boolean isAppsEmpty) {
        /*============================== Total ========================*/
        double totalBytes = AppUtils.bytesConverter(total);
        String unit = AppUtils.getUnit(total);
        /*============================== Total ========================*/


        float downloadsComposition = (received / (float) total) * 100;
        float uploadsComposition = (sent / (float) total) * 100;


        double downloads = AppUtils.bytesConverter(received);
        double uploads = AppUtils.bytesConverter(sent);

        BigDecimal number = new BigDecimal(totalBytes);
        BigDecimal decimal = number.setScale(1, RoundingMode.HALF_EVEN);

        BigDecimal rNumber = new BigDecimal(downloads);
        BigDecimal rDecimal = rNumber.setScale(1, RoundingMode.HALF_EVEN);

        BigDecimal tNumber = new BigDecimal(uploads);
        BigDecimal tDecimal = tNumber.setScale(1, RoundingMode.HALF_EVEN);

        String rUnit = AppUtils.getUnit(received);
        String tUnit = AppUtils.getUnit(sent);

        int[] colors = new int[]{
                Color.parseColor("#00bcd4"),
                Color.parseColor("#e57373")
        };


        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(downloadsComposition, getString(R.string.downloads)+" ("+rDecimal.doubleValue()+" "+rUnit+")"));
        entries.add(new PieEntry(uploadsComposition, getString(R.string.uploads)+" ("+tDecimal.doubleValue()+" "+tUnit+")"));
        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        set.setColors(colors);
        set.setValueTextColor(Color.WHITE);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //set.setLabel("%");

        Description description = new Description();
        description.setText("");

        /*mChart.getLegend().setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        mChart.getLegend().setTextColor(ContextCompat.getColor(getContext(), R.color.primaryText));
        mChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        mChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        mChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);*/
        mChart.getLegend().setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryText));
        mChart.setData(data);
        mChart.setHoleRadius(62f);
        mChart.setTransparentCircleRadius(68f);
        mChart.setHoleColor(ContextCompat.getColor(requireContext(), R.color.cardBackground));
        mChart.setEntryLabelColor(ContextCompat.getColor(requireContext(), R.color.primaryText));
        mChart.setEntryLabelTextSize(11f);
//        mChart.setUsePercentValues(true);
        mChart.setCenterText(decimal.doubleValue() + "" + unit + "\n" + getString(R.string.total).toUpperCase(Locale.ROOT));
        mChart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.primaryText));
        mChart.setCenterTextSize(11f);
        mChart.setCenterTextTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        mChart.setDescription(description);
        mChart.invalidate(); // refresh

        if (total <= 0){
            mChart.setVisibility(View.GONE);
            noDataUsed.setVisibility(View.VISIBLE);
        }
        progressBarWeekly.setVisibility(View.GONE);
        mChart.setVisibility(View.VISIBLE);


    }


    private void setupDailyViews(long received, long sent, long total) {

        double tot = AppUtils.bytesConverter(total);
        String unit = AppUtils.getUnit(total);

        /*============================== Downloads ========================*/
        double downloads = AppUtils.bytesConverter(received);
        String downloadsUnit = AppUtils.getUnit(received);
        /*============================== Downloads ========================*/


        /*============================== Uploads ========================*/
        double uploads = AppUtils.bytesConverter(sent);
        String uploadUnit = AppUtils.getUnit(sent);
        /*============================== Uploads ========================*/

        BigDecimal numberTotal = new BigDecimal(tot);
        BigDecimal decimalTotal = numberTotal.setScale(1, RoundingMode.HALF_EVEN);

        BigDecimal numberReceived = new BigDecimal(downloads);
        BigDecimal decimalReceived = numberReceived.setScale(1, RoundingMode.HALF_EVEN);

        BigDecimal numberSent = new BigDecimal(uploads);
        BigDecimal decimalSent = numberSent.setScale(1, RoundingMode.HALF_EVEN);

        String receivedText = decimalReceived.doubleValue() + " " + downloadsUnit;
        String sentText = decimalSent.doubleValue() + " " + uploadUnit;
        String totText = decimalTotal.doubleValue() + " " + unit;
        dailyDownloadsTv.setText(receivedText);
        dailyUploadsTv.setText(sentText);
        subDailyTotalTv.setText(totText);
        progressBarDaily.setVisibility(View.GONE);
        durationLayout.setVisibility(View.VISIBLE);

    }


    private void fetchUsageData() {
        if (getDate1() == null) {
            Snackbar.make(requireView(), getString(R.string.empty_start_time), Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (getDate2() == null) {
            Snackbar.make(requireView(), getString(R.string.empty_end_time), Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (AppUtils.getDifferenceInHours(startMillis, endMillis) < 1.5) {
            errorMsgTv.setText(R.string.diff_txt);
            errorMsgTv.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> errorMsgTv.setVisibility(View.GONE), 5000);
            return;
        }

        if (AppUtils.getDifferenceInHours(startMillis, endMillis) <= 2.5){
            startMillis = startMillis - Constants.getOneHourMillis();
        }

        Intent intent = new Intent(view.getContext(), WifiRangeActivity.class);
        intent.putExtra(Constants.START_TIME, startMillis);
        intent.putExtra(Constants.END_TIME, endMillis);
        intent.putExtra(WifiRangeActivity.EXTRA_DISPLAY_START_DATE, getDate1().getTime());
        intent.putExtra(WifiRangeActivity.EXTRA_DISPLAY_END_DATE, getDate2().getTime());
        startActivity(intent);

    }

    private void fetchWeeklyWifiUsage(StatisticsViewModel statisticsViewModel, NetworkStatsManager networkStatsManager, AppUtils appUtils, Context context){
        int firstDay = PreferenceUtils.getFirstDayOfWeek(view.getContext());
        long startDate, endDate;

        cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(firstDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        endDate = System.currentTimeMillis();

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        startDate = cal.getTimeInMillis();

        statisticsViewModel.fetchWifiStatistics(startDate, endDate, new NetStatsUtil(networkStatsManager), appUtils, context);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        long startTime;
        Calendar now = Calendar.getInstance();
        long endTime = now.getTimeInMillis();
        endTimeMillisToNextScreen = now.getTimeInMillis();
        if (position == 0){
            now.add(Calendar.HOUR, -4);
            startTime = now.getTimeInMillis();
            startTimeMillisToNextScreen = now.getTimeInMillis();

        }else if (position == 1){
            now.add(Calendar.HOUR, -8);
            startTime = now.getTimeInMillis();
            startTimeMillisToNextScreen = now.getTimeInMillis();

        }else if (position == 2){
            now.add(Calendar.HOUR, -12);
            startTime = now.getTimeInMillis();
            startTimeMillisToNextScreen = now.getTimeInMillis();

        }else if (position == 3){
            now.add(Calendar.HOUR, -23);
            startTime = now.getTimeInMillis();
            startTimeMillisToNextScreen = now.getTimeInMillis();

        }else {
            now.add(Calendar.HOUR, -24);
            startTime = now.getTimeInMillis();
            startTimeMillisToNextScreen = now.getTimeInMillis();
        }

        fetchAllSentAndReceivedBytesWifiByDuration(netStatsUtil, startTime, endTime);
        viewModel.fetchDailyWifiStatistics(startTime, endTime, netStatsUtil, utils, requireContext());
        PreferenceUtils.saveIsWifiDurationSelected(requireContext(), true);
        PreferenceUtils.saveWifiDurationSelectedPosition(requireContext(), position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getContext() != null){
            if (PreferenceUtils.isWifiDurationSelected(view.getContext())){
                int pos = PreferenceUtils.getWifiDurationSelectedPosition(view.getContext());
                durationSpinner.setSelection(pos, true);
                Calendar now = Calendar.getInstance();
                long endTime = now.getTimeInMillis();
                long startTime;
                if (pos == 0){
                    now.add(Calendar.HOUR, -4);
                }else if (pos == 1){
                    now.add(Calendar.HOUR, -8);
                }else if (pos == 2){
                    now.add(Calendar.HOUR, -12);
                }else {
                    now.add(Calendar.HOUR, -23);
                }
                startTime = now.getTimeInMillis();
                fetchAllSentAndReceivedBytesWifiByDuration(netStatsUtil, startTime, endTime);
                viewModel.fetchDailyWifiStatistics(startTime, endTime, netStatsUtil, utils, getContext());
                setDaysWeek();

            }

            String lang = PreferenceUtils.getLanguagePreference(getContext());
            setLocale(getActivity(), lang);

        }
    }

    private void setDaysWeek(){
        if (getContext() != null){
            long startDate, endDate;
            cal = Calendar.getInstance();
            cal.setFirstDayOfWeek(PreferenceUtils.getFirstDayOfWeek(getContext()));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            endDate = System.currentTimeMillis();
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            startDate = cal.getTimeInMillis();
            String displayDate = getString(R.string.data_used_this_week)+"\n("+sdf.format(new Date(startDate))+" - "+sdf.format(new Date(endDate))+")";
            weekDate.setText(displayDate);

            fetchAllSentAndReceivedBytesWifiWeekly(netStatsUtil, startDate, endDate);


        }
    }

    private void setLocale(Activity activity, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
    }

}
