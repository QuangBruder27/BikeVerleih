package htwb.ai.rentservice.Entity;

import javax.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "bike_id")
    private String bikeId;

    @Column(name = "begin_time")
    private String beginTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "status")
    private String status;


    public Booking(String customerId, String bikeId, String status) {
        this.customerId = customerId;
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

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
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

    public boolean isAcceptable(){
        return this.distance!=null && !this.endTime.isEmpty()
                && !this.bikeId.isEmpty() && this.id!=null;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", bikeId='" + bikeId + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", distance=" + distance +
                ", status='" + status + '\'' +
                '}';
    }

}
