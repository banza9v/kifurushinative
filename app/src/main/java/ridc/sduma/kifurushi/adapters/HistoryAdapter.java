package ridc.sduma.kifurushi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.models.History;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {

    private List<History> historyList;
    private final OnHistoryClickListener listener;

    public HistoryAdapter(OnHistoryClickListener listener) {
        this.listener = listener;
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryVH holder, int position) {

        History history = historyList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMM dd yyyy hh:mm", Locale.getDefault());
        String date = sdf.format(new Date(history.getComplaint_date()));

        holder.title.setText(history.getComplaintTitle());
        holder.idTV.setText(history.getComplaintId());
        holder.dateTV.setText(date);

        holder.followComplaint.setOnClickListener(view -> listener.onHistoryClick(history));

    }

    @Override
    public int getItemCount() {
        return (historyList != null) ? historyList.size() : 0;
    }

    class HistoryVH extends RecyclerView.ViewHolder{
        TextView title, idTV, dateTV;
        AppCompatTextView followComplaint;
        public HistoryVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            idTV = itemView.findViewById(R.id.complaintId);
            dateTV = itemView.findViewById(R.id.date);
            followComplaint = itemView.findViewById(R.id.followComplaint);
        }
    }

    public interface OnHistoryClickListener{
        void onHistoryClick(History history);
    }

}
