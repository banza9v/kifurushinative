package ridc.sduma.kifurushi.Service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;

import com.google.android.material.snackbar.Snackbar;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.activities.HomeActivity;

public class ThresholdService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingView;
    private String thresholdType = "";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thresholdType = intent.getStringExtra("THRESHOLD_TYPE");
        if (Settings.canDrawOverlays(ThresholdService.this)) {
            setupFloatingView(thresholdType);
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleNotification(){
        String CHANNEL_ID = "com.sduma.ThresholdService.CHANNEL_ID";
        String CHANNEL_NAME = "Threshold_channel";

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        RemoteViews layout = new RemoteViews(getPackageName(), R.layout.custom_threshold_notification_layout);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            builder.setCustomContentView(layout)
                    .setCustomBigContentView(layout)
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .build();
        }

        startForeground(101010, builder.build());

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @SuppressLint({"InflateParams", "RtlHardcoded"})
    private void setupFloatingView(String thresholdType){
        int LAYOUT_FLAG;
        LayoutInflater mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        mFloatingView = mInflater.inflate(R.layout.floating_threshold_item, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        else
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        //params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.gravity = Gravity.CENTER;
        //params.x = 0;
        //params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        TextView thresholdTitle = mFloatingView.findViewById(R.id.thresholdTitle);
        thresholdTitle.setText(getString(R.string.maximun_value_reached));


        //Set the view while floating view is expanded.
        //Set the play button.
        TextView btnClose = mFloatingView.findViewById(R.id.close);
        btnClose.setOnClickListener(v -> {
            if (thresholdType.equals("WIFI")){
                PreferenceUtils.saveWifiThresholdStatus(false, ThresholdService.this);
                PreferenceUtils.clearThresholdStartTime(ThresholdService.this);
                PreferenceUtils.clearWifiThresholdValuePrefs(ThresholdService.this);
            }
            if (thresholdType.equals("CELLULAR")){
                PreferenceUtils.saveThresholdStatus(false, ThresholdService.this);
                PreferenceUtils.clearThresholdStartTime(ThresholdService.this);
                PreferenceUtils.clearWifiThresholdValuePrefs(ThresholdService.this);
            }
            stopSelf();
        });


        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
//                        if (Xdiff < 10 && Ydiff < 10) {
//                            if (isViewCollapsed()) {
//                                //When user clicks on the image view of the collapsed layout,
//                                //visibility of the collapsed layout will be changed to "View.GONE"
//                                //and expanded view will become visible.
//                                collapsedView.setVisibility(View.GONE);
//                                expandedView.setVisibility(View.VISIBLE);
//                            }
//                        }
                        return true;
                }
                return false;
            }
        });


    }

    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
//    private boolean isViewCollapsed() {
//        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
//    }

    private void createThresholdValueDialog(Context context){
        String[] items = {
                "10 MB",
                "100 MB",
                "200 MB",
                "300 MB",
                "400 MB",
                "500 MB",
                "600 MB",
                "700 MB",
                "800 MB",
                "900 MB",
                "1 GB",
                "2 GB",
        };

        new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.CustomAlertDialogStyle))
                .setTitle(R.string.select_new_threshold_value)
                .setItems(items, (dialogInterface, which) -> {
                    Log.d("SELECTED_ITEM", items[which]);
                    System.out.println("ThresholdType: "+thresholdType);
                })
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, which) -> dialogInterface.dismiss())
                .create()
                .show();

    }

    private void saveWifiThresholdValue(int val, Context context) {

        long startTime = System.currentTimeMillis();
        long bytesTobeUsed = (long) val * 1024 * 1024;


        PreferenceUtils.saveWifiThresholdStartTimePref(startTime, context);
        PreferenceUtils.saveWifiThresholdValuePref(bytesTobeUsed, context);
        PreferenceUtils.saveWifiThresholdStatus(true, context);
        AppUtils.showSuccessToast(context, getString(R.string.threshold_success));

    }

    private void saveCellularThresholdValue(int val, Context context) {

        long startTime = System.currentTimeMillis();
        long bytesTobeUsed = (long) val * 1024 * 1024;

        PreferenceUtils.saveThresholdStartTimePref(startTime, context);
        PreferenceUtils.saveThresholdValuePref(bytesTobeUsed, context);
        PreferenceUtils.saveThresholdStatus(true, context);
        AppUtils.showSuccessToast(context, getString(R.string.threshold_success));

    }

}
