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

public class LanguagesModalBottomSheet extends BottomSheetDialogFragment implements MediaPlayer.OnPreparedListener {

    public static final String LANGUAGE_TAG = "ridc.sduma.kifurushi.fragments.LanguagesModalBottomSheet.LANGUAGE_TAG";
    private OnLanguageChangedListener listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLanguageChangedListener)
            listener = (OnLanguageChangedListener) context;
        else
            throw new ClassCastException(context+" Must implement OnRecordCompleteListener");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.languages_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        view.findViewById(R.id.english).setOnClickListener(v -> listener.onLanguageChanged("en"));
        view.findViewById(R.id.swahili).setOnClickListener(v -> listener.onLanguageChanged("sw"));
    }




    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    public interface OnLanguageChangedListener{
        void onLanguageChanged(String lang);
    }


}
