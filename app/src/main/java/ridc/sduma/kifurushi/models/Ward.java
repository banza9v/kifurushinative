package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

public class Ward {

    @SerializedName("ward_id")
    private int wardId;

    @SerializedName("ward_token")
    private String wardToken;

    @SerializedName("ward_name")
    private String wardName;

    @SerializedName("ward_uniqueID")
    private String wardUniqueID;

    @SerializedName("ward_code")
    private String wardCode;

    @SerializedName("parent_distric")
    private String parentDistrict;

    public Ward() {
    }

    public Ward(int wardId, String wardToken, String wardName, String wardUniqueID, String wardCode, String parentDistrict) {
        this.wardId = wardId;
        this.wardToken = wardToken;
        this.wardName = wardName;
        this.wardUniqueID = wardUniqueID;
        this.wardCode = wardCode;
        this.parentDistrict = parentDistrict;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }

    public String getWardToken() {
        return wardToken;
    }

    public void setWardToken(String wardToken) {
        this.wardToken = wardToken;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getWardUniqueID() {
        return wardUniqueID;
    }

    public void setWardUniqueID(String wardUniqueID) {
        this.wardUniqueID = wardUniqueID;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getParentDistrict() {
        return parentDistrict;
    }

    public void setParentDistrict(String parentDistrict) {
        this.parentDistrict = parentDistrict;
    }
}
