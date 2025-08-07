package ridc.sduma.kifurushi.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PreferenceUtils;
import ridc.sduma.kifurushi.activities.MainActivity;

public class AppForegroundService extends Service {
    Timer timer;

    @Override
    public void onCreate() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
                updateNotification(networkStatsManager);
            }
        }, 5000, 1000 * 60);

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
        handleNotification(networkStatsManager);

        return super.onStartCommand(intent, flags, startId);

    }


    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            boolean wifiCapability = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            boolean cellularCapability = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            boolean vpnCapability = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
            if (wifiCapability) {
                //showNetworkCapabilityNotification("Wi-Fi");
            }
            if (cellularCapability) {
                //showNetworkCapabilityNotification("Cellular");
            }
            if (vpnCapability) {
                //showNetworkCapabilityNotification("VPN");
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void handleNotification(NetworkStatsManager networkStatsManager) {

        Calendar cal;
        long startDate, endDate;

        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_MONTH, 1);

        startDate = cal.getTimeInMillis();
        endDate = System.currentTimeMillis();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        String CHANNEL_ID = "com.sduma.CHANNEL_ID";
        String CHANNEL_NAME = "Kifurushi notification CHANNEL_NAME";

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());

        String month = getString(R.string.data_used_notif) + " "+sdf.format(new Date(startDate));

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }


        NetStatsUtil util = new NetStatsUtil(networkStatsManager);

        /*===============================================Start Mobile Calculations=====================================================*/

        NetStatsUtil.UsageStats usageStatsMobile = util.getTotalBytesByMobile(this, startDate, endDate);
        long downloadsMobile = usageStatsMobile.getDownloads();
        long uploadsMobile = usageStatsMobile.getUploads();

        /*===============================================End of Mobile Calculations=====================================================*/


        /*===============================================Start of Wi-Fi Calculations=====================================================*/

        NetStatsUtil.UsageStats usageStatsWifi = util.getTotalBytesByWifi(this, startDate, endDate);
        long downloadsWifi = usageStatsWifi.getDownloads();
        long uploadsWifi = usageStatsWifi.getUploads();

        /*===============================================End of Wi-Fi Calculations=====================================================*/


        String receivedMobile = AppUtils.bytesConverter(downloadsMobile) + " " + AppUtils.getUnit(downloadsMobile);
        String sentMobile = AppUtils.bytesConverter(uploadsMobile) + " " + AppUtils.getUnit(uploadsMobile);


        String receivedWifi = AppUtils.bytesConverter(downloadsWifi) + " " + AppUtils.getUnit(downloadsWifi);
        String sentWifi = AppUtils.bytesConverter(uploadsWifi) + " " + AppUtils.getUnit(uploadsWifi);


        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);

        notificationLayout.setTextViewText(R.id.titleNotification, month);
        notificationLayout.setTextViewText(R.id.cellularReceived, receivedMobile);
        notificationLayout.setTextViewText(R.id.cellularSent, sentMobile);
        notificationLayout.setTextViewText(R.id.wifiReceived, receivedWifi);
        notificationLayout.setTextViewText(R.id.wifiSent, sentWifi);

        notificationLayoutExpanded.setTextViewText(R.id.titleNotification, month);
        notificationLayoutExpanded.setTextViewText(R.id.cellularReceived, receivedMobile);
        notificationLayoutExpanded.setTextViewText(R.id.cellularSent, sentMobile);
        notificationLayoutExpanded.setTextViewText(R.id.wifiReceived, receivedWifi);
        notificationLayoutExpanded.setTextViewText(R.id.wifiSent, sentWifi);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.tcra)
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(pendingIntent);
        startForeground(1243, builder.build());
    }

    // use this method to update the Notification's UI
    private void updateNotification(NetworkStatsManager networkStatsManager) {

        Calendar cal;
        long startDate, endDate;

        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.set(Calendar.DAY_OF_MONTH, 1);

        startDate = cal.getTimeInMillis();
        endDate = System.currentTimeMillis();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        String CHANNEL_ID = "com.sduma.CHANNEL_ID";
        String CHANNEL_NAME = "Kifurushi notification CHANNEL_NAME";

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", Locale.getDefault());

        String month = getString(R.string.data_used_notif) + " "+sdf.format(new Date(startDate));

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }

        NetStatsUtil util = new NetStatsUtil(networkStatsManager);

/*===============================================Start Mobile Calculations=====================================================*/

        NetStatsUtil.UsageStats usageStatsMobile = util.getTotalBytesByMobile(this, startDate, endDate);
        long downloadsMobile = usageStatsMobile.getDownloads();
        long uploadsMobile = usageStatsMobile.getUploads();

/*===============================================End of Mobile Calculations=====================================================*/


/*===============================================Start of Wi-Fi Calculations=====================================================*/

        NetStatsUtil.UsageStats usageStatsWifi = util.getTotalBytesByWifi(this, startDate, endDate);
        long downloadsWifi = usageStatsWifi.getDownloads();
        long uploadsWifi = usageStatsWifi.getUploads();

/*===============================================End of Wi-Fi Calculations=====================================================*/

        String receivedMobile = AppUtils.roundOffToAnySignificantFigure(1, AppUtils.bytesConverter(downloadsMobile)) + " " + AppUtils.getUnit(downloadsMobile);
        String sentMobile = AppUtils.roundOffToAnySignificantFigure(1, AppUtils.bytesConverter(uploadsMobile)) + " " + AppUtils.getUnit(uploadsMobile);

        String receivedWifi = AppUtils.roundOffToAnySignificantFigure(1, AppUtils.bytesConverter(downloadsWifi)) + " " + AppUtils.getUnit(downloadsWifi);
        String sentWifi = AppUtils.roundOffToAnySignificantFigure(1, AppUtils.bytesConverter(uploadsWifi)) + " " + AppUtils.getUnit(uploadsWifi);


        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
        // update the icon
        notificationLayout.setTextViewText(R.id.titleNotification, month);
        notificationLayout.setTextViewText(R.id.cellularReceived, receivedMobile);
        notificationLayout.setTextViewText(R.id.cellularSent, sentMobile);
        notificationLayout.setTextViewText(R.id.wifiReceived, receivedWifi);
        notificationLayout.setTextViewText(R.id.wifiSent, sentWifi);

        notificationLayoutExpanded.setTextViewText(R.id.titleNotification, month);
        notificationLayoutExpanded.setTextViewText(R.id.cellularReceived, receivedMobile);
        notificationLayoutExpanded.setTextViewText(R.id.cellularSent, sentMobile);
        notificationLayoutExpanded.setTextViewText(R.id.wifiReceived, receivedWifi);
        notificationLayoutExpanded.setTextViewText(R.id.wifiSent, sentWifi);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.tcra)
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        manager.notify(1243, builder.build());


    }

    private void showNetworkCapabilityNotification(String type) {
        String title = "You are currently connected to a " + type + " Network";
        int notification_id = 100100;

        String CHANNEL_ID = "showNetworkCapabilityNotification.CHANNEL_ID";
        String CHANNEL_NAME = "showNetworkCapabilityNotification.CHANNEL_NAME";

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            //channel.setSound();
            manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.tcra)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setColor(Color.BLUE)
                .build();

        manager.notify(notification_id, notification);

    }


}


