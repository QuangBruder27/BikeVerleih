package htwb.ai.locationservice.Entity;

import javax.persistence.*;

@Entity
@Table(name="location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bike_id")
    private String bikeId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longtitude")
    private String longtitude;

    @Column(name = "time_created")
    private String timeCreated;

    public Location() {
    }

    public Location(String bikeId, String latitude, String longtitude, String timeCreated) {
        this.bikeId = bikeId;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.timeCreated = timeCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", bikeId='" + bikeId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                ", timeCreated='" + timeCreated + '\'' +
                '}';
    }
}
