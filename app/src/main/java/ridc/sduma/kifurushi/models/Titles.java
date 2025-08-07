package ridc.sduma.kifurushi.models;

import com.google.gson.annotations.SerializedName;

public class Titles {

    @SerializedName("class_id")
    private int classId;

    @SerializedName("class_title")
    private String classTitle;

    @SerializedName("institution_id_id")
    private int id;

    public Titles() {
    }

    public Titles(int classId, String classTitle, int id) {
        this.classId = classId;
        this.classTitle = classTitle;
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Titles{" +
                "classId=" + classId +
                ", classTitle='" + classTitle + '\'' +
                ", id=" + id +
                '}';
    }
}
