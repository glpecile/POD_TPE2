package ar.edu.itba.pod.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SensorTests {
    
    @Test
    public void testParsing (){
        // Arrange
        var id = 45;
        var description = "Little Collins St-Swanston St (East)";
        var status = "A";
        
        var row = String.join(";",
            String.valueOf(id),
            description,
                "Swa148_T",
                "2017/06/29",
                status,
                "",
                "South",
                "North",
                "-37.81414074",
                "144.9660938",
                "(-37.81414074, 144.9660938)"
            );
        
        // Act
        var sensor = new Sensor(row);
        
        // Assert
        assertThat(sensor.getId()).isEqualTo(id);   
        assertThat(sensor.getDescription()).isEqualTo(description);
        assertThat(sensor.getStatus()).isEqualTo(SensorStatus.ACTIVE);
    }
}
