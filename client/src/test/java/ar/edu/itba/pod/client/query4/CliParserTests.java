package ar.edu.itba.pod.client.query4;

import ar.edu.itba.pod.client.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CliParserTests {
    private CliParser parser;
    
    @BeforeEach
    public void setUp() {
        parser = new CliParser();
    }
    
    @Test
    public void withRequiredParameters_ShouldSucceed(){
        // Arrange
        var year = "2019";
        var n = "10";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dn=" + n,
                "-Dyear=" + year
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> {
                    var arg = (CliParser.Arguments) parsedArguments;
                    assertThat(arg.getN()).isEqualTo(Integer.parseInt(n));
                    assertThat(arg.getYear()).isEqualTo(Integer.parseInt(year));
                },
                () -> fail("Arguments should be parsed")
        );
    }
    
    @Test
    public void withoutN_ShouldFail(){
        // Arrange
        var year = "2019";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dyear=" + year
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void withoutYear_ShouldFail(){
        // Arrange
        var n = "10";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dn=" + n,
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void whenNIsNotANumber_ShouldFail(){
        // Arrange
        var year = "2019";
        var n = "prueba";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dn=" + n,
                "-Dyear=" + year
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void whenYearIsNotANumber_ShouldFail(){
        // Arrange
        var year = "prueba";
        var n = "1000";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dn=" + n,
                "-Dyear=" + year
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void whenNIsNegative_ShouldFail(){
        // Arrange
        var year = "2019";
        var n = "-1000";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dn=" + n,
                "-Dyear=" + year
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void whenYearIsNegative_ShouldFail(){
        // Arrange
        var year = "-2019";
        var n = "1000";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dn=" + n,
                "-Dyear=" + year
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void whenNIsZero_ShouldFail(){
        // Arrange
        var year = "2019";
        var n = "0";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dn=" + n,
                "-Dyear=" + year
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
       
    }
    
    @Test
    public void whenYearIsZero_ShouldFail(){
        // Arrange
        var year = "0";
        var n = "1000";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dn=" + n,
                "-Dyear=" + year
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
}
