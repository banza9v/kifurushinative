package ridc.sduma.kifurushi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ridc.sduma.kifurushi.BuildConfig;
import ridc.sduma.kifurushi.Params;
import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.fragments.ComplaintsFragment;
import ridc.sduma.kifurushi.models.ComplaintDto;
import ridc.sduma.kifurushi.models.ComplaintResult;

public class FollowComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_complaint);
        Toolbar toolbar = findViewById(R.id.followToolbar);
        CardView complaintDetailsCard = findViewById(R.id.complaintDetailsCard);
        ProgressBar mProgressBar = findViewById(R.id.followProgress);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        if (getIntent().hasExtra(ComplaintsFragment.EXTRA_COMPLAINT_ID)){
            ActionBar actionBar = getSupportActionBar();
            String complaintTitle = getIntent().getStringExtra(ComplaintsFragment.EXTRA_TITLE);
            String complaintId = getIntent().getStringExtra(ComplaintsFragment.EXTRA_COMPLAINT_ID);
            String complaintType = getIntent().getStringExtra(ComplaintsFragment.EXTRA_COMPLAINT_TYPE);
            long complaintDate = getIntent().getLongExtra(ComplaintsFragment.EXTRA_COMPLAINT_DATE, 0);
            if (actionBar != null){
                actionBar.setTitle(R.string.following_complaint);
                actionBar.setSubtitle(complaintTitle);
            }
            fetchComplaintStatus(complaintId, complaintTitle, complaintType, complaintDate, mProgressBar, complaintDetailsCard);
        }

    }

    private void fetchComplaintStatus(String id, String title, String type, long date, ProgressBar progressBar, CardView cardView){
        String url = BuildConfig.FOLLOW_COMPLAINT_URL;
        ComplaintDto dto = new ComplaintDto(id);
        String reqBody = new Gson().toJson(dto);
        MediaType JSON = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(reqBody, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("ERR", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String jsonRes = Objects.requireNonNull(response.body()).string();
                if (response.isSuccessful()){
                    ComplaintResult result = new Gson().fromJson(jsonRes, ComplaintResult.class);
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        cardView.setVisibility(View.VISIBLE);
                        setupViews(result, title, type, date);
                    });
                }
            }
        });

    }

    private void setupViews(ComplaintResult result, String title, String type, long date) {
        TextView incidentTitle = findViewById(R.id.incidentTitle);
        TextView complaintDate = findViewById(R.id.complaintDate);
        TextView incidentType = findViewById(R.id.incidentType);
        TextView complaintMessage = findViewById(R.id.complaintMessage);

        incidentTitle.setText(title);
        incidentType.setText(type);
        complaintMessage.setText(result.getMessage());

        SimpleDateFormat sdf;
        if (AppUtils.getDifferenceInHours(date, System.currentTimeMillis()) > 23){
            sdf = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm", Locale.getDefault());
        }else {
            sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        String time = sdf.format(new Date(date));
        complaintDate.setText(time);

    }

}