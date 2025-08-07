package ridc.sduma.kifurushi.Receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;

import ridc.sduma.kifurushi.Service.AppForegroundService;
import ridc.sduma.kifurushi.Service.ThresholdService;
import ridc.sduma.kifurushi.activities.MainActivity;
import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "threshold_channel";
    private static final CharSequence CHANNEL_NAME = "Alarm_Channel_CHANNEL_NAME";


    public AlarmReceiver() {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        receiveThreshold(context);
    }

    private void receiveThreshold(Context context) {

        if (PreferenceUtils.isThresholdValueSet(context)) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetStatsUtil statisticsUtil = new NetStatsUtil(networkStatsManager);

            //Get threshold value from shared preference
            long thresholdValue = PreferenceUtils.getThresholdValuePref(context);

            //Get start time value from shared preference
            long startTime = PreferenceUtils.getThresholdStartTimePref(context);

            //Get all received bytes
            long allReceivedBytes = statisticsUtil.getAllRxBytesMobile(context, startTime, System.currentTimeMillis());

            //Get all sent bytes
            long allSentBytes = statisticsUtil.getAllTxBytesMobile(context, startTime, System.currentTimeMillis());

            long totalUsage = allReceivedBytes + allSentBytes;

            if (totalUsage >= thresholdValue) {
                showAlarmNotification(context, "CELLULAR");
                PreferenceUtils.saveThresholdStatus(false, context);
                PreferenceUtils.clearThresholdStartTime(context);
                PreferenceUtils.clearThresholdValuePrefs(context);

            }
        }

        if (PreferenceUtils.isWifiThresholdValueSet(context)) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetStatsUtil statisticsUtil = new NetStatsUtil(networkStatsManager);

            //Get threshold value from shared preference
            long thresholdValue = PreferenceUtils.getWifiThresholdValuePref(context);

            //Get start time value from shared preference
            long startTime = PreferenceUtils.getWifiThresholdStartTimePref(context);

            //Get all received bytes
            long allReceivedBytes = statisticsUtil.getAllRxBytesWifi(startTime, System.currentTimeMillis());

            //Get all sent bytes
            long allSentBytes = statisticsUtil.getAllTxBytesWifi(startTime, System.currentTimeMillis());

            long totalUsage = allReceivedBytes + allSentBytes;

            if (totalUsage >= thresholdValue) {
                showAlarmNotification(context, "WIFI");
                PreferenceUtils.saveWifiThresholdStatus(false, context);
                PreferenceUtils.clearWifiThresholdStartTime(context);
                PreferenceUtils.clearWifiThresholdValuePrefs(context);
            }
        }

    }


    public void showAlarmNotification(Context context, String thresholdType) {
        sendOnChannel(context, thresholdType);
    }


    public static void sendOnChannel(Context context, String thresholdType) {

        Intent resultIntent = new Intent(context, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService((Context.NOTIFICATION_SERVICE));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLightColor(Color.BLUE);
            channel.enableLights(true);
            channel.setImportance(NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        ringtone.play();
        PreferenceUtils.saveThresholdStatus(false, context);
        PreferenceUtils.clearThresholdStartTime(context);
        PreferenceUtils.clearThresholdValuePrefs(context);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.tcra)
                .setColor(Color.WHITE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentText(context.getString(R.string.maximun_value_reached))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(121212, notification);
        Intent intent = new Intent(context, ThresholdService.class);
        intent.putExtra("THRESHOLD_TYPE", thresholdType);
        context.startService(intent);

    }


}