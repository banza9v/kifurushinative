package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WardResponse {

    @SerializedName("locations")
    private List<Ward> wardList;

    public WardResponse() {
    }

    public WardResponse(List<Ward> wardList) {
        this.wardList = wardList;
    }

    public List<Ward> getWardList() {
        return wardList;
    }

    public void setWardList(List<Ward> wardList) {
        this.wardList = wardList;
    }
}
