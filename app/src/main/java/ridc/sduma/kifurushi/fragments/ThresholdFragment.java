package ridc.sduma.kifurushi.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.ThresholdAppsAdapter;
import ridc.sduma.kifurushi.ThresholdViewModel;
import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.Locale;


public class ThresholdFragment extends Fragment {


    CoordinatorLayout coordinatorLayout;
    TextView cellularUsage, wifiUsage;
    ImageView mAddCellular, mAddWifi, mEditCellular, mEditWifi;

    public ThresholdFragment() {
        // Required empty public constructor
    }

    public static ThresholdFragment newInstance() {
        return new ThresholdFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_threshold, container, false);
        setHasOptionsMenu(true);

        coordinatorLayout = view.findViewById(R.id.viewMain);
        cellularUsage = view.findViewById(R.id.cellularUsedAmount);
        wifiUsage = view.findViewById(R.id.wifiUsedAmount);
        mAddCellular = view.findViewById(R.id.cellular_threshold);
        mAddWifi = view.findViewById(R.id.wifi_threshold);
        mEditCellular = view.findViewById(R.id.editCellularThreshold);
        mEditWifi = view.findViewById(R.id.editWifiThreshold);


        getThresholdUsage(view.getContext());
        getWifiThresholdUsage(view.getContext());

        mAddCellular.setOnClickListener(v -> createInputDialog(v.getContext()));
        mAddWifi.setOnClickListener(v -> createWifiInputDialog(v.getContext()));
        mEditWifi.setOnClickListener(v -> createUpdateWifiThresholdValue(view.getContext()));
        mEditCellular.setOnClickListener(v -> createUpdateCellularThresholdValue(view.getContext()));

        setupButtonVisibility(view.getContext());

        return view;
    }


    private void createInputDialog(Context context) {

        Dialog dialogBuilder = new Dialog(context);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_layout, null);
        EditText editText = (EditText)view.findViewById(R.id.thresholdInput);
        TextView message = (TextView)view.findViewById(R.id.customThresholdMessage);
        TextView title = (TextView)view.findViewById(R.id.customThresholdTitle);
        AppCompatButton cancelBtn = (AppCompatButton)view.findViewById(R.id.cancelBtn);
        AppCompatButton confirmBtn = (AppCompatButton)view.findViewById(R.id.confirmBtn);
        dialogBuilder.setContentView(view);
        message.setText(R.string.cellular_threshold_dialog_message);
        title.setText(R.string.cellular_threshold);

        cancelBtn.setOnClickListener(v-> dialogBuilder.dismiss());
        confirmBtn.setOnClickListener(v -> {
            String inputValue = editText.getText().toString().trim();
            try {
                double val = Double.parseDouble(inputValue);
                saveThresholdValue(val, dialogBuilder, context);
            } catch (NumberFormatException e) {
                Snackbar.make(coordinatorLayout, R.string.enter_valid_number, Snackbar.LENGTH_LONG).show();
            }
        });

        dialogBuilder.show();

    }

    private void createWifiInputDialog(Context context) {

        Dialog dialogBuilder = new Dialog(context);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_layout, null);
        EditText editText = (EditText)view.findViewById(R.id.thresholdInput);
        TextView message = (TextView)view.findViewById(R.id.customThresholdMessage);
        TextView title = (TextView)view.findViewById(R.id.customThresholdTitle);
        AppCompatButton cancelBtn = (AppCompatButton)view.findViewById(R.id.cancelBtn);
        AppCompatButton confirmBtn = (AppCompatButton)view.findViewById(R.id.confirmBtn);
        dialogBuilder.setContentView(view);
        message.setText(R.string.wifi_threshold_dialog_message);
        title.setText(R.string.wi_fi_threshold);

        cancelBtn.setOnClickListener(v -> dialogBuilder.dismiss());
        confirmBtn.setOnClickListener(v -> {
            String inputValue = editText.getText().toString().trim();
            try {
                double val = Double.parseDouble(inputValue);
                saveWifiThresholdValue(val, dialogBuilder, context);
            } catch (NumberFormatException e) {
                Snackbar.make(coordinatorLayout, R.string.enter_valid_number, Snackbar.LENGTH_LONG).show();
            }
        });

        dialogBuilder.show();
    }

    private void saveThresholdValue(double val, Dialog dialogInterface, Context context) {
        dialogInterface.dismiss();

        long startTime = System.currentTimeMillis();
        long bytesTobeUsed = (long) (val * 1024 * 1024);
        if (val == 0) {
            Snackbar.make(coordinatorLayout, R.string.zero_cannot_be_used, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (val > (1024 * 1024 * 1024)) {
            Snackbar.make(coordinatorLayout, R.string.too_large_threshold, Snackbar.LENGTH_SHORT).show();
            return;
        }

        PreferenceUtils.saveThresholdStartTimePref(startTime, context);
        PreferenceUtils.saveThresholdValuePref(bytesTobeUsed, context);
        PreferenceUtils.saveThresholdStatus(true, context);
        Snackbar.make(coordinatorLayout, R.string.threshold_success, Snackbar.LENGTH_SHORT).show();
        mAddCellular.setVisibility(View.GONE);
        getThresholdUsage(context);


    }

    private void saveWifiThresholdValue(double val, Dialog dialogInterface, Context context) {
        dialogInterface.dismiss();

        long startTime = System.currentTimeMillis();
        long bytesTobeUsed = (long) (val * 1024 * 1024);
        if (val == 0) {
            Snackbar.make(coordinatorLayout, R.string.zero_cannot_be_used, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (val > (1024 * 1024 * 1024)) {
            Snackbar.make(coordinatorLayout, R.string.too_large_threshold, Snackbar.LENGTH_SHORT).show();
            return;
        }

        PreferenceUtils.saveWifiThresholdStartTimePref(startTime, context);
        PreferenceUtils.saveWifiThresholdValuePref(bytesTobeUsed, context);
        PreferenceUtils.saveWifiThresholdStatus(true, context);
        Snackbar.make(coordinatorLayout, R.string.threshold_success, Snackbar.LENGTH_SHORT).show();
        mAddWifi.setVisibility(View.GONE);
        getWifiThresholdUsage(context);


    }

    private void getThresholdUsage(Context context) {
        setupButtonVisibility(context);
        if (PreferenceUtils.isThresholdValueSet(context)) {
            //long startTimePref = PreferenceUtils.getThresholdStartTimePref(context);
            long valueStored = PreferenceUtils.getThresholdValuePref(context);

            double threshold = AppUtils.bytesConverter(valueStored);
            //double usedVolume = AppUtils.bytesConverter(total);
            String storedUnit = AppUtils.getUnit(valueStored);
            //String totalUnit = AppUtils.getUnit(total);

            String progress = threshold + " " + storedUnit;

            cellularUsage.setText(progress);

        }

    }

    private void getWifiThresholdUsage(Context context) {
        setupButtonVisibility(context);
        if (PreferenceUtils.isWifiThresholdValueSet(context)) {
            //long startTimePref = PreferenceUtils.getWifiThresholdStartTimePref(context);
            long valueStored = PreferenceUtils.getWifiThresholdValuePref(context);
            /*NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
            NetStatsUtil statsUtil = new NetStatsUtil(networkStatsManager);

            long received = statsUtil.getAllRxBytesWifi(startTimePref, System.currentTimeMillis());
            long sent = statsUtil.getAllTxBytesWifi(startTimePref, System.currentTimeMillis());
            long total = received + sent;

            double percent = (total / (double) valueStored) * 100;

            BigDecimal number = new BigDecimal(percent);
            BigDecimal decimal = number.setScale(1, BigDecimal.ROUND_HALF_EVEN);*/

            double threshold = AppUtils.bytesConverter(valueStored);
            //double usedVolume = AppUtils.bytesConverter(total);
            String storedUnit = AppUtils.getUnit(valueStored);
            //String totalUnit = AppUtils.getUnit(total);

            String progress = threshold + " " + storedUnit;

            wifiUsage.setText(progress);

        }

    }

    private void setupButtonVisibility(Context context){
        if (PreferenceUtils.isThresholdValueSet(context)){
            mAddCellular.setVisibility(View.GONE);
            mEditCellular.setVisibility(View.VISIBLE);
        }else {
            mAddCellular.setVisibility(View.VISIBLE);
            mEditCellular.setVisibility(View.GONE);
        }

        if (PreferenceUtils.isWifiThresholdValueSet(context)){
            mAddWifi.setVisibility(View.GONE);
            mEditWifi.setVisibility(View.VISIBLE);
        }else {
            mAddWifi.setVisibility(View.VISIBLE);
            mEditWifi.setVisibility(View.GONE);
        }
    }

    private void createUpdateCellularThresholdValue(Context context){

        Dialog dialog = new Dialog(context);
        LayoutInflater inflater = getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_layout, null);
        EditText editText = (EditText)view.findViewById(R.id.thresholdInput);
        TextView message = (TextView)view.findViewById(R.id.customThresholdMessage);
        TextView title = (TextView)view.findViewById(R.id.customThresholdTitle);
        AppCompatButton cancelBtn = (AppCompatButton)view.findViewById(R.id.cancelBtn);
        AppCompatButton confirmBtn = (AppCompatButton)view.findViewById(R.id.confirmBtn);
        dialog.setContentView(view);
        message.setText(R.string.cellular_threshold_dialog_message);
        title.setText(R.string.cellular_threshold);

        cancelBtn.setOnClickListener(v-> dialog.dismiss());
        confirmBtn.setOnClickListener(v -> {
            String inputValue = editText.getText().toString().trim();
            try {
                double val = Double.parseDouble(inputValue);
                updateCellularThresholdValue(val, dialog, context);
            } catch (NumberFormatException e) {
                Snackbar.make(coordinatorLayout, R.string.enter_valid_number, Snackbar.LENGTH_LONG).show();
            }
        });

        dialog.show();


    }

    private void createUpdateWifiThresholdValue(Context context){

        Dialog dialog = new Dialog(context);
        LayoutInflater inflater = getLayoutInflater();

        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_layout, null);
        EditText editText = (EditText)view.findViewById(R.id.thresholdInput);
        TextView message = (TextView)view.findViewById(R.id.customThresholdMessage);
        TextView title = (TextView)view.findViewById(R.id.customThresholdTitle);
        AppCompatButton cancelBtn = (AppCompatButton)view.findViewById(R.id.cancelBtn);
        AppCompatButton confirmBtn = (AppCompatButton)view.findViewById(R.id.confirmBtn);
        dialog.setContentView(view);
        message.setText(R.string.cellular_threshold_dialog_message);
        title.setText(R.string.cellular_threshold);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        confirmBtn.setOnClickListener(v -> {
            String input = editText.getText().toString();
            try {
                double val = Double.parseDouble(input);
                updateWifiThresholdValue(val, dialog, getContext());
            }catch (NumberFormatException e){
                AppUtils.showInfoToast(getContext(), getString(R.string.enter_valid_number));
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void updateCellularThresholdValue(double val, Dialog dialog, Context context) {
        dialog.dismiss();
        long bytesTobeUsed = (long) (val * 1024 * 1024);
        if (val == 0) {
            Snackbar.make(coordinatorLayout, R.string.zero_cannot_be_used, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (val > (1024 * 1024 * 1024)) {
            Snackbar.make(coordinatorLayout, R.string.too_large_threshold, Snackbar.LENGTH_SHORT).show();
            return;
        }

        PreferenceUtils.saveThresholdValuePref(bytesTobeUsed, context);
        PreferenceUtils.saveThresholdStatus(true, context);
        Snackbar.make(coordinatorLayout, R.string.threshold_success, Snackbar.LENGTH_SHORT).show();
        mAddCellular.setVisibility(View.GONE);
        getThresholdUsage(context);

    }

    private void updateWifiThresholdValue(double val, Dialog dialogInterface, Context context) {
        dialogInterface.dismiss();
        long bytesTobeUsed = (long) (val * 1024 * 1024);
        if (val == 0) {
            Snackbar.make(coordinatorLayout, R.string.zero_cannot_be_used, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (val > (1024 * 1024 * 1024)) {
            Snackbar.make(coordinatorLayout, R.string.too_large_threshold, Snackbar.LENGTH_SHORT).show();
            return;
        }

        PreferenceUtils.saveWifiThresholdValuePref(bytesTobeUsed, context);
        PreferenceUtils.saveWifiThresholdStatus(true, context);
        Snackbar.make(coordinatorLayout, R.string.threshold_success, Snackbar.LENGTH_SHORT).show();
        mAddWifi.setVisibility(View.GONE);
        getWifiThresholdUsage(context);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getContext() != null){
            getThresholdUsage(getContext());
            getWifiThresholdUsage(getContext());
            String lang = PreferenceUtils.getLanguagePreference(getContext());
            setLocale(getActivity(), lang);
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