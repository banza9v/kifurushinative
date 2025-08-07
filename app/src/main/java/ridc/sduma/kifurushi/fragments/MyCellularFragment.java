package ridc.sduma.kifurushi.fragments;

import android.view.View;

import androidx.annotation.NonNull;

import ridc.sduma.kifurushi.databinding.FragmentCellurFragementBinding;

public class MyCellularFragment extends KifurushiBaseFragment<FragmentCellurFragementBinding>{
    @NonNull
    @Override
    public FragmentCellurFragementBinding getViews() {
        return FragmentCellurFragementBinding.inflate(getLayoutInflater());
    }

    @NonNull
    @Override
    public View getRootView() {
        return FragmentCellurFragementBinding.inflate(getLayoutInflater()).getRoot();
    }

    @Override
    public void doAppStaffs() {

    }
}
