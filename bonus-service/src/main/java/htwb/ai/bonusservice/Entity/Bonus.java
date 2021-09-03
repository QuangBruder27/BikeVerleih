package htwb.ai.bonusservice.Entity;

import javax.persistence.*;

@Entity
@Table(name = "bonus")
public class Bonus {

    @Id
    @Column(name = "custom_id")
    private String customId;

    @Column(name = "score")
    private double score;

    public Bonus(){}

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public String toString() {
        return "Bonus{" +
                "customId='" + customId + '\'' +
                ", score=" + score +
                '}';
    }
}
