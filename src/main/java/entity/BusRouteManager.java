package entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DRIVER_BUS_MANAGEMENT")
public class BusRouteManager {
    @Id
    @Column(name = "driver_id")
    private double driverId;

    @Id
    @Column(name = "bus_line_id")
    private double busLineId;

    @Column(name = "ROUND_NUMBER", nullable = false)
    Integer roundNumber;

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public double getDriverId() {
        return driverId;
    }

    public void setDriverId(double driverId) {
        this.driverId = driverId;
    }

    public double getBusLineId() {
        return busLineId;
    }

    public void setBusLineId(double busLineId) {
        this.busLineId = busLineId;
    }
}
