package ridc.sduma.kifurushi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.fragments.DaysModalBottomSheet;
import ridc.sduma.kifurushi.fragments.LanguagesModalBottomSheet;

public class SettingsActivity extends AppCompatActivity implements
        DaysModalBottomSheet.OnRecordCompleteListener, LanguagesModalBottomSheet.OnLanguageChangedListener {

    DaysModalBottomSheet daysModalBottomSheet;
    LanguagesModalBottomSheet languagesModalBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbarSettings);
        TextView languageTv = findViewById(R.id.language_change);
        TextView startDayOfWeek = findViewById(R.id.startDayOfWeek);

        toolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        daysModalBottomSheet = new DaysModalBottomSheet();
        languagesModalBottomSheet = new LanguagesModalBottomSheet();

        languageTv.setOnClickListener(view -> languagesModalBottomSheet.show(getSupportFragmentManager(), LanguagesModalBottomSheet.LANGUAGE_TAG));
        findViewById(R.id.share_app).setOnClickListener(view -> shareApp(this));
        findViewById(R.id.rate_app).setOnClickListener(view -> rateApp(this));
        startDayOfWeek.setOnClickListener(view -> daysModalBottomSheet.show(getSupportFragmentManager(), DaysModalBottomSheet.DAYS_TAG));

    }


    private void restartApp(String lang){
        PreferenceUtils.saveLanguagePreference(this, lang);
        setLocale(lang);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void shareApp(Activity activity){
        ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setChooserTitle("Kifurushi App")
                .setText(getString(R.string.share_message)+"\n\nhttp://play.google.com/store/apps/details?id=" + activity.getPackageName())
                .startChooser();
    }

    private void rateApp(Activity activity){
        Uri uri = Uri.parse("market://details?id="+activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="+activity.getPackageName())));
        }
    }


    void saveSelectedDay(int day){
        PreferenceUtils.saveIsStartDayOfWeekChanged(this, true);
        PreferenceUtils.saveSelectedStartDayOfWeek(this, day);
    }

    @Override
    public void onDaySelected(int day) {
        saveSelectedDay(day);
        daysModalBottomSheet.dismiss();
        AppUtils.showInfoToast(this, getString(R.string.first_day_updated));
    }

    @Override
    public void onLanguageChanged(String lang) {
        restartApp(lang);
        languagesModalBottomSheet.dismiss();
        AppUtils.showInfoToast(this, getString(R.string.Language_changed_successfullly));
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}