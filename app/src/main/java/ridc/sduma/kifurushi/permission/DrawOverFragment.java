package ridc.sduma.kifurushi.permission;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ridc.sduma.kifurushi.R;

public class DrawOverFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private PermissionListener listener;


    public DrawOverFragment() {
        // Required empty public constructor
    }

    public static DrawOverFragment newInstance() {
        return new DrawOverFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PermissionListener)
            listener = (PermissionListener) context;
        else
            throw new ClassCastException(context+" Must implement PermissionListener");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_draw_over, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.nextBtn).setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(view.getContext())){

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + view.getContext().getPackageName()));

                startActivityForResult(intent, REQUEST_CODE);
            }else {
                listener.onDrawOverClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(getContext())) {
                listener.onDrawOverClick();
            }
        }
    }

}