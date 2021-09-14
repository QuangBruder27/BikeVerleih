package htwb.ai.reportservice.Entity;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "report")
public class Report {

    @Id
    private String reportId;
    private String customerId;
    private String bikeId;
    private String note;
    private boolean isSolved;

    private Binary image;

    public Report(){};

    public Report(String reportId, String customerId, String bikeId, String note, boolean isSolved, Binary image) {
        this.reportId = reportId;
        this.customerId = customerId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public boolean isAcceptable(){
        return (this.bikeId!= null && !this.bikeId.isEmpty()
                && this.note!=null && !this.note.isEmpty()
                && this.customerId!=null && !this.customerId.isEmpty());
    }


    @Override
    public String toString() {
        return "Report{" +
                "reportId='" + reportId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", bikeId='" + bikeId + '\'' +
                ", note='" + note + '\'' +
                ", isSolved=" + isSolved +
                ", image=" + image +
                '}';
    }

    public boolean equals(Report that) {
        return this.reportId.equals(that.reportId)
                && this.note.equals(that.note)
                && this.customerId.equals(that.customerId)
                && this.image.equals(that.image);
    }
}
