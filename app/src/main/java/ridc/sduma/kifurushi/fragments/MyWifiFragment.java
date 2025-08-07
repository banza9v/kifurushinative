package ridc.sduma.kifurushi.fragments;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.util.Collections;

import ridc.sduma.kifurushi.StatisticsViewModel;
import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.CustomComparator;
import ridc.sduma.kifurushi.databinding.FragmentWifiBinding;

public class MyWifiFragment extends KifurushiBaseFragment<FragmentWifiBinding>{

    StatisticsViewModel viewModel;

    @NonNull
    @Override
    public FragmentWifiBinding getViews() {
        return FragmentWifiBinding.inflate(getLayoutInflater());
    }

    @NonNull
    @Override
    public View getRootView() {
        return FragmentWifiBinding.inflate(getLayoutInflater()).getRoot();
    }

    @Override
    public void doAppStaffs() {
        viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);

        viewModel.getWifiUsage().observe(getViewLifecycleOwner(), appDetails -> {
            Collections.sort(appDetails, new CustomComparator().reversed());
            for (AppDetails details : appDetails){
                Log.d("APP_DEATILS", "AppName ==> "+details.getAppName()+", Total usage: "+details.getTotalBytes());
            }
        });

    }


}
