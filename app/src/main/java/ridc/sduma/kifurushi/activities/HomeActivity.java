package ridc.sduma.kifurushi.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import kotlin.time.Duration;
import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Receiver.AlarmReceiver;
import ridc.sduma.kifurushi.Service.AppForegroundService;
import ridc.sduma.kifurushi.StatisticsViewModel;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.adapters.FragmentViewPagerAdapter;
import ridc.sduma.kifurushi.fragments.CellularFragment;
import ridc.sduma.kifurushi.fragments.ComplaintsFragment;
import ridc.sduma.kifurushi.fragments.ExtraFragment;
import ridc.sduma.kifurushi.fragments.MyCellularFragment;
import ridc.sduma.kifurushi.fragments.MyWifiFragment;
import ridc.sduma.kifurushi.fragments.ThresholdFragment;
import ridc.sduma.kifurushi.fragments.WiFiFragment;
import ridc.sduma.kifurushi.workers.WeeklyWifiWorkManager;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity implements ExtraFragment.OnExtrasCardClickedListener {

    StatisticsViewModel viewModel;
    NetworkStatsManager networkStatsManager;

    TabLayout tabLayout;
    ViewPager viewPager;

    Calendar cal;

    @Override
    protected void onRestart() {
        super.onRestart();
        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null){
            setLocale(lang);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null){
            setLocale(lang);
        }

        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        viewPager = findViewById(R.id.vp);
        tabLayout = findViewById(R.id.tabLayout);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Intent mIntent = new Intent(this, AlarmReceiver.class);
        mIntent.setAction("BackgroundProcess");

        //Set the repeated Task
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60, pendingIntent);

        if (isAccessGranted()){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                if (!foregroundServiceRunning()){
                    Intent intent = new Intent(this, AppForegroundService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        startForegroundService(intent);
                }
            }
        }

        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(StatisticsViewModel.class);
        networkStatsManager = (NetworkStatsManager)getSystemService(NETWORK_STATS_SERVICE);

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), FragmentViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new CellularFragment(), getString(R.string.cellular_data));
        adapter.addFragment(new WiFiFragment(), getString(R.string.wi_fi));
        adapter.addFragment(new ThresholdFragment(), getString(R.string.threshold));
        //adapter.addFragment(new ComplaintsFragment(), getString(R.string.complaints));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.tab_unselected_text_color),
                ContextCompat.getColor(this, R.color.tab_selected_text_color));
        tabLayout.setUnboundedRipple(true);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#F05454"));
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_baseline_signal_cellular_4_bar_24);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_baseline_wifi_24);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_access_time);
        //Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(R.drawable.ic_report_problem);

        fetchWeeklyShortCellularUsage(viewModel);
        fetchWeeklyShortWifiUsage(viewModel);

    }


    private void fetchWeeklyShortWifiUsage(StatisticsViewModel statisticsViewModel){
        int firstDay = PreferenceUtils.getFirstDayOfWeek(this);
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

        statisticsViewModel.fetchWifiStatistics(startDate, endDate, new NetStatsUtil(networkStatsManager), new AppUtils(), this);

    }

    private void fetchWeeklyShortCellularUsage(StatisticsViewModel statisticsViewModel){
        int firstDay = PreferenceUtils.getFirstDayOfWeek(this);
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

        statisticsViewModel.fetchCellularStatistics(startDate, endDate, new NetStatsUtil(networkStatsManager), new AppUtils(), this);

    }


    @Override
    public void onThreshold() {

    }

    @Override
    public void onReportIncident() {

    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager;
            appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode;
            mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public boolean foregroundServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (AppForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private final ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if (result.getResultCode() == 1)
                    recreate();

            });


    @Override
    protected void onResume() {
        fetchWeeklyShortWifiUsage(viewModel);
        fetchWeeklyShortCellularUsage(viewModel);
        super.onResume();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting_menu){
            resultLauncher.launch(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}