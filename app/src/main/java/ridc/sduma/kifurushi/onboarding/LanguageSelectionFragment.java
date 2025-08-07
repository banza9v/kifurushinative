package ridc.sduma.kifurushi.onboarding;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ridc.sduma.kifurushi.R;

public class LanguageSelectionFragment extends Fragment {

    OnLanguageSelectedListener listener;

    public LanguageSelectionFragment() {
        // Required empty public constructor
    }

    public static LanguageSelectionFragment newInstance() {
        return new LanguageSelectionFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLanguageSelectedListener)
            listener = (OnLanguageSelectedListener) context;
        else
            throw new ClassCastException(context +" Must implement OnLanguageSelectedListener.onLanguageSelected(String lang)");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout eng = view.findViewById(R.id.englishSelection);
        LinearLayout sw = view.findViewById(R.id.swahiliSelection);

        sw.setOnClickListener(view1 -> listener.onLanguageSelected("sw"));
        eng.setOnClickListener(view1 -> listener.onLanguageSelected("en"));

    }

    public interface OnLanguageSelectedListener{
        void onLanguageSelected(String lang);
    }
}