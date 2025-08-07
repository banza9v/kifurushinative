package ridc.sduma.kifurushi.models;

public class Report {
    private String complaint_to;
    private String attachments_file;
    private String compl_title_name;
    private String complaint_descriptions;
    private String compl_type;
    private String mkoa_alipotoka;
    private String wilaya_alipotoka;
    private String kata_alipotoka;
    private String mtaa_alipotoka;
    private String phone_number;
    private String sugestions_box;
    private String compl_title;
    private String any_suggestions;
    private String compl_origin;
    private String country_origin;

    public Report() {
    }

    public Report(String complaint_to, String attachments_file, String compl_title_name, String complaint_descriptions,
                  String compl_type, String mkoa_alipotoka, String wilaya_alipotoka, String kata_alipotoka,
                  String mtaa_alipotoka, String phone_number, String sugestions_box, String compl_title,
                  String any_suggestions, String compl_origin, String country_origin) {
        this.complaint_to = complaint_to;
        this.attachments_file = attachments_file;
        this.compl_title_name = compl_title_name;
        this.complaint_descriptions = complaint_descriptions;
        this.compl_type = compl_type;
        this.mkoa_alipotoka = mkoa_alipotoka;
        this.wilaya_alipotoka = wilaya_alipotoka;
        this.kata_alipotoka = kata_alipotoka;
        this.mtaa_alipotoka = mtaa_alipotoka;
        this.phone_number = phone_number;
        this.sugestions_box = sugestions_box;
        this.compl_title = compl_title;
        this.any_suggestions = any_suggestions;
        this.compl_origin = compl_origin;
        this.country_origin = country_origin;
    }

    public String getComplaint_to() {
        return complaint_to;
    }

    public void setComplaint_to(String complaint_to) {
        this.complaint_to = complaint_to;
    }

    public String getAttachments_file() {
        return attachments_file;
    }

    public void setAttachments_file(String attachments_file) {
        this.attachments_file = attachments_file;
    }

    public String getCompl_title_name() {
        return compl_title_name;
    }

    public void setCompl_title_name(String compl_title_name) {
        this.compl_title_name = compl_title_name;
    }

    public String getComplaint_descriptions() {
        return complaint_descriptions;
    }

    public void setComplaint_descriptions(String complaint_descriptions) {
        this.complaint_descriptions = complaint_descriptions;
    }

    public String getCompl_type() {
        return compl_type;
    }

    public void setCompl_type(String compl_type) {
        this.compl_type = compl_type;
    }

    public String getMkoa_alipotoka() {
        return mkoa_alipotoka;
    }

    public void setMkoa_alipotoka(String mkoa_alipotoka) {
        this.mkoa_alipotoka = mkoa_alipotoka;
    }

    public String getWilaya_alipotoka() {
        return wilaya_alipotoka;
    }

    public void setWilaya_alipotoka(String wilaya_alipotoka) {
        this.wilaya_alipotoka = wilaya_alipotoka;
    }

    public String getKata_alipotoka() {
        return kata_alipotoka;
    }

    public void setKata_alipotoka(String kata_alipotoka) {
        this.kata_alipotoka = kata_alipotoka;
    }

    public String getMtaa_alipotoka() {
        return mtaa_alipotoka;
    }

    public void setMtaa_alipotoka(String mtaa_alipotoka) {
        this.mtaa_alipotoka = mtaa_alipotoka;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSugestions_box() {
        return sugestions_box;
    }

    public void setSugestions_box(String sugestions_box) {
        this.sugestions_box = sugestions_box;
    }

    public String getCompl_title() {
        return compl_title;
    }

    public void setCompl_title(String compl_title) {
        this.compl_title = compl_title;
    }

    public String getAny_suggestions() {
        return any_suggestions;
    }

    public void setAny_suggestions(String any_suggestions) {
        this.any_suggestions = any_suggestions;
    }

    public String getCompl_origin() {
        return compl_origin;
    }

    public void setCompl_origin(String compl_origin) {
        this.compl_origin = compl_origin;
    }

    public String getCountry_origin() {
        return country_origin;
    }

    public void setCountry_origin(String country_origin) {
        this.country_origin = country_origin;
    }

    @Override
    public String toString() {
        return "Report{" +
                "complaint_to='" + complaint_to + '\'' +
                ", attachments_file='" + attachments_file + '\'' +
                ", compl_title_name='" + compl_title_name + '\'' +
                ", complaint_descriptions='" + complaint_descriptions + '\'' +
                ", compl_type='" + compl_type + '\'' +
                ", mkoa_alipotoka='" + mkoa_alipotoka + '\'' +
                ", wilaya_alipotoka='" + wilaya_alipotoka + '\'' +
                ", kata_alipotoka='" + kata_alipotoka + '\'' +
                ", mtaa_alipotoka='" + mtaa_alipotoka + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", sugestions_box='" + sugestions_box + '\'' +
                ", compl_title='" + compl_title + '\'' +
                ", any_suggestions='" + any_suggestions + '\'' +
                ", compl_origin='" + compl_origin + '\'' +
                ", country_origin='" + country_origin + '\'' +
                '}';
    }
}
