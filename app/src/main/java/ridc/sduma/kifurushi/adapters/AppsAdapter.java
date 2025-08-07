package ridc.sduma.kifurushi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
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

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.Utils.AppDetails;
import ridc.sduma.kifurushi.Utils.AppUtils;
import ridc.sduma.kifurushi.Utils.NetStatsUtil;
import ridc.sduma.kifurushi.Utils.PackageUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppsVH> implements Filterable {

    private List<AppDetails> appDetails;
    private List<AppDetails> filteredList;
    private long totalBytes;
    private NetStatsUtil util;
    private Context context;
    private long startTime;
    private long endTime;
    private int networkType;

    public AppsAdapter(NetStatsUtil util, Context context, int networkType, long startTime, long endTime) {
        this.util = util;
        this.context = context;
        this.startTime = startTime;
        this.endTime = endTime;
        this.networkType = networkType;
    }

    public void setAppDetails(List<AppDetails> appDetails, long totalBytes) {
        this.appDetails = appDetails;
        this.totalBytes = totalBytes;
        this.filteredList = appDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new AppsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsVH holder, int position) {
        AppDetails app = filteredList.get(position);

        AppDetails stats = util.getUsageStatsForUid(context, app, networkType, startTime, endTime);

        String downloads = AppUtils.roundOffToAnySignificantFigure(3, stats.getDownloads()) + " "+stats.getDownloadUnit();
        String uploads = AppUtils.roundOffToAnySignificantFigure(3, stats.getUploads()) + " "+stats.getUploadUnit();
        String total = AppUtils.roundOffToAnySignificantFigure(3, stats.getTotalBytes()) + " "+stats.getTotalUnit();

        double progress =  (stats.getTotalInBytes() /(double) totalBytes) * 100;

        holder.downloadsTv.setText(downloads);
        holder.uploadsTv.setText(uploads);
        holder.displayTotal.setText(total);
        holder.appName.setText(app.getAppName());
        holder.appIcon.setImageDrawable(app.getAppIcon());
        holder.progressView.setProgress((float) progress);
        holder.progressView.setLabelText(roundPercentage(progress)+"%");

    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() :  0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = appDetails;
                } else {
                    List<AppDetails> filtered = new ArrayList<>();
                    for (AppDetails row : appDetails) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAppName().toLowerCase().contains(charString.toLowerCase())) {
                            filtered.add(row);
                        }
                    }

                    filteredList = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<AppDetails>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class AppsVH extends RecyclerView.ViewHolder {
        TextView appName, downloadsTv, uploadsTv,displayTotal;
        ProgressView progressView;
        ImageView appIcon;
        public AppsVH(@NonNull View itemView) {
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
        BigDecimal decimal = number.setScale(1, RoundingMode.HALF_EVEN);
        return decimal.doubleValue();
    }
}
