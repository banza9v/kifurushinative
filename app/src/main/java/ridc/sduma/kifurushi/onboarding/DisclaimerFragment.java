package ridc.sduma.kifurushi.onboarding;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ridc.sduma.kifurushi.R;

public class DisclaimerFragment extends Fragment {

    OnNextClickedListener listener;

    public DisclaimerFragment() {
        // Required empty public constructor
    }

    public static DisclaimerFragment newInstance() {
        return new DisclaimerFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNextClickedListener)
            listener = (OnNextClickedListener) context;
        else
            throw new ClassCastException(context+"Must implement OnNextClickedListener");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_disclaimer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.disclaimerNext).setOnClickListener(v -> listener.onNext());
    }

    public interface OnNextClickedListener{
        void onNext();
    }
}