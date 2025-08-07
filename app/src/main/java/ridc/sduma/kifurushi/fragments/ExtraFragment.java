package ridc.sduma.kifurushi.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ridc.sduma.kifurushi.R;


public class ExtraFragment extends Fragment {

    private OnExtrasCardClickedListener listener;

    public ExtraFragment() {
        // Required empty public constructor
    }

    public static ExtraFragment newInstance() {

        return new ExtraFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnExtrasCardClickedListener)
            listener = (OnExtrasCardClickedListener) context;
        else
            throw new ClassCastException(context.toString()+" Must implement OnExtrasCardClickedListener");
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
        View view =  inflater.inflate(R.layout.fragment_extra, container, false);
        Toolbar toolbar = view.findViewById(R.id.extrasToolbar);
        CardView threshold = view.findViewById(R.id.thresholdCard);
        CardView report = view.findViewById(R.id.reportCard);

        toolbar.setTitle("Extras");
        if (getActivity() != null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        }

        threshold.setOnClickListener(v -> listener.onThreshold());
        report.setOnClickListener(v -> listener.onReportIncident());


        // Inflate the layout for this fragment
        return view;
    }


    public interface OnExtrasCardClickedListener{
        void onThreshold();
        void onReportIncident();
    }



}