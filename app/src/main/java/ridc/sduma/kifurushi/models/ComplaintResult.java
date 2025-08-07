package ridc.sduma.kifurushi.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ComplaintResult {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("complaint_closed")
    private boolean complaintClosed;

    @SerializedName("complaint_has_rated")
    private boolean complaintHasRated;

    @SerializedName("has_quick_feedback")
    private boolean hasQQuickFeedback;

    @SerializedName("institution_name")
    private String institutionName;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isComplaintClosed() {
        return complaintClosed;
    }

    public void setComplaintClosed(boolean complaintClosed) {
        this.complaintClosed = complaintClosed;
    }

    public boolean isComplaintHasRated() {
        return complaintHasRated;
    }

    public void setComplaintHasRated(boolean complaintHasRated) {
        this.complaintHasRated = complaintHasRated;
    }

    public boolean isHasQQuickFeedback() {
        return hasQQuickFeedback;
    }

    public void setHasQQuickFeedback(boolean hasQQuickFeedback) {
        this.hasQQuickFeedback = hasQQuickFeedback;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    @NonNull
    @Override
    public String toString() {
        return "ComplaintResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", complaintClosed=" + complaintClosed +
                ", complaintHasRated=" + complaintHasRated +
                ", hasQQuickFeedback=" + hasQQuickFeedback +
                ", institutionName='" + institutionName + '\'' +
                '}';
    }

}
