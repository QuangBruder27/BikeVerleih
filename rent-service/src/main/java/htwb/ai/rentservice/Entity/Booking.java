package htwb.ai.rentservice.Entity;

import javax.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "custom_id")
    private String customId;

    @Column(name = "bike_id")
    private String bikeId;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "status")
    private String status;


    public Booking(String customId, String bikeId, String status) {
        this.customId = customId;
        this.bikeId = bikeId;
        this.status = status;
    }

    public Booking() { }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }


    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customId='" + customId + '\'' +
                ", bikeId='" + bikeId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", distance=" + distance +
                ", status='" + status + '\'' +
                '}';
    }
}
