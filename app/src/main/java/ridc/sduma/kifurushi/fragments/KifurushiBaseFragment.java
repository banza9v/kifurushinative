package ridc.sduma.kifurushi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.snackbar.Snackbar;

import es.dmoral.toasty.Toasty;

public abstract class KifurushiBaseFragment<VB extends ViewBinding> extends Fragment {

    VB views;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        views = getViews();
        return views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doAppStaffs();
    }

    @NonNull
    public abstract VB getViews();

    @NonNull
    public abstract View getRootView();

    public abstract void doAppStaffs();

    public void showSnackBar(String message){
        Snackbar.make(getRootView(), message, Snackbar.LENGTH_SHORT).show();
    }

    public void showSuccessToastMessage(String message){
        Toasty.success(requireContext(), message, Toasty.LENGTH_SHORT).show();
    }

    public void showInfoToastMessage(String message){
        Toasty.info(requireContext(), message, Toasty.LENGTH_SHORT).show();
    }

    public void showWarnToastMessage(String message){
        Toasty.warning(requireContext(), message, Toasty.LENGTH_SHORT).show();
    }

    public void showErrorToastMessage(String message){
        Toasty.error(requireContext(), message, Toasty.LENGTH_SHORT).show();
    }

}
