package ridc.sduma.kifurushi.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ridc.sduma.kifurushi.R;

public class DaysModalBottomSheet extends BottomSheetDialogFragment {

    public static final String DAYS_TAG = "ridc.sduma.kifurushi.fragments.DaysModalBottomSheet.DAYS_TAG";
    private OnRecordCompleteListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRecordCompleteListener)
            listener = (OnRecordCompleteListener) context;
        else
            throw new ClassCastException(context+" Must implement OnRecordCompleteListener");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.days_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.sunday).setOnClickListener(v -> listener.onDaySelected(1));
        view.findViewById(R.id.monday).setOnClickListener(v -> listener.onDaySelected(2));
        view.findViewById(R.id.tuesday).setOnClickListener(v -> listener.onDaySelected(3));
        view.findViewById(R.id.wednesday).setOnClickListener(v -> listener.onDaySelected(4));
        view.findViewById(R.id.thursday).setOnClickListener(v -> listener.onDaySelected(5));
        view.findViewById(R.id.friday).setOnClickListener(v -> listener.onDaySelected(6));
        view.findViewById(R.id.saturday).setOnClickListener(v -> listener.onDaySelected(7));
    }



    public interface OnRecordCompleteListener{
        void onDaySelected(int day);
    }


}
