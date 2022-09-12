package entity;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="BUS_DRIVER")
public class Driver implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUS_DRIVER_SEQ")
    @SequenceGenerator(name = "BUS_DRIVER_SEQ", sequenceName = "BUS_DRIVER_SEQ", allocationSize = 1, initialValue = 1)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "driver_level")
    private String driverLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDriverLevel() {
        return driverLevel;
    }

    public void setDriverLevel(String driverLevel) {
        this.driverLevel = driverLevel;
    }
}
