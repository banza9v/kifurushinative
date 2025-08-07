package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

public class District {

    @SerializedName("distric_id")
    private int id;

    @SerializedName("distric_token")
    private String districtToken;

    @SerializedName("distric_name")
    private String districtName;

    @SerializedName("distric_uniqueID")
    private String districtUniqueID;

    @SerializedName("distric_code")
    private String districtCode;

    @SerializedName("parent_region")
    private String parentRegion;

    public District(int id, String districtToken, String districtName, String districtUniqueID, String districtCode, String parentRegion) {
        this.id = id;
        this.districtToken = districtToken;
        this.districtName = districtName;
        this.districtUniqueID = districtUniqueID;
        this.districtCode = districtCode;
        this.parentRegion = parentRegion;
    }

    public District() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistrictToken() {
        return districtToken;
    }

    public void setDistrictToken(String districtToken) {
        this.districtToken = districtToken;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictUniqueID() {
        return districtUniqueID;
    }

    public void setDistrictUniqueID(String districtUniqueID) {
        this.districtUniqueID = districtUniqueID;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getParentRegion() {
        return parentRegion;
    }

    public void setParentRegion(String parentRegion) {
        this.parentRegion = parentRegion;
    }
}
