package htwb.ai.bonusservice.Entity;

import javax.persistence.*;

@Entity
@Table(name = "bonus")
public class Bonus {

    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "score")
    private Integer score;

    public Bonus(){}

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Bonus{" +
                "customerId='" + customerId + '\'' +
                ", score=" + score +
                '}';
    }
}
