package ridc.sduma.kifurushi.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Locale;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.permission.PermissionActivity;

public class OnBoardingActivity extends AppCompatActivity implements
        LanguageSelectionFragment.OnLanguageSelectedListener, DisclaimerFragment.OnNextClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceUtils.isOnBoardingShown(this)){
            Intent intent = new Intent(this, PermissionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_on_boarding);


        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null){
            setLocale(lang);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LanguageSelectionFragment())
                .commit();
    }

    @Override
    public void onLanguageSelected(String lang) {
        PreferenceUtils.saveLanguagePreference(this, lang);
        setLocale(lang);
        Intent intent = new Intent(this, PermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        PreferenceUtils.saveIsOnBoardingShown(true, this);
    }

    @Override
    public void onNext() {
        Intent intent = new Intent(this, PermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}