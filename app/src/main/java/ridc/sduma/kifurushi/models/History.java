package ridc.sduma.kifurushi.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "complaint_title")
    private String complaintTitle;

    @ColumnInfo(name = "complaint_type")
    private String complaintType;

    @ColumnInfo(name = "complaint_id")
    private String complaintId;

    @ColumnInfo(name = "complaintDate")
    private long complaint_date;

    public History() {
    }

    public History(String complaintTitle, String complaintType, String complaintId, long complaint_date) {
        this.complaintTitle = complaintTitle;
        this.complaintType = complaintType;
        this.complaintId = complaintId;
        this.complaint_date = complaint_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public long getComplaint_date() {
        return complaint_date;
    }

    public void setComplaint_date(long complaint_date) {
        this.complaint_date = complaint_date;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", complaintTitle='" + complaintTitle + '\'' +
                ", complaintType='" + complaintType + '\'' +
                ", complaintId='" + complaintId + '\'' +
                ", complaint_date=" + complaint_date +
                '}';
    }
}
