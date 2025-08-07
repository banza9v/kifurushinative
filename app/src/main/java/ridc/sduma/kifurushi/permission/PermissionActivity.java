package ridc.sduma.kifurushi.permission;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Locale;
import java.util.Objects;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.activities.HomeActivity;

public class PermissionActivity extends AppCompatActivity implements PermissionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferenceUtils.allPermissionsAreGranted(this)) {
            Intent intent = new Intent(PermissionActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null) {
            setLocale(lang);
        }

        setContentView(R.layout.activity_permission_activty);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.permissions_container, new UsageAccessFragment())
                .commit();


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

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void slideToNextFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(Objects.requireNonNull(fm.findFragmentById(R.id.permissions_container)));
        transaction.setCustomAnimations(R.anim.enter_anim, R.anim.stay, R.anim.stay, R.anim.exit_anim);
        transaction.add(R.id.permissions_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onUsageAccessClick() {
        if (isAccessGranted()) {
            Fragment fragment = DrawOverFragment.newInstance();
            slideToNextFragment(fragment);
        }
    }

    @Override
    public void onPhoneStateClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            PreferenceUtils.savePermissionPrefs(this);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onDrawOverClick() {
        Fragment fragment;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            fragment = ShowNotificationFragment.newInstance();
        } else {
            fragment = PhoneStateFragment.newInstance();
        }
        slideToNextFragment(fragment);
    }

    @Override
    public void onShowNotificationClick() {
        Fragment fragment = PhoneStateFragment.newInstance();
        slideToNextFragment(fragment);
    }
}