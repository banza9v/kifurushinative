package ridc.sduma.kifurushi.permission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppUtils;

public class PhoneStateFragment extends Fragment {

    private static final int REQUEST_CODE = 3;
    private PermissionListener listener;

    public PhoneStateFragment() {
        // Required empty public constructor
    }

    public static PhoneStateFragment newInstance() {
        return new PhoneStateFragment();
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
        return inflater.inflate(R.layout.fragment_phone_state, container, false);
    }

    ActivityResultLauncher<String> permissionRequestLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            listener.onPhoneStateClick();
        }
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)){
                createAlertDialog(getContext());
            }else {
                createAlertDialog(getContext());
            }
        }
    });

    private void createAlertDialog(Context context) {
        new AlertDialog.Builder(context)
                .setMessage("Read phone state is required for this app to work. please go to settings and grant permission")
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(getString(R.string.settings), (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_CODE);
                })
                .create()
                .show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.nextBtn).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                permissionRequestLauncher.launch(Manifest.permission.READ_PHONE_STATE);
            }else {
                listener.onPhoneStateClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (getContext() != null){
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    listener.onPhoneStateClick();
                }
            }
        }
    }
}