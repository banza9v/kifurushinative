package ridc.sduma.kifurushi.permission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ridc.sduma.kifurushi.R;

public class ShowNotificationFragment extends Fragment {

    private static final int REQUEST_CODE = 4;
    private PermissionListener listener;


    public ShowNotificationFragment() {
        // Required empty public constructor
    }

    public static ShowNotificationFragment newInstance() {
        return new ShowNotificationFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PermissionListener)
            listener = (PermissionListener) context;
        else
            throw new ClassCastException(context + " Must implement PermissionListener");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_notification, container, false);
    }

    ActivityResultLauncher<String> notificationPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            listener.onShowNotificationClick();
        } else {
           /* if(NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {

            } else {
                notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }*/
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                createAlertDialog(getContext());
            } else {
                createAlertDialog(getContext());
            }
        }
    });


    private void createAlertDialog(Context context) {
        new AlertDialog.Builder(context)
                .setMessage("Show notification is required for this app to work. please go to settings and grant permission")
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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.nextBtn).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
            }else {
                listener.onShowNotificationClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (getContext() != null){
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    listener.onShowNotificationClick();
                }
            }
        }
    }

}