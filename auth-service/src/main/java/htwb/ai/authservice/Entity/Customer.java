package htwb.ai.authservice.Entity;

import javax.persistence.*;

@Entity
@Table(name ="customer")
public class Customer {

    @Id
    @Column(name="customer_id")
    private String customerId;

    @Column(name="email")
    private String email;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    public Customer(){}

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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isAcceptable(){
        return this.email!= null && !this.email.isEmpty()
                && this.password != null && !this.password.isEmpty();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
