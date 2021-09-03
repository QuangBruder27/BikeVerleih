package htwb.ai.authservice.Entity;

import javax.persistence.*;

@Entity
@Table(name ="custom")
public class Custom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="custom_id")
    private int custom_id;

    @Column(name="email")
    private String email;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    public Custom(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCustom_id() {
        return custom_id;
    }

    public void setCustom_id(int custom_id) {
        this.custom_id = custom_id;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "custom_id=" + custom_id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
