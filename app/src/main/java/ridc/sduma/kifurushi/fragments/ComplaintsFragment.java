package ridc.sduma.kifurushi.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Locale;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.activities.FollowComplaintActivity;
import ridc.sduma.kifurushi.activities.ReportComplaintActivity;
import ridc.sduma.kifurushi.adapters.HistoryAdapter;
import ridc.sduma.kifurushi.db.HistoryViewModel;
import ridc.sduma.kifurushi.models.History;

public class ComplaintsFragment extends Fragment implements HistoryAdapter.OnHistoryClickListener {

    public static final String EXTRA_TITLE = "ridc.sduma.kifurushi.fragments.EXTRA_TITLE";
    public static final String EXTRA_COMPLAINT_ID = "ridc.sduma.kifurushi.fragments.EXTRA_COMPLAINT_ID";
    public static final String EXTRA_COMPLAINT_DATE = "ridc.sduma.kifurushi.fragments.EXTRA_COMPLAINT_DATE";
    public static final String EXTRA_COMPLAINT_TYPE = "ridc.sduma.kifurushi.fragments.EXTRA_COMPLAINT_TYPE";

    HistoryViewModel viewModel;

    public ComplaintsFragment() {
        // Required empty public constructor
    }

    public static ComplaintsFragment newInstance() {
        return new ComplaintsFragment();
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
        View view = inflater.inflate(R.layout.fragment_complaints, container, false);
        setHasOptionsMenu(true);
        RecyclerView recyclerView = view.findViewById(R.id.historyRv);
        LinearLayout emptyHistoryMessage = view.findViewById(R.id.emptyHistoryMessage);

        HistoryAdapter adapter = new HistoryAdapter(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        if (getActivity() != null){

            viewModel = new ViewModelProvider(getViewModelStore(), (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                    .get(HistoryViewModel.class);
        }

        viewModel.getAllHistory().observe(getViewLifecycleOwner(), histories -> {
            if (histories.isEmpty()){
                emptyHistoryMessage.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                emptyHistoryMessage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setHistoryList(histories);
            }
        });

        view.findViewById(R.id.addThreshold).setOnClickListener(v -> startActivity(new Intent(getContext(), ReportComplaintActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getContext() != null){
            String lang = PreferenceUtils.getLanguagePreference(getContext());
            setLocale(getActivity(), lang);
        }
    }

    @Override
    public void onHistoryClick(History history) {
        Intent intent = new Intent(getContext(), FollowComplaintActivity.class);
        intent.putExtra(EXTRA_TITLE, history.getComplaintTitle());
        intent.putExtra(EXTRA_COMPLAINT_ID, history.getComplaintId());
        intent.putExtra(EXTRA_COMPLAINT_DATE, history.getComplaint_date());
        intent.putExtra(EXTRA_COMPLAINT_TYPE, history.getComplaintType());
        startActivity(intent);
    }


    private void setLocale(Activity activity, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
    }

}