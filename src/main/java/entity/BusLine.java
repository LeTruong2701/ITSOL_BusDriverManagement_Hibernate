package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="BUS_LINE")
public class BusLine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUS_LINE_SEQ")
    @SequenceGenerator(name = "BUS_LINE_SEQ", sequenceName = "BUS_LINE_SEQ", allocationSize = 1, initialValue = 1)
    private int id;
    @Column(name = "distance")
    private double distance;
    @Column(name = "stop_station_number")
    private int stopStationNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getStopStationNumber() {
        return stopStationNumber;
    }

    public void setStopStationNumber(int stopStationNumber) {
        this.stopStationNumber = stopStationNumber;
    }
}
