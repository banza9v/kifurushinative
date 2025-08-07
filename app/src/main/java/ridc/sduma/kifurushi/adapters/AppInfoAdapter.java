package ridc.sduma.kifurushi.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skydoves.progressview.ProgressView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PackageUtil;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoVH> implements Filterable {

    private List<ApplicationInfo> infoList;
    private List<ApplicationInfo> infoListFiltered;
    private long totalBytes;
    private final Context context;
    private final NetStatsUtil netStatsUtil;
    private long startTime, endTime;
    private final Activity activity;

    public AppInfoAdapter(Context context, NetStatsUtil netStatsUtil, Activity activity) {
        this.context = context;
        this.netStatsUtil = netStatsUtil;
        this.activity = activity;
    }

    public void setInfoList(List<ApplicationInfo> infoList, long totalBytes) {
        this.infoList = infoList;
        this.totalBytes = totalBytes;
        infoListFiltered = infoList;
        notifyDataSetChanged();
    }

    public void setDuration(long startTime, long endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppInfoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new AppInfoVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppInfoVH holder, int position) {
        ApplicationInfo app = infoListFiltered.get(position);

        String appName = PackageUtil.getAppName(context, app);
        int uid = PackageUtil.getPackageUid(context, app.packageName);
        Drawable icon = PackageUtil.getPackageDrawable(context, app);

        Executors.newSingleThreadExecutor().execute(() -> {
            long received = netStatsUtil.getPackageRxBytesWifi(uid, startTime, endTime);
            long sent = netStatsUtil.getPackageTxBytesWifi(uid, startTime, endTime);
            long total = received+sent;
            double progress =  (total /(double) totalBytes) * 100;

            activity.runOnUiThread(() -> {
                String downloads = AppUtils.bytesConverter(received) + " "+AppUtils.getUnit(received);
                String uploads = AppUtils.bytesConverter(sent) + " "+AppUtils.getUnit(sent);
                String totalText = roundPercentage(AppUtils.bytesConverter(total)) + " "+AppUtils.getUnit(total);

                holder.downloadsTv.setText(downloads);
                holder.uploadsTv.setText(uploads);
                holder.displayTotal.setText(totalText);
                holder.appName.setText(appName);
                holder.appIcon.setImageDrawable(icon);
                holder.progressView.setProgress((float) progress);
                holder.progressView.setLabelText(roundPercentage(progress)+"%");
            });
        });


    }

    @Override
    public int getItemCount() {
        return infoList != null ? infoList.size() : 0;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    infoListFiltered = infoList;
                } else {
                    List<ApplicationInfo> filteredList = new ArrayList<>();
                    for (ApplicationInfo row : infoList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (PackageUtil.getAppName(context, row).toLowerCase().contains(charString.toLowerCase()) /*|| row.packageName.contains(charSequence)*/) {
                            filteredList.add(row);
                        }
                    }

                    infoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = infoListFiltered;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                infoListFiltered = Collections.unmodifiableList((List<ApplicationInfo>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    class AppInfoVH extends RecyclerView.ViewHolder{
        TextView appName, downloadsTv, uploadsTv,displayTotal;
        ProgressView progressView;
        ImageView appIcon;
        public AppInfoVH(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.appName);
            downloadsTv = itemView.findViewById(R.id.downloads);
            uploadsTv = itemView.findViewById(R.id.uploads);
            progressView = itemView.findViewById(R.id.progressView);
            appIcon = itemView.findViewById(R.id.appIcon);
            displayTotal = itemView.findViewById(R.id.displayTotal);
        }
    }

    private double roundPercentage(double percent){
        BigDecimal number = new BigDecimal(percent);
        BigDecimal decimal = number.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return decimal.doubleValue();
    }


}
