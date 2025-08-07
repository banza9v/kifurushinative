package ridc.sduma.kifurushi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import ridc.sduma.kifurushi.BuildConfig;
import ridc.sduma.kifurushi.Params;
import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.adapters.DistrictAdapter;
import ridc.sduma.kifurushi.adapters.RegionsAdapter;
import ridc.sduma.kifurushi.adapters.StreetAdapter;
import ridc.sduma.kifurushi.adapters.WardAdapter;
import ridc.sduma.kifurushi.db.HistoryViewModel;
import ridc.sduma.kifurushi.fragments.DescriptionModalBottomSheet;
import ridc.sduma.kifurushi.models.District;
import ridc.sduma.kifurushi.models.DistrictResponse;
import ridc.sduma.kifurushi.models.History;
import ridc.sduma.kifurushi.models.Region;
import ridc.sduma.kifurushi.models.Report;
import ridc.sduma.kifurushi.models.ReportResponse;
import ridc.sduma.kifurushi.models.Street;
import ridc.sduma.kifurushi.models.StreetResponse;
import ridc.sduma.kifurushi.models.TitleResponse;
import ridc.sduma.kifurushi.models.Titles;
import ridc.sduma.kifurushi.models.Ward;
import ridc.sduma.kifurushi.models.WardResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReportComplaintActivity extends AppCompatActivity implements DescriptionModalBottomSheet.OnSendClickedListener {

    CoordinatorLayout coordinatorLayout;
    EditText username;
    EditText phoneNumber;
    EditText complaintTicket;
    AppCompatSpinner regionTv;
    //TextView locationTv;
    AppCompatSpinner districtTv;
    AppCompatSpinner wardTv;
    AppCompatSpinner villageTv;
    AppCompatSpinner typeSpinner;
    AppCompatSpinner titleSpinner;
    Spinner mobileOppName;
    //ImageView microphone;
    ImageView messageIv;
    RadioGroup radioGroup;
    RadioButton yesRB, noRB;
    ProgressDialog mProgressDialog;

    List<String> titles = new ArrayList<>();
    List<String> regionNames = new ArrayList<>();
    HistoryViewModel viewModel;
    DescriptionModalBottomSheet descriptionModalBottomSheet;

/*======================================== User specific selections ===============================*/
    String type, title, mkoa, wilaya, kijiji, kata, selectedMno;
/*======================================== End of user selections ===============================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String lang = PreferenceUtils.getLanguagePreference(this);
        if (lang != null){
            setLocale(lang);
        }
        setContentView(R.layout.activity_report_complaint);

        username = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_number);
        typeSpinner = findViewById(R.id.aina_ya_taarifa);
        titleSpinner = findViewById(R.id.title);
        //locationTv = findViewById(R.id.location);
        regionTv = findViewById(R.id.mkoa);
        districtTv = findViewById(R.id.wilaya);
        wardTv = findViewById(R.id.kata);
        villageTv = findViewById(R.id.kijiji);
        //microphone = findViewById(R.id.recordAudio);
        messageIv = findViewById(R.id.writeMessage);
        coordinatorLayout = findViewById(R.id.mainView);
        mobileOppName = findViewById(R.id.mobileOppName);
        yesRB = findViewById(R.id.yesRb);
        noRB = findViewById(R.id.noRb);
        radioGroup = findViewById(R.id.radioGroup);
        complaintTicket = findViewById(R.id.complaintTicket);
        LinearLayout linearLayout = findViewById(R.id.detailsLayout);
        Toolbar toolbar = findViewById(R.id.extrasToolbar);

        toolbar.setTitle(R.string.report_incident);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        descriptionModalBottomSheet = new DescriptionModalBottomSheet();

        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(HistoryViewModel.class);



        messageIv.setOnClickListener(view -> {
            //showInputDialog();
            descriptionModalBottomSheet.show(getSupportFragmentManager(), DescriptionModalBottomSheet.DESCRIPTION_TAG);
        });


        List<String> types = new ArrayList<>();
        types.add(getString(R.string.complaints_));
        types.add(getString(R.string.congratulation));
        types.add(getString(R.string.inquiries));
        types.add(getString(R.string.sugestions));

        List<String> mnos = new ArrayList<>();
        mnos.add("Vodacom Tanzania");
        mnos.add("Tigo");
        mnos.add("Airtel");
        mnos.add("TTCL");
        mnos.add("Halotel");
        mnos.add("Zantel");

        ArrayAdapter<String> ad = new ArrayAdapter<>(this, R.layout.spinner_item, R.id.spinner_id, types);
        ArrayAdapter<String> mnoAdapter = new ArrayAdapter<>(this, R.layout.mno_layout_item, R.id.durationText, mnos);

        typeSpinner.setAdapter(ad);
        mobileOppName.setAdapter(mnoAdapter);
        mobileOppName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedMno = (String) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (radioGroup.getCheckedRadioButtonId() == -1){
                linearLayout.setVisibility(View.GONE);
            }
            if (radioGroup.getCheckedRadioButtonId() == R.id.noRb){
                linearLayout.setVisibility(View.GONE);
                showDisclaimerDialog();
            }

            if (radioGroup.getCheckedRadioButtonId() == R.id.yesRb){
                linearLayout.setVisibility(View.VISIBLE);
            }

        });


        fetchTitles(titleSpinner);
        fetchRegions(regionTv);


    }

    private void showDisclaimerDialog() {

        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(R.string.you_must)
                .setPositiveButton(getString(R.string.ok_), (dialogInterface, i) -> dialogInterface.dismiss())
                .create()
                .show();

    }

    private void submitComplaint(String msg) {

        String name =  username.getText().toString();
        String phone = phoneNumber.getText().toString();
        String ticket = complaintTicket.getText().toString();

        if (TextUtils.isEmpty(name)){
            username.setError(getString(R.string.user_name_required));
            username.requestFocus();
            return;
        }

        if (phone.length() < 10){
            phoneNumber.setError(getString(R.string.valid_phone_error));
            phoneNumber.requestFocus();
            return;
        }

        if (phone.length() > 13){
            phoneNumber.setError(getString(R.string.valid_phone_error));
            phoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ticket)){
            complaintTicket.setError(getString(R.string.ticket_required));
            complaintTicket.requestFocus();
            return;
        }

        if (ticket.length() <= 4){
            complaintTicket.setError(getString(R.string.valid_reference));
            complaintTicket.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)){
            phone = "";
        }

        if (phone.startsWith("0")) {
            StringBuilder builder = new StringBuilder(phone);
            builder.deleteCharAt(0);
            builder.insert(0, "255");
            phone = builder.toString();
        }

        if (phone.startsWith("+255")){
            StringBuilder builder = new StringBuilder(phone);
            builder.deleteCharAt(0);
            phone = builder.toString();
        }

        mProgressDialog.show();

        String titleToSend = title+" (Mtoa huduma "+selectedMno.toUpperCase(Locale.ROOT)+" & MNO reference number: "+ticket+")";

        String url = BuildConfig.ANONYMOUS_URL;

        Report report = new Report("970", "", titleToSend, msg, type, mkoa, wilaya, kata, kijiji, phone, "none", title, "True", "Tanzania", "Tanzania");
        String json = new Gson().toJson(report);
        MediaType JSON = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(json, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        Handler handler = new Handler(Looper.getMainLooper());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mProgressDialog.dismiss();
                handler.post(()-> AppUtils.showErrorToast(getApplicationContext(), getString(R.string.failed_to_send_complaint)));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resJson = Objects.requireNonNull(response.body()).string();
                handler.post(() -> {
                    mProgressDialog.dismiss();
                    if (response.isSuccessful()){
                        if (response.code() == 200){
                            ReportResponse reportResponse = new Gson().fromJson(resJson, ReportResponse.class);
                            History history = new History(title, type, reportResponse.getId(), System.currentTimeMillis());
                            saveHistory(history);
                            showSuccessDialog(reportResponse);
                        }else {
                            showFailureMassage();
                        }
                    }else {
                        showUnsuccessfulDialog();
                    }
                });
            }
        }));

    }

    private void showSuccessDialog(ReportResponse reportResponse) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getString(R.string.success))
                .setMessage(reportResponse.getMessage())
                .setPositiveButton(getString(R.string.ok_), (dialogInterface, i) -> dialogInterface.dismiss())
                .setOnCancelListener(dialogInterface -> onBackPressed())
                .create()
                .show();
    }

    private void showFailureMassage(){
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(getString(R.string.error))
                .setMessage(getString(R.string.failed_to_send_complaint))
                .setPositiveButton(getString(R.string.ok_), (dialogInterface, i) -> dialogInterface.dismiss())
                .create()
                .show();
    }

    private void showUnsuccessfulDialog(){
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.failed)
                .setMessage(R.string.unable_to_reach_server_error)
                .setPositiveButton(getString(R.string.ok_), (dialogInterface, i) -> dialogInterface.dismiss())
                .create()
                .show();
    }

    private void saveHistory(History history){
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> viewModel.insertHistory(history));

    }

    private void fetchTitles(AppCompatSpinner appCompatSpinner){

        String url = BuildConfig.BASE_URL+"get_titles_from_institution?institution_id=970";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        Executors.newSingleThreadExecutor().execute(() -> client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> AppUtils.showErrorToast(ReportComplaintActivity.this, e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resJson = Objects.requireNonNull(response.body()).string();
                TitleResponse titleResponse = new Gson().fromJson(resJson, TitleResponse.class);
                for (Titles title : titleResponse.getTitlesList())
                    titles.add(title.getClassTitle());

                runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ReportComplaintActivity.this, R.layout.spinner_item, R.id.spinner_id, titles);
                    appCompatSpinner.setAdapter(adapter);
                    appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            title = (String) adapterView.getSelectedItem();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                });
            }
        }));

    }

    private void fetchRegions(AppCompatSpinner appCompatSpinner){
        String url = BuildConfig.BASE_URL+"get_reginal_list";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        Executors.newCachedThreadPool().execute(() -> client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> AppUtils.showErrorToast(ReportComplaintActivity.this, e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resJson = Objects.requireNonNull(response.body()).string();
                Type listType = new TypeToken<ArrayList<Region>>(){}.getType();
                List<Region> regions = new Gson().fromJson(resJson, listType);
                for (Region region : regions)
                    regionNames.add(region.getRegionalName());

                runOnUiThread(() -> {
                    RegionsAdapter adapter = new RegionsAdapter(ReportComplaintActivity.this, R.layout.spinner_item, R.id.spinner_id, regions);
                    appCompatSpinner.setAdapter(adapter);
                    appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Region region = (Region) adapter.getItem(i);
                            if (region != null){
                                mkoa = region.getRegionalName();
                                try {
                                    fetchDistricts(districtTv, region);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                });
            }
        }));

    }

    private void fetchDistricts(AppCompatSpinner appCompatSpinner, Region region) throws JSONException {
        String url = BuildConfig.BASE_URL+"client_request_locations";

        JSONObject object = new JSONObject();
        object.put("location_id", region.getUniqueID());
        object.put("location_type", "wilaya");

        RequestBody body = RequestBody.create(object.toString(), MediaType.parse("application/json"));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        Executors.newCachedThreadPool().execute(() -> client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> AppUtils.showErrorToast(ReportComplaintActivity.this, e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resJson = Objects.requireNonNull(response.body()).string();
                DistrictResponse districtResponse = new Gson().fromJson(resJson, DistrictResponse.class);

                runOnUiThread(() -> {
                    DistrictAdapter adapter = new DistrictAdapter(ReportComplaintActivity.this, R.layout.spinner_item, R.id.spinner_id, districtResponse.getDistrictList());
                    appCompatSpinner.setAdapter(adapter);
                    appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            District item = adapter.getItem(i);
                            if (item != null){
                                wilaya = item.getDistrictName();
                                try {
                                    fetchWards(wardTv, item);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                });
            }
        }));

    }

    private void fetchWards(AppCompatSpinner appCompatSpinner, District region) throws JSONException {
        String url = BuildConfig.BASE_URL+"client_request_locations";

        JSONObject object = new JSONObject();
        object.put("location_id", region.getDistrictUniqueID());
        object.put("location_type", "kata");

        RequestBody body = RequestBody.create(object.toString(), MediaType.parse("application/json"));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        Executors.newCachedThreadPool().execute(() -> client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> AppUtils.showErrorToast(ReportComplaintActivity.this, e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resJson = Objects.requireNonNull(response.body()).string();

                WardResponse wardResponse = new Gson().fromJson(resJson, WardResponse.class);

                runOnUiThread(() -> {
                    WardAdapter adapter = new WardAdapter(ReportComplaintActivity.this, R.layout.spinner_item, R.id.spinner_id, wardResponse.getWardList());
                    appCompatSpinner.setAdapter(adapter);
                    appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            Ward item = adapter.getItem(position);
                            if (item != null){
                                kata = item.getWardName();
                                try {
                                    fetchStreets(villageTv, item);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                });
            }
        }));

    }

    private void fetchStreets(AppCompatSpinner appCompatSpinner, Ward ward) throws JSONException {
        String url = BuildConfig.BASE_URL +"client_request_locations";

        JSONObject object = new JSONObject();
        object.put("location_id", ward.getWardUniqueID());
        object.put("location_type", "mtaa");

        RequestBody body = RequestBody.create(object.toString(), MediaType.parse("application/json"));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        Executors.newCachedThreadPool().execute(() -> client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> AppUtils.showErrorToast(ReportComplaintActivity.this, e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String resJson = Objects.requireNonNull(response.body()).string();

                StreetResponse streetResponse = new Gson().fromJson(resJson, StreetResponse.class);


                runOnUiThread(() -> {
                    StreetAdapter adapter = new StreetAdapter(ReportComplaintActivity.this, R.layout.spinner_item, R.id.spinner_id, streetResponse.getStreetList());
                    appCompatSpinner.setAdapter(adapter);
                    appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Street item = adapter.getItem(i);
                            if (item != null){
                                kijiji = item.getStreetName();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                });
            }
        }));

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onSendComplaint(String description) {
        submitComplaint(description);
        descriptionModalBottomSheet.dismiss();
    }


}