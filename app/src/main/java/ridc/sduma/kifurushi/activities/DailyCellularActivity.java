package ridc.sduma.kifurushi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ridc.sduma.kifurushi.Params;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.adapters.AppsAdapter;
import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.StatisticsViewModel;
import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.CustomComparator;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyCellularActivity extends AppCompatActivity {

    public static final String EXTRA_START_TIME_MILLIS = "dailyCellularActivity.EXTRA_START_TIME_MILLIS";
    public static final String EXTRA_END_TIME_MILLIS = "dailyCellularActivity.EXTRA_END_TIME_MILLIS";
    public static final String EXTRA_RxBYTES = "dailyCellularActivity.EXTRA_RxBYTES";
    public static final String EXTRA_TxBYTES = "dailyCellularActivity.EXTRA_TxBYTES";
    public static final String EXTRA_TOTAL_BYTES = "dailyCellularActivity.EXTRA_TOTAL_BYTES";

    TextView totalValue;
    TextView downloadsTv;
    TextView uploadTv;
    TextView durationDisplayTv;
    ProgressBar progressBar;
    ProgressBar loader;
    LinearLayout emptyApps;
    SearchView searchView;

    AppUtils appUtils;
    AppsAdapter adapter;
    NetStatsUtil netStatsUtil;
    NetworkStatsManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_cellular);

        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null){
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

        appUtils = new AppUtils();
        manager = (NetworkStatsManager)getSystemService(Context.NETWORK_STATS_SERVICE);
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


        StatisticsViewModel viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(StatisticsViewModel.class);

        long startTime = getIntent().getLongExtra(EXTRA_START_TIME_MILLIS, 0);
        long endTime = getIntent().getLongExtra(EXTRA_END_TIME_MILLIS, 0);
        long rxBytes = getIntent().getLongExtra(EXTRA_RxBYTES, 0);
        long txBytes = getIntent().getLongExtra(EXTRA_TxBYTES, 0);
        long totalBytes = getIntent().getLongExtra(EXTRA_TOTAL_BYTES, 0);

        adapter = new AppsAdapter(netStatsUtil, this, ConnectivityManager.TYPE_MOBILE, startTime, endTime);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        viewModel.getDailyCellularUsage().observe(this, appDetails -> {
            loader.setVisibility(View.GONE);
            if (appDetails.isEmpty()){
                emptyApps.setVisibility(View.VISIBLE);
                setupUi(0, 0, 0);
            }else {
                emptyApps.setVisibility(View.GONE);
                setupUi(rxBytes, txBytes, totalBytes);
                long total = appUtils.getTotalBytes(appDetails);
                Collections.sort(appDetails, new CustomComparator().reversed());
                adapter.setAppDetails(appDetails, total);
            }
        });

        SimpleDateFormat sdf;
        if (AppUtils.getDifferenceInHours(startTime, endTime) > 23) {
            sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm", new Locale(PreferenceUtils.getLanguagePreference(this)));
        } else {
            sdf = new SimpleDateFormat("HH:mm", new Locale(PreferenceUtils.getLanguagePreference(this)));
        }
        String displayDate = getString(R.string.data_used_from)+"\n"+sdf.format(new Date(startTime))+" - "+sdf.format(new Date(endTime))+".";

        durationDisplayTv.setText(displayDate);

    }


    private void setupUi(long received, long sent, long total){

        /*========================================== Total ========================================*/
        double convertedTotal = AppUtils.bytesConverter(total);
        String totalUnit = AppUtils.getUnit(total);

        /*========================================== Total ========================================*/


        /*========================================== Downloads ========================================*/
        double convertedReceived = AppUtils.bytesConverter(received);
        String rUnit = AppUtils.getUnit(received);

        /*========================================== Downloads ========================================*/

        /*========================================== Uploads ========================================*/
        double convertedSent = AppUtils.bytesConverter(sent);
        String tUnit = AppUtils.getUnit(sent);

        /*========================================== Uploads ========================================*/


        /*=======================================Icons====================================================*/

        /*=======================================Icons====================================================*/

        String displayTotal = AppUtils.roundOffToAnySignificantFigure(1, convertedTotal)+" "+totalUnit;
        String downloads = AppUtils.roundOffToAnySignificantFigure(1, convertedReceived)+" "+rUnit;
        String uploads = AppUtils.roundOffToAnySignificantFigure(1, convertedSent)+" "+tUnit;

        double progress = (received/(double)total)*100;

        progressBar.setProgress((int) progress);

        downloadsTv.setText(downloads);
        uploadTv.setText(uploads);
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