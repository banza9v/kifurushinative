package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

public class ReportResponse {
    @SerializedName("code")
    private int cod;

    @SerializedName("ID")
    private String id;

    @SerializedName("message")
    private String message;

    public ReportResponse() {
    }

    public ReportResponse(int cod, String id, String message) {
        this.cod = cod;
        this.id = id;
        this.message = message;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ReportResponse{" +
                "cod=" + cod +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
