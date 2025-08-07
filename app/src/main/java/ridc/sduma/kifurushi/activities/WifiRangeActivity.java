package ridc.sduma.kifurushi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ridc.sduma.kifurushi.Utils.Constants;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PackageUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.adapters.AppsAdapter;
import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.CustomComparator;

import com.ferfalk.simplesearchview.SimpleSearchView;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class WifiRangeActivity extends AppCompatActivity {

    public static final String EXTRA_DISPLAY_START_DATE = "wifiRangeActivity.EXTRA_DISPLAY_START_DATE";
    public static final String EXTRA_DISPLAY_END_DATE = "wifiRangeActivity.EXTRA_DISPLAY_END_DATE";

    TextView totalValue;
    TextView downloadsTv;
    TextView uploadTv;
    TextView durationDisplayTv;
    ProgressBar progressBar;
    ProgressBar loader;
    LinearLayout emptyApps;
    SearchView searchView;

    AppUtils appUtils;
    NetStatsUtil netStatsUtil;
    AppsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_range);

        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null) {
            setLocale(lang);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        totalValue = findViewById(R.id.totalBytes);
        downloadsTv = findViewById(R.id.downloads);
        uploadTv = findViewById(R.id.uploads);
        durationDisplayTv = findViewById(R.id.durationDisplay);
        progressBar = findViewById(R.id.weeklyCellularProgress);
        loader = findViewById(R.id.progressIndicator);
        emptyApps = findViewById(R.id.emptyApps);

        NetworkStatsManager manager = (NetworkStatsManager)getSystemService(Context.NETWORK_STATS_SERVICE);
        appUtils = new AppUtils();
        netStatsUtil = new NetStatsUtil(manager);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout coll_toolbar = findViewById(R.id.collapsing_toolbar_layout);
        coll_toolbar.setTitle("");
        coll_toolbar.setCollapsedTitleTextAppearance(R.style.MediumTextAppearanceBold);
        coll_toolbar.setExpandedTitleTextAppearance(R.style.MediumTextAppearanceBold);
        coll_toolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.primary));
        RecyclerView rv = findViewById(R.id.appsRv);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(layoutManager);

        NetworkStatsManager statsManager = (NetworkStatsManager) getSystemService(NETWORK_STATS_SERVICE);
        NetStatsUtil statsUtil = new NetStatsUtil(statsManager);


        if (getIntent().hasExtra(Constants.START_TIME) && getIntent().hasExtra(Constants.END_TIME)) {
            long startTime = getIntent().getLongExtra(Constants.START_TIME, 0);
            long endTime = getIntent().getLongExtra(Constants.END_TIME, 0);

            long displayStartDate = getIntent().getLongExtra(EXTRA_DISPLAY_START_DATE, 0);
            long displayEndDate = getIntent().getLongExtra(EXTRA_DISPLAY_END_DATE, 0);

            adapter = new AppsAdapter(netStatsUtil, this, ConnectivityManager.TYPE_WIFI, startTime, endTime);
            rv.setAdapter(adapter);

            SimpleDateFormat sdf;
            if (AppUtils.getDifferenceInHours(startTime, endTime) > 23) {
                sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm", new Locale(PreferenceUtils.getLanguagePreference(this)));
            } else {
                sdf = new SimpleDateFormat("HH:mm", new Locale(PreferenceUtils.getLanguagePreference(this)));
            }

            String displayDate = getString(R.string.data_used_from) + "\n" + sdf.format(new Date(displayStartDate)) + " - " + sdf.format(new Date(displayEndDate)) + ".";

            durationDisplayTv.setText(displayDate);


            Executors.newCachedThreadPool().execute(() -> {

                NetStatsUtil.UsageStats stats = statsUtil.getTotalBytesByWifi(WifiRangeActivity.this, startTime, endTime);

                List<AppDetails> listOfAppsAndTheirWifiUsage = appUtils.getListOfAppsAndTheirWifiUsage(WifiRangeActivity.this, statsUtil, startTime, endTime);

                runOnUiThread(() -> {
                    loader.setVisibility(View.GONE);
                    if (listOfAppsAndTheirWifiUsage.isEmpty()){
                        emptyApps.setVisibility(View.VISIBLE);
                        setupUi( stats);
                    }else {
                        emptyApps.setVisibility(View.GONE);
                        setupUi(stats);
                        Collections.sort(listOfAppsAndTheirWifiUsage, new CustomComparator().reversed());
                        adapter.setAppDetails(listOfAppsAndTheirWifiUsage, stats.getTotal());
                    }

                });

            });

        }


    }


    private void setupUi(NetStatsUtil.UsageStats stats) {
        /*========================================== Total ========================================*/

        double convertedTotal = AppUtils.bytesConverter(stats.getTotal());
        String totalUnit = AppUtils.getUnit(stats.getTotal());

        /*========================================== Total ========================================*/


        /*========================================== Downloads ========================================*/

        double convertedReceived = AppUtils.bytesConverter(stats.getDownloads());
        String rUnit = AppUtils.getUnit(stats.getDownloads());

        /*========================================== Downloads ========================================*/

        /*========================================== Uploads ========================================*/

        double convertedSent = AppUtils.bytesConverter(stats.getUploads());
        String tUnit = AppUtils.getUnit(stats.getUploads());

        /*========================================== Uploads ========================================*/

        String displayTotal = AppUtils.roundOffToAnySignificantFigure(1, convertedTotal) + " " + totalUnit;
        String displayDownloads = AppUtils.roundOffToAnySignificantFigure(1, convertedReceived) + " " + rUnit;
        String displayUploads = AppUtils.roundOffToAnySignificantFigure(1, convertedSent) + " " + tUnit;

        double progress = (stats.getDownloads() / (double) stats.getTotal()) * 100;

        progressBar.setProgress((int) progress);

        downloadsTv.setText(displayDownloads);
        uploadTv.setText(displayUploads);
        totalValue.setText(displayTotal);


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
        getMenuInflater().inflate(R.menu.search, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_menu)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_menu) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

}