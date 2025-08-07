package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

public class Region {

    @SerializedName("reginal_token")
    private String regionalToken;

    @SerializedName("reginal_uniqueID")
    private String uniqueID;

    @SerializedName("reginal_name")
    private String regionalName;

    public Region() {
    }

    public Region(String regionalToken, String uniqueID, String regionalName) {
        this.regionalToken = regionalToken;
        this.uniqueID = uniqueID;
        this.regionalName = regionalName;
    }

    public String getRegionalToken() {
        return regionalToken;
    }

    public void setRegionalToken(String regionalToken) {
        this.regionalToken = regionalToken;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getRegionalName() {
        return regionalName;
    }

    public void setRegionalName(String regionalName) {
        this.regionalName = regionalName;
    }

}
