package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TitleResponse {
    @SerializedName("complaint_titles")
    private List<Titles> titlesList;

    public TitleResponse(List<Titles> titlesList) {
        this.titlesList = titlesList;
    }

    public List<Titles> getTitlesList() {
        return titlesList;
    }

    public void setTitlesList(List<Titles> titlesList) {
        this.titlesList = titlesList;
    }

    public TitleResponse() {
    }

    @Override
    public String toString() {
        return "TitleResponse{" +
                "titlesList=" + titlesList +
                '}';
    }
}
