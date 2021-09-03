package htwb.ai.rentservice.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bike")
public class Bike {

    @Id
    @Column(name = "bike_id")
    private String bikeId;

    @Column(name = "status")
    private String status;

    @Column(name = "pin")
    private Integer pin;

    public Bike(){};

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "Bike{" +
                "bikeId='" + bikeId + '\'' +
                ", status='" + status + '\'' +
                ", pin=" + pin +
                '}';
    }

}
