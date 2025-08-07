package ridc.sduma.kifurushi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ridc.sduma.kifurushi.R;

public class DescriptionModalBottomSheet extends BottomSheetDialogFragment {

    public static final String DESCRIPTION_TAG = "ridc.sduma.kifurushi.fragments.DescriptionModalBottomSheet.DESCRIPTION_TAG";
    private OnSendClickedListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSendClickedListener)
            listener = (OnSendClickedListener) context;
        else
            throw new ClassCastException(context+" Must implement OnRecordCompleteListener");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.desciption_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editText = view.findViewById(R.id.descriptionEt);

        view.findViewById(R.id.sendComplaint).setOnClickListener(v -> {
            String description = editText.getText().toString();
            if (TextUtils.isEmpty(description)){
                editText.setError(getString(R.string.required_descriptions));
                editText.requestFocus();
                return;
            }

            listener.onSendComplaint(description);

        });
    }



    public interface OnSendClickedListener{
        void onSendComplaint(String description);
    }


}
