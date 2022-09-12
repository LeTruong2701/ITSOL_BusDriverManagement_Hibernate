package dto;

import entity.BusLine;
import entity.Driver;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DriverBusManagementDto {
    Driver driver;
    Map<BusLine,Integer> assignedBuses;
    double totalDistance;

    public DriverBusManagementDto(Driver driver,Map<BusLine,Integer> assignedBuses){
        this.driver=driver;
        this.assignedBuses=assignedBuses;
    }


    public void setTotalDistance() {
        if (assignedBuses == null || assignedBuses.isEmpty()) {
            this.setTotalDistance();
        }
        AtomicReference<Double> totalDistance = new AtomicReference<>((double) 0);
        this.assignedBuses.forEach((busLine, round) -> totalDistance.updateAndGet(v -> v + busLine.getDistance() * round));
        this.totalDistance = totalDistance.get();

    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Map<BusLine, Integer> getAssignedBuses() {
        return assignedBuses;
    }

    public void setAssignedBuses(Map<BusLine, Integer> assignedBuses) {
        this.assignedBuses = assignedBuses;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
}
