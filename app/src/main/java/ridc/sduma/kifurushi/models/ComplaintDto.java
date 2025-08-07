package ridc.sduma.kifurushi.models;

public class ComplaintDto {

    private String complaint_id;

    public ComplaintDto(String complaint_id){
        this.complaint_id = complaint_id;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

}
