package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

public class Street {

    @SerializedName("street_id")
    private int id;

    @SerializedName("street_token")
    private String streetToken;

    @SerializedName("street_name")
    private String streetName;

    @SerializedName("street_uniqueID")
    private String streetUniqueID;

    @SerializedName("street_code")
    private String streetCode;

    @SerializedName("parent_ward")
    private String parent_ward;

    public Street() {
    }

    public Street(int id, String streetToken, String streetName, String streetUniqueID, String streetCode, String parent_ward) {
        this.id = id;
        this.streetToken = streetToken;
        this.streetName = streetName;
        this.streetUniqueID = streetUniqueID;
        this.streetCode = streetCode;
        this.parent_ward = parent_ward;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreetToken() {
        return streetToken;
    }

    public void setStreetToken(String streetToken) {
        this.streetToken = streetToken;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetUniqueID() {
        return streetUniqueID;
    }

    public void setStreetUniqueID(String streetUniqueID) {
        this.streetUniqueID = streetUniqueID;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getParent_ward() {
        return parent_ward;
    }

    public void setParent_ward(String parent_ward) {
        this.parent_ward = parent_ward;
    }
}
