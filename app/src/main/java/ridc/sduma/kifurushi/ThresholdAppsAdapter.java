package ridc.sduma.kifurushi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ridc.sduma.kifurushi.R;

import ridc.sduma.kifurushi.Utils.AppDetails;

import java.util.List;

public class ThresholdAppsAdapter extends RecyclerView.Adapter<ThresholdAppsAdapter.VH> {

    private List<AppDetails> appDetailsList;

    public void setAppDetailsList(List<AppDetails> appDetailsList) {
        this.appDetailsList = appDetailsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == R.layout.app_item)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_top_item, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        if (position == 0){

        }else {
            AppDetails app = appDetailsList.get(position);

            String downloads = app.getDownloads() + " "+app.getDownloadUnit();
            String uploads = app.getUploads() + " "+app.getUploadUnit();
            String total = app.getTotalBytes() + " "+app.getTotalUnit();

            //holder.totalTv.setText(total);
            holder.downloadsTv.setText(downloads);
            holder.uploadsTv.setText(uploads);
            holder.appName.setText(app.getAppName());
            holder.appIcon.setImageDrawable(app.getAppIcon());
        }

    }

    @Override
    public int getItemCount() {
        return appDetailsList != null? appDetailsList.size()+1 : 0;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView appName, downloadsTv, uploadsTv, totalTv;
        ImageView appIcon;
        public VH(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.appName);
            downloadsTv = itemView.findViewById(R.id.downloads);
            uploadsTv = itemView.findViewById(R.id.uploads);
            //totalTv = itemView.findViewById(R.id.total);
            appIcon = itemView.findViewById(R.id.appIcon);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? R.layout.apps_top_item : R.layout.app_item;
    }
}
