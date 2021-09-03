package htwb.ai.authservice.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="bike_auth")
public class BikeAuth {

    @Id
    @Column(name = "bike_id")
    private String bikeId;

    @Column(name = "password")
    private String password;

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "BikeAuth{" +
                "bikeId='" + bikeId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
