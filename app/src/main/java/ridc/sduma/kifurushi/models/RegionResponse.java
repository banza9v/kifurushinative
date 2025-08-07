package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegionResponse {

    @SerializedName("locations")
    private List<Region> regionList;

    public RegionResponse() {
    }

    public RegionResponse(List<Region> regionList) {
        this.regionList = regionList;
    }

    public List<Region> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<Region> regionList) {
        this.regionList = regionList;
    }
}
