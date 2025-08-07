package ridc.sduma.kifurushi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Locale;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceUtils.isSkipPressed(this)){
            Intent intent = new Intent(AuthActivity.this,HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null){
            setLocale(lang);
        }
        setContentView(R.layout.activity_auth);

        AppCompatButton appCompatButton = findViewById(R.id.btn_skip);
        AppCompatButton appCompatButton1 = findViewById(R.id.btn_sign);
        AppCompatButton appCompatButton2 = findViewById(R.id.btn_login);


        appCompatButton.setOnClickListener(
                view -> {
                    PreferenceUtils.saveIsSkipPressed(true, AuthActivity.this);
                    Intent intent = new Intent(AuthActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
        );

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}