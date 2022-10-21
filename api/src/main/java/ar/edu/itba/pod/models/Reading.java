package ar.edu.itba.pod.models;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

import java.io.Serializable;

public class Reading implements Serializable {
    
    public Reading(String row){
        String[] data = row.split(";");
        this.Year = Integer.parseInt(data[2]);
        this.Month = EnumUtils.getEnumIgnoreCase(Month.class, data[3]);
        this.DayOfTheMonth = Integer.parseInt(data[4]);
        this.DayOfTheWeek = EnumUtils.getEnumIgnoreCase(Day.class, data[5]);
        this.HourOfTheDay = Integer.parseInt(data[6]);
        this.SensorId = Integer.parseInt(data[7]);
        this.HourlyCount = Integer.parseInt(data[9]);
    }
    
    @Getter 
    private final int Year;
    @Getter
    private final Month Month;
    @Getter
    private final int DayOfTheMonth;
    @Getter
    private final Day DayOfTheWeek;
    @Getter
    private final int HourOfTheDay;
    @Getter
    private final int HourlyCount;
    @Getter
    private final int SensorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reading reading = (Reading) o;

        if (Year != reading.Year) return false;
        if (DayOfTheMonth != reading.DayOfTheMonth) return false;
        if (HourOfTheDay != reading.HourOfTheDay) return false;
        if (HourlyCount != reading.HourlyCount) return false;
        if (SensorId != reading.SensorId) return false;
        if (Month != reading.Month) return false;
        return DayOfTheWeek == reading.DayOfTheWeek;
    }

    @Override
    public int hashCode() {
        int result = Year;
        result = 31 * result + Month.hashCode();
        result = 31 * result + DayOfTheMonth;
        result = 31 * result + DayOfTheWeek.hashCode();
        result = 31 * result + HourOfTheDay;
        result = 31 * result + HourlyCount;
        result = 31 * result + SensorId;
        return result;
    }
}
