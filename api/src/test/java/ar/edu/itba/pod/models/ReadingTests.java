package ar.edu.itba.pod.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadingTests {
    
    @Test
    public void testParsing (){
        // Arrange
        var year = 2018;
        var month = "May";
        var dayOfTheMonth = 29;
        var dayOfTheWeek = "Tuesday";
        var hourOfDay = 15;
        var sensorId = 51;
        var hourlyCount = 172;
        
        var row = String.join(";",
                "2249087",
                "May 29, 2018 03:00:00 PM",
                String.valueOf(year),
                month,
                String.valueOf(dayOfTheMonth),
                dayOfTheWeek,
                String.valueOf(hourOfDay) ,
                String.valueOf(sensorId),
                "QVM-Franklin St (North)",
                String.valueOf(hourlyCount)
                );
        
        // Act
        var reading = new Reading(row);
        
        // Assert
        assertThat(reading.getSensorId()).isEqualTo(sensorId);
        assertThat(reading.getYear()).isEqualTo(year);
        assertThat(reading.getMonth()).isEqualTo(Month.MAY);
        assertThat(reading.getDayOfTheMonth()).isEqualTo(dayOfTheMonth);
        assertThat(reading.getDayOfTheWeek()).isEqualTo(Day.TUESDAY);
        assertThat(reading.getHourOfTheDay()).isEqualTo(hourOfDay);
        assertThat(reading.getHourlyCount()).isEqualTo(hourlyCount);
    }
}
