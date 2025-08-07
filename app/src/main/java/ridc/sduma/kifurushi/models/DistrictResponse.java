package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictResponse {
    @SerializedName("locations")
    private List<District> districtList;

    public DistrictResponse() {
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }

    public List<District> getDistrictList() {
        return districtList;
    }
}
