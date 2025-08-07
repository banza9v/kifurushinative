package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StreetResponse {
    @SerializedName("locations")
    private List<Street> streetList;

    public StreetResponse() {
    }

    public StreetResponse(List<Street> streetList) {
        this.streetList = streetList;
    }

    public List<Street> getStreetList() {
        return streetList;
    }

    public void setStreetList(List<Street> streetList) {
        this.streetList = streetList;
    }
}
