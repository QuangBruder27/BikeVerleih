package htwb.ai.reportservice.Entity;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "report")
public class Report {

    @Id
    private String reportId;
    private String customId;
    private String bikeId;
    private String note;
    private boolean isSolved;

    private Binary image;

    public Report(){};

    public Report(String reportId, String customId, String bikeId, String note, boolean isSolved, Binary image) {
        this.reportId = reportId;
        this.customId = customId;
        this.bikeId = bikeId;
        this.note = note;
        this.isSolved = isSolved;
        this.image = image;
    }

    public Binary getImage() {
        return image;
    }

    public void setImage(Binary image) {
        this.image = image;
    }


    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Report{" +
                "reportId='" + reportId + '\'' +
                ", customId='" + customId + '\'' +
                ", bikeId='" + bikeId + '\'' +
                ", note='" + note + '\'' +
                ", isSolved=" + isSolved +
                ", image=" + image +
                '}';
    }

    public boolean acceptable(){
        return //this.getReportId() != null
                 !this.getBikeId().isEmpty()
                && !this.getNote().isEmpty()
                && !this.getCustomId().isEmpty();
    }

}
