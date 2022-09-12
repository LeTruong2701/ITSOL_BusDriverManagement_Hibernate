package dto;

import entity.BusLine;
import entity.Driver;

public class DriverBusManagementTempDto {
//    private final Integer roundNumber;
    Driver driver;

    BusLine busLine;

    Integer round;

    public DriverBusManagementTempDto(Driver driver, BusLine busLine, Integer roundNumber) {

    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public BusLine getBusLine() {
        return busLine;
    }

    public void setBusLine(BusLine busLine) {
        this.busLine = busLine;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }
}
