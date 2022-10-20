package ar.edu.itba.pod.models;

import lombok.Getter;

public class Sensor {
    
    public Sensor(String row){
        String[] data = row.split(";");
        this.Id = Integer.parseInt(data[0]);
        this.Description = data[1];
        this.Status = switch (data[4]) {
            case "A" -> SensorStatus.ACTIVE;
            case "R" -> SensorStatus.REMOVED;           
            case "I" -> SensorStatus.INACTIVE;           
            default -> throw new IllegalArgumentException("Unknown status: " + data[4]);
        };
    }
    
    @Getter
    private final int Id;
    
    @Getter
    private final String Description;
    
    @Getter
    private final SensorStatus Status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sensor sensor = (Sensor) o;

        return Id == sensor.Id;
    }

    @Override
    public int hashCode() {
        return Id;
    }
}
