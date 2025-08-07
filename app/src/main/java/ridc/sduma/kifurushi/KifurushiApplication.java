package ridc.sduma.kifurushi;

import android.app.Application;
import android.content.res.Configuration;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.workers.WeeklyWifiWorkManager;

public class KifurushiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null){
            setLocale(lang);
        }
        /*PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(WeeklyWifiWorkManager.class, 13, TimeUnit.MINUTES)
                .addTag("Kifurushi_work_manager")
                .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Kifurushi_work_manager",
                ExistingPeriodicWorkPolicy.REPLACE, workRequest);*/
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}
